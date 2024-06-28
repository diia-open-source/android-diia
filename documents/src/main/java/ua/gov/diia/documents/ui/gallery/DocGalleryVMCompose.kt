package ua.gov.diia.documents.ui.gallery

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import ua.gov.diia.core.di.actions.GlobalActionConfirmDocumentRemoval
import ua.gov.diia.core.di.actions.GlobalActionFocusOnDocument
import ua.gov.diia.core.di.actions.GlobalActionNetworkState
import ua.gov.diia.core.di.actions.GlobalActionSelectedMenuItem
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.models.share.ShareByteArr
import ua.gov.diia.core.network.connectivity.ConnectivityObserver
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRatingDialogOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.documents.barcode.DocumentBarcodeRepository
import ua.gov.diia.documents.barcode.DocumentBarcodeResult
import ua.gov.diia.documents.barcode.DocumentBarcodeResultLoading
import ua.gov.diia.documents.data.api.ApiDocuments
import ua.gov.diia.documents.data.repository.DocumentsDataRepository
import ua.gov.diia.documents.di.GlobalActionUpdateDocument
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata
import ua.gov.diia.documents.models.DocumentCard
import ua.gov.diia.documents.models.LocalizationType
import ua.gov.diia.documents.models.ManualDocs
import ua.gov.diia.documents.models.SourceType
import ua.gov.diia.documents.ui.DocVM
import ua.gov.diia.documents.ui.DocsConst
import ua.gov.diia.documents.ui.DocumentComposeMapper
import ua.gov.diia.documents.ui.ToggleId
import ua.gov.diia.documents.ui.WithCheckLocalizationDocs
import ua.gov.diia.documents.ui.WithPdfCertificate
import ua.gov.diia.documents.ui.WithPdfDocument
import ua.gov.diia.documents.ui.WithRemoveDocument
import ua.gov.diia.documents.ui.gallery.DocActions.DOC_ACTION_CALL
import ua.gov.diia.documents.ui.gallery.DocActions.DOC_ACTION_IN_LINE
import ua.gov.diia.documents.ui.gallery.DocActions.DOC_ACTION_TO_DRIVER_ACCOUNT
import ua.gov.diia.documents.util.WithUpdateExpiredDocs
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.DocAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.organism.pager.DocCardFlipData
import ua.gov.diia.ui_base.components.organism.pager.DocCarouselOrgData
import ua.gov.diia.ui_base.components.organism.pager.DocsCarouselItem
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor
import ua.gov.diia.ui_base.navigation.BaseNavigation
import javax.inject.Inject

@HiltViewModel
class DocGalleryVMCompose @Inject constructor(
    private val apiDocs: ApiDocuments,
    @GlobalActionConfirmDocumentRemoval val globalActionConfirmDocumentRemoval: MutableStateFlow<UiDataEvent<String>?>,
    @GlobalActionUpdateDocument val globalActionUpdateDocument: MutableStateFlow<UiDataEvent<DiiaDocument>?>,
    @GlobalActionFocusOnDocument val globalActionFocusOnDocument: MutableStateFlow<UiDataEvent<String>?>,
    @GlobalActionSelectedMenuItem val globalActionSelectedMenuItem: MutableStateFlow<UiDataEvent<HomeMenuItemConstructor>?>,
    @GlobalActionNetworkState private val connectivityObserver: ConnectivityObserver,
    private val barcodeRepository: DocumentBarcodeRepository,
    private val documentsDataSource: DocumentsDataRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val withRetryLastAction: WithRetryLastAction,
    private val withRatingDialog: WithRatingDialogOnFlow,
    private val composeMapper: DocumentComposeMapper,
    private val withUpdateExpiredDocs: WithUpdateExpiredDocs,
    private val withPdfCertificate: WithPdfCertificate,
    private val withPdfDocument: WithPdfDocument,
    private val withCheckLocalizationDocs: WithCheckLocalizationDocs,
    private val withRemoveDocument: WithRemoveDocument,
    private val documentsHelper: DocumentsHelper,
    private val clientAlertDialogsFactory: ClientAlertDialogsFactory
) : ViewModel(),
    DocVM,
    WithErrorHandlingOnFlow by errorHandling,
    WithRetryLastAction by withRetryLastAction,
    DocumentComposeMapper by composeMapper,
    WithRatingDialogOnFlow by withRatingDialog,
    WithPdfCertificate by withPdfCertificate,
    WithPdfDocument by withPdfDocument {

    private val _bodyData = mutableStateListOf<UIElementData>()
    val bodyData: SnapshotStateList<UIElementData> = _bodyData

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val navigation = _navigation.asSharedFlow()

    private val _docAction = MutableSharedFlow<DocAction>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val docAction = _docAction.asSharedFlow()

    private val _progressIndicatorKey = MutableStateFlow("")
    private val _progressIndicator = MutableStateFlow(false)
    val progressIndicator: Flow<Pair<String, Boolean>> =
        _progressIndicator.combine(_progressIndicatorKey) { value, key ->
            key to value
        }

    private val _contentLoadedKey =
        MutableStateFlow(UIActionKeysCompose.PAGE_LOADING_LINEAR_WITH_LABEL)
    private val _contentLoaded = MutableStateFlow(true)
    val contentLoaded: Flow<Pair<String, Boolean>> =
        _contentLoaded.combine(_contentLoadedKey) { value, key ->
            key to value
        }

    private val _openLink = MutableLiveData<UiDataEvent<String>>()
    val openLink = _openLink.asLiveData()

    private var _error = MutableLiveData<UiDataEvent<TemplateDialogModel>>()
    val error = _error.asLiveData()

    private val _documentCardData = MutableLiveData<List<DocumentCard>>()
    private val documentCardData = _documentCardData.asLiveData()

    private val _scrollToFirstPosition = MutableLiveData<UiEvent>()

    private val menuDocumentSelected =
        MutableLiveData<HomeMenuItemConstructor?>()

    val connectivity = MutableStateFlow(connectivityObserver.isAvailable)

    private var currPos: Int = 0
    private var currentDoc: DiiaDocument? = null

    private var focusDocType: String? = null
    private var isLocalizationChecked: Boolean = false

    init {
        viewModelScope.launch {
            connectivityObserver.observe().collect { value ->
                connectivity.emit(value)
            }
        }

        viewModelScope.launch {
            globalActionUpdateDocument.collectLatest { event ->
                event?.getContentIfNotHandled()?.let {
                    updateDocument(it)
                }
            }
        }
        viewModelScope.launch {
            globalActionSelectedMenuItem.collectLatest { event ->
                val previous = menuDocumentSelected.value
                menuDocumentSelected.value = event?.peekContent()
                if (previous?.position == ACTION_HOME_DOCUMENTS &&
                    menuDocumentSelected.value?.position == ACTION_HOME_DOCUMENTS
                ) {
                    _scrollToFirstPosition.postValue(UiEvent())
                }
            }
        }

        viewModelScope.launch {
            globalActionConfirmDocumentRemoval.collectLatest {
                it?.getContentIfNotHandled()?.let {
                    confirmDelDocument()
                }
            }
        }

        viewModelScope.launch {
            globalActionFocusOnDocument.collectLatest { event ->
                event?.getContentIfNotHandled()?.let { type ->
                    focusDocType = type
                    updateExpDate(type)
                    documentsDataSource.invalidate()
                }
            }
        }

        viewModelScope.launch {
            documentsDataSource.data
                .mapNotNull { it.data }
                .map { it.filter { d -> d.diiaDocument != null } }
                .map { it.filter { d -> documentsHelper.isDocumentValid(d) } }
                .map { docs ->
                    clearDocFocus()
                    if (!isLocalizationChecked) {
                        checkLocalizationDocs(docs)
                        isLocalizationChecked = true
                    }
                    docs.sortedBy { it.getDocOrder() }.groupBy { it.type }
                        .map { docGroup ->
                            if (docGroup.value.size > 1) {
                                val doc =
                                    docGroup.value.minByOrNull { it.diiaDocument!!.getDocOrder() }!!
                                DocumentCard(
                                    doc.copy(diiaDocument = doc.diiaDocument?.makeCopy()),
                                    docCount = docGroup.value.size
                                )
                            } else {
                                val doc = docGroup.value.first()
                                DocumentCard(doc.copy(diiaDocument = doc.diiaDocument?.makeCopy()))
                            }
                        }.also { documents ->
                            indexOfFocusedDoc(documents)?.let { scrollToDocByPos(it) }
                        }
                }
                .flowOn(dispatcherProvider.ioDispatcher())
                .collect {
                    if (it != documentCardData.value) {
                        _documentCardData.postValue(it)
                        configureBody(it)
                    }
                }
        }
    }

    private fun configureBody(data: List<DocumentCard>) {
        if (bodyData.isEmpty()) {
            _bodyData.addIfNotNull(
                composeMapper.toDocCarousel(
                    data,
                    barcodeResult = DocumentBarcodeResultLoading(loading = false)
                ).copy(focusOnDoc = currPos)
            )
        } else {
            if (!isDocCardFlipped(currPos.toString())) {
                _bodyData.findAndChangeFirstByInstance<DocCarouselOrgData> {
                    composeMapper.toDocCarousel(
                        data,
                        barcodeResult = DocumentBarcodeResultLoading(loading = false)
                    ).copy(focusOnDoc = currPos)
                }
            }
        }
    }

    private fun onFlip(
        position: Int,
        result: DocumentBarcodeResult,
        localizationType: LocalizationType,
        showToggle: Boolean
    ) {
        val index = _bodyData.indexOfFirst { it is DocCarouselOrgData }

        if (index != -1) {
            val oldValue: DocCarouselOrgData =
                _bodyData[index] as DocCarouselOrgData

            if (position >= 0 && position < oldValue.data.size) {
                val updatedData = oldValue.data.toMutableList()

                val updatedItem: DocsCarouselItem =
                    when (val currentItem = updatedData[position]) {
                        is DocCardFlipData -> {
                            currentItem.copy(
                                back = toComposeDocCodeOrg(
                                    result,
                                    localizationType,
                                    showToggle,
                                    isStack = currentItem.front.docButtonHeading?.isStack
                                        ?: false
                                )
                            )
                        }

                        else -> currentItem // Handle other cases if needed
                    }

                updatedData[position] = updatedItem
                _bodyData[index] =
                    oldValue.copy(data = updatedData.toMutableStateList())
            }
        }
        _docAction.tryEmit(DocActions.DefaultBrightness)
    }

    private fun onToggleClick(toggleId: String, position: Int) {
        val index = _bodyData.indexOfFirst { it is DocCarouselOrgData }
        if (index == -1) {
            return
        } else {
            _bodyData[index] =
                (_bodyData[index] as DocCarouselOrgData).onToggleClick(
                    position,
                    toggleId
                )
        }
        when (toggleId) {
            ToggleId.qr.value -> _docAction.tryEmit(DocActions.DefaultBrightness)
            ToggleId.ean.value -> _docAction.tryEmit(DocActions.HighBrightness)
        }

    }

    private fun onFlipAndToggle(
        position: Int,
        result: DocumentBarcodeResult,
        toggleId: String,
        localizationType: LocalizationType,
        showToggle: Boolean
    ) {
        val index = _bodyData.indexOfFirst { it is DocCarouselOrgData }

        if (index != -1) {
            val oldValue: DocCarouselOrgData =
                _bodyData[index] as DocCarouselOrgData

            if (position >= 0 && position < oldValue.data.size) {
                val updatedData = oldValue.data.toMutableList()

                val updatedItem: DocsCarouselItem =
                    when (val currentItem = updatedData[position]) {
                        is DocCardFlipData -> {
                            currentItem.copy(
                                back = toComposeDocCodeOrg(
                                    result,
                                    localizationType,
                                    showToggle,
                                    isStack = currentItem.front.docButtonHeading?.isStack
                                        ?: false
                                )
                            )
                        }

                        else -> currentItem // Handle other cases if needed
                    }

                updatedData[position] = updatedItem
                _bodyData[index] =
                    oldValue.copy(data = updatedData.toMutableStateList())
            }
        }

        onToggleClick(toggleId, position)
    }

    override fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.DOC_CARD_SWIPE_FINISHED -> {
                _bodyData.findAndChangeFirstByInstance<DocCarouselOrgData> {
                    it.flipAllCardsToFrontSideIfNeeded()
                }
                _docAction.tryEmit(DocActions.DefaultBrightness)
            }

            UIActionKeysCompose.DOC_CARD_FLIP -> {
                val data = event.data
                val optionalId = event.optionalId
                optionalId?.let {
                    _bodyData.findAndChangeFirstByInstance<DocCarouselOrgData> { docData ->
                        docData.flipCard(optionalId.toIntOrNull() ?: 0)
                    }
                }
                if (data != null && optionalId != null) {
                    val doc = findDoc(data)
                    if (doc != null && isDocCardFlipped(optionalId)) {
                        loadQR(doc, optionalId.toIntOrNull() ?: 0, null)
                    }
                }

            }

            UIActionKeysCompose.DOC_CARD_FORCE_FLIP -> {
                val data = event.data
                val optionalId = event.optionalId
                optionalId?.let {
                    _bodyData.findAndChangeFirstByInstance<DocCarouselOrgData> {
                        it.flipCardForceWithToggle(
                            optionalId.toIntOrNull() ?: 0,
                            event.optionalType
                        )
                    }
                }
                if (data != null && optionalId != null) {
                    val doc = findDoc(data)
                    if (doc != null) {
                        loadQR(
                            doc,
                            optionalId.toIntOrNull() ?: 0,
                            event.optionalType
                        )
                    }
                }

            }

            UIActionKeysCompose.TOGGLE_BUTTON_MOLECULE -> {
                val data = event.data
                val optionalId = event.optionalId
                if (data != null && optionalId != null) {
                    onToggleClick(
                        data,
                        optionalId.toIntOrNull() ?: 0
                    )
                }
            }

            UIActionKeysCompose.REFRESH_BUTTON -> {
                val data = event.data
                val optionalId = event.optionalId
                if (data != null && optionalId != null) {
                    val doc = findDoc(data)
                    if (doc != null) {
                        loadQR(doc, optionalId.toIntOrNull() ?: 0, null)
                    }
                }
            }

            UIActionKeysCompose.DOC_ELLIPSE_MENU -> {
                val data = event.data
                val optionalId = event.optionalId
                if (data != null && optionalId != null) {
                    val doc = findDoc(data)
                    if (doc != null) {
                        _navigation.tryEmit(
                            Navigation.ToDocActions(
                                doc,
                                optionalId.toIntOrNull() ?: 0, null
                            )
                        )
                    }
                }
            }

            UIActionKeysCompose.TICKER_ATOM_CLICK -> {
                val data = event.data
                val optionalId = event.optionalId
                if (data != null && optionalId != null) {
                    val doc = findDoc(data)
                    if (doc != null) {
                        _navigation.tryEmit(
                            Navigation.NavToVehicleInsurance(doc)
                        )
                    }
                }
            }

            UIActionKeysCompose.ADD_DOC_ORG -> {
                executeActionOnFlow(
                    templateKey = RESUlT_KEY_TEMP_RETRY,
                    contentLoadedIndicator = _contentLoaded.also {
                        _contentLoadedKey.value =
                            UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_UI_BLOCKING
                    }) {
                    val data = event.data
                    val optionalId = event.optionalId
                    if (data != null && optionalId != null) {
                        val doc = findDoc(data)
                        if (doc != null && doc.getSourceType() == SourceType.STATIC) {
                            _navigation.tryEmit(
                                Navigation.ToDocActions(
                                    doc,
                                    optionalId.toIntOrNull() ?: 0,
                                    apiDocs.getDocsManual()
                                ),
                            )
                        }
                    }
                }

            }

            UIActionKeysCompose.DOC_STACK -> {
                val data = event.data
                if (data != null) {
                    val doc = findDoc(data)
                    if (doc != null) {
                        _navigation.tryEmit(
                            Navigation.ToDocStack(
                                doc
                            )
                        )
                    }
                }
            }

            DOC_ACTION_IN_LINE -> {
                _docAction.tryEmit(DocActions.OpenElectronicQueue)
            }

            DOC_ACTION_TO_DRIVER_ACCOUNT -> {
                _docAction.tryEmit(DocActions.OpenDriverAccount)
            }

            DOC_ACTION_CALL -> {
                _docAction.tryEmit(DocActions.Call)
            }

            UIActionKeysCompose.CHANGE_DOC_ORDER -> {
                _navigation.tryEmit(Navigation.ToDocStackOrder)
            }

            UIActionKeysCompose.DOC_NUMBER_COPY -> {
                event.data?.let {
                    _docAction.tryEmit(DocActions.DocNumberCopy(event.data!!))
                }
            }

            UIActionKeysCompose.DOC_PAGE_SELECTED -> {
                event.data?.toIntOrNull()?.let { position ->
                    currPos = position
                }
            }

            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }
        }
    }

    private fun checkLocalizationDocs(docs: List<DiiaDocumentWithMetadata>?) {
        viewModelScope.launch {
            withCheckLocalizationDocs.checkLocalizationDocs(
                docs,
                ::updateExpDate
            )
        }
    }

    override fun invalidateAndScroll(type: String) {
        executeActionOnFlow {
            focusDocType = type
            documentsDataSource.invalidate()
        }
    }

    override fun getCertificatePdf(cert: DiiaDocument) {
        executeActionOnFlow(contentLoadedIndicator = _contentLoaded.also {
            _contentLoadedKey.value = UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_UI_BLOCKING
        }) {
            withPdfCertificate.loadCertificatePdf(cert)
        }
    }

    override fun getDocumentPdf(doc: DiiaDocument) {
        executeActionOnFlow(contentLoadedIndicator = _contentLoaded.also {
            _contentLoadedKey.value = UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_UI_BLOCKING
        }) {
            withPdfDocument.loadDocumentPdf(doc) {
                showTemplateDialog(it)
            }
        }
    }

    override fun addDocToGallery() {
        executeActionOnFlow(progressIndicator = _progressIndicator) {
            val template = documentsHelper.addDocToGallery()
            if (template != null) {
                showTemplateDialog(template)
            }
        }
    }

    override fun showRemoveDocDialog(key: String) {
        showTemplateDialog(
            clientAlertDialogsFactory.showDocRemoveDialog(
                key
            )
        )
    }

    override fun loadImageAndShare(docId: String) {
        executeActionOnFlow(
            dispatcher = Dispatchers.IO,
            progressIndicator = _progressIndicator
        ) {
            documentsHelper.loadDocImageInByteArray(docId)?.let { data ->
                _docAction.tryEmit(DocActions.ShareImage(data))
            }
        }
    }

    private fun findDoc(docName: String): DiiaDocument? {
        val doc = documentCardData.value?.find {
            it.doc.diiaDocument?.getItemType() == docName
        }
        currentDoc = doc?.doc?.diiaDocument
        return currentDoc
    }

    private fun loadQR(doc: DiiaDocument, position: Int, toggleId: String?) {
        executeActionOnFlow(
            dispatcher = dispatcherProvider.ioDispatcher(),
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value =
                    UIActionKeysCompose.DOC_CODE_ORG_DATA
            }
        ) {
            val barcodeResult = barcodeRepository.loadBarcode(doc, position)
            if (toggleId == null) {
                onFlip(
                    position,
                    barcodeResult.result,
                    doc.localization() ?: LocalizationType.ua,
                    barcodeResult.showToggle

                )

            } else {
                onFlipAndToggle(
                    position,
                    barcodeResult.result,
                    toggleId,
                    doc.localization() ?: LocalizationType.ua,
                    barcodeResult.showToggle
                )
            }
        }
    }

    override fun removeDoc(diiaDocument: DiiaDocument) {
        executeActionOnFlow {
            withRemoveDocument.removeDocument(diiaDocument, ::removeDocument)
        }
    }

    private fun removeDocument(diiaDocument: DiiaDocument) {
        documentsDataSource.removeDocument(diiaDocument)
        currPos = setPrevPos(currPos)
    }

    override fun confirmDelDocument() {
        executeActionOnFlow(
            progressIndicator = _progressIndicator
        ) {
            withRemoveDocument.confirmRemoveDocument(
                currentDoc,
                ::showTemplateDialog,
                ::removeDocument
            )
        }
    }

    private fun updateDocument(doc: DiiaDocument) {
        executeActionOnFlow {
            documentsDataSource.updateDocument(doc)
        }
    }

    override fun forceUpdateDocument(doc: DiiaDocument) {
        executeActionOnFlow(
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value = UIActionKeysCompose.DOC_ORG_DATA_UPDATE_DOC
            }
        ) {
            val response = apiDocs.fetchDocuments(
                listOf(doc.getItemType())
                    .withIndex()
                    .associateBy(
                        { "filter[${it.index}]" }, { it.value }
                    )
            )

            handleUpdatedDocResponse(
                oldDoc = doc,
                updatedDocResponse = response
            )
        }
    }

    private fun handleUpdatedDocResponse(
        oldDoc: DiiaDocument,
        updatedDocResponse: List<DiiaDocumentWithMetadata>?
    ) {
        when (updatedDocResponse?.firstOrNull()?.status) {
            DocsConst.DOCUMENT_UPDATED_STATUS -> handleDocumentUpdatedStatus(
                updatedDocResponse = updatedDocResponse
            )

            DocsConst.DOCUMENT_NOT_FOUND_STATUS -> handleDocumentNotFoundStatus(oldDoc)

            else -> showTemplateDialog(
                clientAlertDialogsFactory
                    .registerNotAvailable(docType = updatedDocResponse?.firstOrNull()?.type)
            )
        }
    }

    private fun handleDocumentUpdatedStatus(updatedDocResponse: List<DiiaDocumentWithMetadata>?) {
        updatedDocResponse?.let { updatedDocsList ->
            updatedDocsList.firstOrNull()?.diiaDocument?.let { updateDocument(it) }
            if (updatedDocsList.size > 1) {
                updatedDocsList.forEach {
                    documentsDataSource.attachExternalDocument(it)
                }
            }
        }
    }

    private fun handleDocumentNotFoundStatus(oldDoc: DiiaDocument) {
        documentsDataSource.removeDocument(oldDoc)
        currPos = setPrevPos(currPos)
        showTemplateDialog(clientAlertDialogsFactory.documentNotFound())
    }

    private fun updateExpDate(type: String) {
        executeActionOnFlow(dispatcher = dispatcherProvider.work) {
            withUpdateExpiredDocs.updateExpirationDate(type)
        }
    }

    private fun updateExpDate(types: List<String>) {
        executeActionOnFlow(dispatcher = dispatcherProvider.work) {
            withUpdateExpiredDocs.updateExpirationDate(types)
        }
    }

    private fun setPrevPos(position: Int): Int {
        return if (position > 0) {
            position - 1
        } else {
            0
        }
    }


    override fun sendRatingRequest(ratingRequest: RatingRequest) {
        currentDoc?.getItemType()
            ?.let { sendRating(ratingRequest, ActionsConst.DOCUMENTS_CODE, it) }
    }

    override fun currentDocId() = currentDoc?.docId()

    override fun showRating(doc: DiiaDocument) {
        executeActionOnFlow(
            progressIndicator = _contentLoaded.also {
                _contentLoadedKey.value =
                    UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_UI_BLOCKING
            }) {
            currentDoc = doc
            getRating(ActionsConst.DOCUMENTS_CODE, doc.getItemType())
        }
    }

    override fun stopLoading() {
        _contentLoadedKey.value = ""
    }

    override fun scrollToLastDocPos() {
        _bodyData.findAndChangeFirstByInstance<DocCarouselOrgData> {
            it.copy(focusOnDoc = currPos)
        }
    }

    private fun scrollToDocByPos(position: Int?) {
        _bodyData.findAndChangeFirstByInstance<DocCarouselOrgData> {
            it.copy(focusOnDoc = position)
        }
    }

    private fun indexOfFocusedDoc(data: List<DocumentCard>): Int? {
        focusDocType?.let { type ->
            data.find { it.doc.type == type }?.let {
                val index = data.indexOf(it)
                if (index == -1) return null
                currPos = index
                return index
            }
        }
        return null
    }

    private fun isDocCardFlipped(optionalId: String): Boolean {
        return _bodyData.firstOrNull {
            it is DocCarouselOrgData
        }?.let { uiElementData ->
            uiElementData as DocCarouselOrgData
            uiElementData.isDocCardFlipper(optionalId.toIntOrNull())
        } ?: false
    }

    private fun clearDocFocus() {
        _bodyData.findAndChangeFirstByInstance<DocCarouselOrgData> {
            it.copy(focusOnDoc = null)
        }
    }

    fun clearFocusType() {
        focusDocType = null
    }

    companion object {
        const val RESUlT_KEY_TEMP_RETRY = "result_action_preview_f_add_docs"
        const val ACTION_HOME_DOCUMENTS = 1
    }

    sealed class Navigation : NavigationPath {
        data class ToDocActions(
            val doc: DiiaDocument,
            val position: Int,
            val manualDocs: ManualDocs?
        ) : Navigation()

        data class ToDocStack(val doc: DiiaDocument) : Navigation()
        object ToDocStackOrder : Navigation()

        data class NavToVehicleInsurance(val doc: DiiaDocument) : Navigation()
    }

    sealed class DocActions : DocAction {
        object OpenDriverAccount : DocActions()
        object Call : DocActions()
        object OpenElectronicQueue : DocActions()
        object HighBrightness : DocActions()
        object DefaultBrightness : DocActions()

        data class ShareImage(val data: ShareByteArr) : DocActions()
        data class DocNumberCopy(val value: String) : DocActions()

    }
}
