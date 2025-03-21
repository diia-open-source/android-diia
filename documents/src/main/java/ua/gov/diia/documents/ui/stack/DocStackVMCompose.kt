package ua.gov.diia.documents.ui.stack

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import ua.gov.diia.core.di.actions.GlobalActionConfirmDocumentRemoval
import ua.gov.diia.core.di.actions.GlobalActionFocusOnDocument
import ua.gov.diia.core.di.actions.GlobalActionSelectedMenuItem
import ua.gov.diia.core.models.common.BackStackEvent
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.models.share.ShareByteArr
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRatingDialogOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeRepository
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeResult
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeResultLoading
import ua.gov.diia.documents.data.api.ApiDocuments
import ua.gov.diia.documents.data.repository.DocumentsDataRepository
import ua.gov.diia.documents.di.GlobalActionUpdateDocument
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.core.models.document.DocumentCard
import ua.gov.diia.core.models.document.LocalizationType
import ua.gov.diia.documents.ui.DocVM
import ua.gov.diia.documents.ui.DocsConst
import ua.gov.diia.ui_base.mappers.document.DocumentComposeMapper
import ua.gov.diia.ui_base.mappers.document.ToggleId
import ua.gov.diia.documents.ui.WithPdfCertificate
import ua.gov.diia.documents.ui.WithPdfDocument
import ua.gov.diia.documents.ui.WithRemoveDocument
import ua.gov.diia.documents.ui.gallery.DocActions.DOC_ACTION_OPEN_LINK
import ua.gov.diia.documents.ui.gallery.DocActions.DOC_ACTION_OPEN_NEW_FLOW
import ua.gov.diia.documents.ui.gallery.DocActions.DOC_ACTION_OPEN_WEB_VIEW
import ua.gov.diia.documents.ui.gallery.DocActions.DOC_COVER_ACTION_DELETE
import ua.gov.diia.documents.util.AndroidClientAlertDialogsFactory
import ua.gov.diia.documents.util.AndroidClientAlertDialogsFactory.Companion.DOC_REMOVE
import ua.gov.diia.documents.util.AndroidClientAlertDialogsFactory.Companion.REGISTER_NOT_AVAILABLE
import ua.gov.diia.documents.util.WithUpdateExpiredDocs
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.DocAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.organism.pager.DocCardFlipData
import ua.gov.diia.ui_base.components.organism.pager.DocCarouselOrgData
import ua.gov.diia.ui_base.components.organism.pager.DocsCarouselItem
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.ui_base.util.navigation.generateComposeNavigationPanel
import javax.inject.Inject

@HiltViewModel
class DocStackVMCompose @Inject constructor(
    private val apiDocs: ApiDocuments,
    @GlobalActionConfirmDocumentRemoval val globalActionConfirmDocumentRemoval: MutableStateFlow<UiDataEvent<String>?>,
    @GlobalActionUpdateDocument val globalActionUpdateDocument: MutableStateFlow<UiDataEvent<DiiaDocument>?>,
    @GlobalActionFocusOnDocument val globalActionFocusOnDocument: MutableStateFlow<UiDataEvent<String>?>,
    @GlobalActionSelectedMenuItem val globalActionSelectedMenuItem: MutableStateFlow<UiDataEvent<HomeMenuItemConstructor>?>,
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
    private val withRemoveDocument: WithRemoveDocument,
    private val documentsHelper: DocumentsHelper,
    private val clientAlertDialogsFactory: AndroidClientAlertDialogsFactory
) : ViewModel(),
    DocVM,
    WithErrorHandlingOnFlow by errorHandling,
    WithRetryLastAction by withRetryLastAction,
    DocumentComposeMapper by composeMapper,
    WithRatingDialogOnFlow by withRatingDialog,
    WithPdfCertificate by withPdfCertificate,
    WithPdfDocument by withPdfDocument {

    private val _bodyData = mutableStateOf<DocCarouselOrgData?>(value = null)
    val bodyData: State<DocCarouselOrgData?> = _bodyData

    private val _topBarData = mutableStateListOf<UIElementData>()
    val topBarData: SnapshotStateList<UIElementData> = _topBarData

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val navigation = _navigation.asSharedFlow()

    val navigationBackStackEventFlow = mutableSharedFlowOf<BackStackEvent>()

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
    private val _contentLoadedKey = MutableStateFlow(UIActionKeysCompose.PAGE_LOADING_TRIDENT)
    private val _contentLoaded = MutableStateFlow(true)
    val contentLoaded: Flow<Pair<String, Boolean>> =
        _contentLoaded.combine(_contentLoadedKey) { value, key ->
            key to value
        }

    private val _checkStack = MutableStateFlow(true)

    private var _error = MutableLiveData<UiDataEvent<TemplateDialogModel>>()
    val error = _error.asLiveData()

    private val _documentCardData = MutableLiveData<List<DocumentCard>>()
    val documentCardData = _documentCardData.asLiveData()

    private val _scrollToFirstPosition = MutableLiveData<UiEvent>()

    private val menuDocumentSelected = MutableLiveData<HomeMenuItemConstructor?>()

    private var currPos: Int = 0

    private var documentToFocus: Int? = null
    private var focusDocType: String? = null
    private var removeDoc: DiiaDocument? = null

    private var currentDoc: DiiaDocument? = null

    init {
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
                    showConfirmDeleteTemplateLocal()
                }
            }
        }

        viewModelScope.launch {
            globalActionFocusOnDocument.collectLatest { event ->
                event?.getContentIfNotHandled()?.let { type ->
                    focusDocType = type
                    updateExpDate(focusDocType ?: type)
                    documentsDataSource.invalidate()
                }
            }
        }

    }

    fun subscribeForDocuments(docType: String) {
        viewModelScope.launch {
            documentsDataSource.data
                .flowOn(dispatcherProvider.ioDispatcher())
                .mapNotNull { it.data }
                .map { it.filter { d -> d.diiaDocument != null } }
                .map { docs ->
                    docs.filter { it.type == docType }
                        .sortedBy { it.diiaDocument!!.getDocOrder() }
                        .map { DocumentCard(it.copy(diiaDocument = it.diiaDocument?.makeCopy())) }
                        .also { documents ->
                            focusDocType?.let { type ->
                                documents.find { it.doc.type == type }?.let {
                                    documentToFocus = documents.indexOf(it)
                                    focusDocType = null
                                }
                            }
                        }
                }.collect {
                    if (it != documentCardData.value) {
                        _documentCardData.value = it
                        _documentCardData.value?.let { docCard ->
                            if (docCard.size <= 1 && _checkStack.value) {
                                _navigation.tryEmit(
                                    BaseNavigation.Back
                                )
                            } else {
                                currPos = setPrevPos(currPos)
                            }
                        }
                        configureBody(it)
                    }
                }
        }
    }

    private fun configureBody(data: List<DocumentCard>) {
        if (!isDocCardFlipped(currPos.toString())) {
            _bodyData.value = composeMapper.toDocCarousel(
                data,
                barcodeResult = DocumentBarcodeResultLoading(loading = false)
            ).copy(focusOnDoc = currPos)

        }
    }

    override fun scrollToLastDocPos() {
        _bodyData.value = _bodyData.value?.copy(focusOnDoc = currPos)
    }

    private fun onFlip(
        position: Int,
        result: DocumentBarcodeResult,
        localizationType: LocalizationType,
        showToggle: Boolean
    ) {

        val oldValue: DocCarouselOrgData = _bodyData.value ?: return

        if (position >= 0 && position < oldValue.data.size) {
            val updatedData = oldValue.data.toMutableList()

            val updatedItem: DocsCarouselItem = when (val currentItem = updatedData[position]) {
                is DocCardFlipData -> {
                    currentItem.copy(
                        back = toComposeDocCodeOrg(result, localizationType, showToggle)
                    )
                }

                else -> currentItem // Handle other cases if needed
            }

            updatedData[position] = updatedItem
            _bodyData.value = _bodyData.value?.copy(data = updatedData.toMutableStateList())
        }
        _docAction.tryEmit(DocActions.DefaultBrightness)
    }

    private fun onToggleClick(toggleId: String, position: Int) {
        val docCarousel = _bodyData.value ?: return
        _bodyData.value = docCarousel.onToggleClick(position, toggleId)

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

        val oldValue: DocCarouselOrgData = _bodyData.value ?: return

        if (position >= 0 && position < oldValue.data.size) {
            val updatedData = oldValue.data.toMutableList()

            val updatedItem: DocsCarouselItem = when (val currentItem = updatedData[position]) {
                is DocCardFlipData -> {
                    currentItem.copy(
                        back = toComposeDocCodeOrg(result, localizationType, showToggle)
                    )
                }

                else -> currentItem
            }

            updatedData[position] = updatedItem
            _bodyData.value = oldValue.copy(data = updatedData.toMutableStateList())
        }
        onToggleClick(toggleId, position)
    }

    fun configureTopBar(title: String) {
        _topBarData.addIfNotNull(generateComposeNavigationPanel(title = title))
    }

    override fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.DOC_CARD_SWIPE_FINISHED -> {
                _bodyData.value = _bodyData.value?.flipAllCardsToFrontSideIfNeeded()
                _docAction.tryEmit(DocActions.DefaultBrightness)
            }

            UIActionKeysCompose.DOC_CARD_FLIP -> {
                val data = event.data
                val optionalId = event.optionalId
                optionalId?.let {
                    _bodyData.value = _bodyData.value?.flipCard(optionalId.toIntOrNull() ?: 0)
                }
                if (data != null && optionalId != null) {
                    val doc = findDoc(data, optionalId.toIntOrNull() ?: 0)
                    if (doc != null && isDocCardFlipped(optionalId)) {
                        loadQR(doc, optionalId.toIntOrNull() ?: 0, null)
                    }
                }
            }

            UIActionKeysCompose.TICKER_ATOM_CLICK -> {
                if (event.data != null && event.optionalId != null) {
                    val doc = findDoc(event.data.toString(), event.optionalId?.toIntOrNull() ?: 0)
                    if (doc != null) {
                        _navigation.tryEmit(Navigation.NavToVehicleInsurance(doc))

                    }
                }
            }

            UIActionKeysCompose.DOC_CARD_FORCE_FLIP -> {
                val data = event.data
                val optionalId = event.optionalId
                optionalId?.let {
                    _bodyData.value = _bodyData.value?.flipCardForceWithToggle(
                        optionalId.toIntOrNull() ?: 0,
                        event.optionalType
                    )
                }
                if (data != null && optionalId != null) {
                    val doc = findDoc(data, optionalId.toIntOrNull() ?: 0)
                    if (doc != null) {
                        loadQR(doc, optionalId.toIntOrNull() ?: 0, event.optionalType)
                    }
                }

            }

            UIActionKeysCompose.TOGGLE_BUTTON_MOLECULE -> {
                val data = event.data
                val optionalId = event.optionalId
                if (data != null && optionalId != null) {
                    onToggleClick(data, optionalId.toIntOrNull() ?: 0)
                }
            }

            UIActionKeysCompose.DOC_NUMBER_COPY -> {
                event.data?.let {
                    _docAction.tryEmit(DocActions.DocNumberCopy(it))

                }
            }

            UIActionKeysCompose.REFRESH_BUTTON -> {
                val data = event.data
                val optionalId = event.optionalId
                if (data != null && optionalId != null) {
                    val doc = findDoc(data, optionalId.toIntOrNull() ?: 0)
                    if (doc != null) {
                        loadQR(doc, optionalId.toIntOrNull() ?: 0, null)
                    }
                }
            }

            UIActionKeysCompose.DOC_ELLIPSE_MENU -> {
                val data = event.data
                val optionalId = event.optionalId
                if (data != null && optionalId != null) {
                    val doc = findDoc(data, optionalId.toIntOrNull() ?: 0)
                    if (doc != null) {
                        _navigation.tryEmit(
                            Navigation.ToDocActions(
                                doc,
                                optionalId.toIntOrNull() ?: 0
                            )
                        )
                    }
                }
            }

            UIActionKeysCompose.DOC_PAGE_SELECTED -> {
                event.data?.toIntOrNull()?.let { currPos = it }
            }

            DOC_ACTION_OPEN_WEB_VIEW -> {
                val url = event.action?.resource ?: return
                _docAction.tryEmit(DocActions.OpenWebView(url))
            }

            DOC_ACTION_OPEN_LINK -> {
                val link = event.action?.resource ?: return
                _docAction.tryEmit(DocActions.OpenLink(link))
            }

            DOC_ACTION_OPEN_NEW_FLOW -> {
                _docAction.tryEmit(DocActions.OpenNewFlow(event.data ?: return))
            }


            DOC_COVER_ACTION_DELETE -> {
                val data = event.data
                val optionalId = event.optionalId
                if (data != null && optionalId != null) {
                    val doc = findDoc(data, optionalId.toIntOrNull() ?: 0)
                    if (doc != null) {
                        removeDoc(doc)
                    }
                }
            }
        }
    }

    fun invalidateDataSource() {
        documentsDataSource.invalidate()
    }

    override fun invalidateAndRemove() {
        executeActionOnFlow {
            removeDoc?.let {
                forceUpdateDocument(it)
                removeDoc = null
            }
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
                removeDoc = doc
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

    override fun showRemoveDocDialog(doc: DiiaDocument) {
        currentDoc = doc
        showTemplateDialog(clientAlertDialogsFactory.showCustomAlert(DOC_REMOVE))
    }

    fun stopLoading() = _contentLoaded.tryEmit(true)

    private fun findDoc(docName: String, position: Int): DiiaDocument? {
        val documentCardList =
            documentCardData.value?.toList() // Make a copy to avoid potential modifications

        return documentCardList?.find { documentCard ->
            val itemTypeMatches = documentCard.doc.diiaDocument?.getItemType() == docName
            val indexMatches = documentCardList.indexOf(documentCard) == position
            itemTypeMatches && indexMatches
        }?.doc?.diiaDocument
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
        _checkStack.value = false
        executeActionOnFlow(
            progressIndicator = _contentLoaded.also {
                _contentLoadedKey.value =
                    UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_UI_BLOCKING
            }
        ) {
            withRemoveDocument.removeDocument(diiaDocument, ::showTemplateDialog, ::removeDocument)
        }
    }

    private fun removeDocument(diiaDocument: DiiaDocument) {
        executeActionOnFlow {
            documentsDataSource.removeDocument(diiaDocument)
            documentsDataSource.invalidate()
            stopLoading()
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

            val matchingDoc = documentsDataSource.data.mapNotNull { docs ->
                docs.data?.firstOrNull { it.diiaDocument?.docId() == doc.docId() }
            }.firstOrNull()

            matchingDoc?.let {
                handleUpdatedDocResponse(
                    oldDoc = it,
                    updatedDocResponse = response
                )
            }
        }
    }

    private fun handleUpdatedDocResponse(
        oldDoc: DiiaDocumentWithMetadata,
        updatedDocResponse: List<DiiaDocumentWithMetadata>?
    ) {
        val newDoc = updatedDocResponse?.firstOrNull()
        val status = newDoc?.status
        when (status) {
            DocsConst.DOCUMENT_UPDATED_STATUS, in 400..499 -> handleDocumentUpdatedStatus(
                oldDoc,
                updatedDocResponse
            )

            in 500..599 -> {
                handleServerErrorDocStatus(oldDoc)
            }

            else -> showTemplateDialog(
                clientAlertDialogsFactory
                    .showCustomAlert(REGISTER_NOT_AVAILABLE)
            )
        }
    }

    private fun handleDocumentUpdatedStatus(
        oldDoc: DiiaDocumentWithMetadata,
        updatedDocResponse: List<DiiaDocumentWithMetadata>?
    ) {
        oldDoc.diiaDocument?.getItemType()?.let { documentsDataSource.removeDocumentByType(it) }
        updatedDocResponse?.let { updatedDocsList ->
            updatedDocsList.forEach { diiaDocumentWithMetadata ->
                diiaDocumentWithMetadata.diiaDocument?.let {
                    updateDocument(it)
                }
                if (getStackCurrentDocCount() < updatedDocsList.size) {
                    updatedDocsList.forEach {
                        documentsDataSource.attachExternalDocument(it)
                    }
                }
            }
        }
    }

    private fun handleServerErrorDocStatus(oldDoc: DiiaDocumentWithMetadata) {
        documentsDataSource.attachExternalDocument(oldDoc)
    }

    private fun updateExpDate(type: String) {
        executeActionOnFlow(dispatcher = dispatcherProvider.work) {
            withUpdateExpiredDocs.updateExpirationDate(type)
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

    override fun loadImageAndShare(docType: String, docId: String) {
        executeActionOnFlow(
            dispatcher = Dispatchers.IO,
            contentLoadedIndicator = _contentLoaded.also {
                _contentLoadedKey.value = UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_UI_BLOCKING
            }
        ) {
            documentsHelper.loadDocImageInByteArray(docType, docId)?.let { data ->
                _docAction.tryEmit(DocActions.ShareImage(data))
            }
        }
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

    override fun checkStack() {
        _checkStack.value = true
    }

    override fun showConfirmDeleteTemplateLocal() {
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


    fun checkStackCount() {
        _documentCardData.value?.let { docCard ->
            if (docCard.size == 1 && _checkStack.value) {
                _navigation.tryEmit(
                    BaseNavigation.Back
                )
            }
        }
    }

    private fun isDocCardFlipped(optionalId: String): Boolean {
        val docCarousel = _bodyData.value ?: return false
        return docCarousel.isDocCardFlipper(optionalId.toIntOrNull())
    }

    private fun getStackCurrentDocCount(): Int {
        val docCarousel = _bodyData.value ?: return 0
        return docCarousel.data.size
    }

    private companion object {
        private const val ACTION_HOME_DOCUMENTS = 1
    }

    sealed class Navigation : NavigationPath {
        data class ToDocActions(val doc: DiiaDocument, val position: Int) : Navigation()
        data class NavToVehicleInsurance(val doc: DiiaDocument) : Navigation()

    }

    sealed class DocActions : DocAction {
        data class OpenWebView(val url: String) : DocActions()
        data class OpenNewFlow(val code: String) : DocActions()
        data class OpenLink(val link: String) : DocActions()
        object HighBrightness : DocActions()
        object DefaultBrightness : DocActions()

        data class DocNumberCopy(val value: String) : DocActions()

        data class ShareImage(val data: ShareByteArr) : DocActions()
    }
}