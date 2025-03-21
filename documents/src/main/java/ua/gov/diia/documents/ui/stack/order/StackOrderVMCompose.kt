package ua.gov.diia.documents.ui.stack.order

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ua.gov.diia.core.network.Http
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst.ACTION_NAVIGATE_BACK
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRatingDialogOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.documents.R
import ua.gov.diia.documents.data.repository.DocumentsDataRepository
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.documents.models.DocOrder
import ua.gov.diia.documents.models.DocumentOrderModel
import ua.gov.diia.core.models.document.SourceType
import ua.gov.diia.documents.models.TypeDefinedDocOrder
import ua.gov.diia.documents.ui.DocsConst
import ua.gov.diia.ui_base.mappers.document.DocumentComposeMapper
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.molecule.list.ListItemDragMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemDragOrgData
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.ui_base.util.navigation.generateComposeNavigationPanel
import javax.inject.Inject

@HiltViewModel
class StackOrderVMCompose @Inject constructor(
    private val documentsDataSource: DocumentsDataRepository,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val withRetryLastAction: WithRetryLastAction,
    private val withRatingDialog: WithRatingDialogOnFlow,
    private val composeMapper: DocumentComposeMapper,
    private val documentsHelper: DocumentsHelper
) : ViewModel(),
    WithErrorHandlingOnFlow by errorHandling,
    WithRetryLastAction by withRetryLastAction,
    DocumentComposeMapper by composeMapper,
    WithRatingDialogOnFlow by withRatingDialog {

    private val _bodyData = mutableStateListOf<UIElementData>()
    val bodyData: SnapshotStateList<UIElementData> = _bodyData

    private val _topBarData = mutableStateListOf<UIElementData>()
    val topBarData: SnapshotStateList<UIElementData> = _topBarData

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    private var docType: String = DocsConst.DOCUMENT_TYPE_ALL
    private var originalOrder: List<DiiaDocumentWithMetadata>? = null

    fun doInit(docType: String) {
        this.docType = docType
        documentsDataSource.invalidate()
        
        viewModelScope.launch {
            documentsDataSource.data.collectLatest { documents ->
                documents.data?.let { dataResult ->
                    val docs = if (docType == DocsConst.DOCUMENT_TYPE_ALL) {
                        dataResult
                            .asSequence()
                            .filter { it.diiaDocument != null && (it.diiaDocument as DiiaDocument).getSourceType() != SourceType.STATIC }
                            .filter { d -> documentsHelper.isDocumentValid(d) }
                            .groupBy { it.diiaDocument!!.getItemType() }
                            .mapNotNull { diaDocument -> diaDocument.value.minByOrNull { it.diiaDocument!!.getDocOrder() }!! }
                            .sortedBy { it.getDocOrder() }
                            .toList()
                    } else {
                        dataResult
                            .filter { it.diiaDocument != null && (it.diiaDocument as DiiaDocument).getItemType() == docType }
                            .sortedBy { it.diiaDocument!!.getDocOrder() }
                    }

                    val list = if (docType == DocsConst.DOCUMENT_TYPE_ALL) {
                        docs.toStateList { type -> dataResult.count { it.type == type } }
                    } else {
                        docs.toTypeStateList()
                    }

                    originalOrder = docs

                    _topBarData.clear()
                    _topBarData.addIfNotNull(getNavigationData(docs.getOrNull(0)))

                    _bodyData.clear()
                    _bodyData.add(
                        ListItemDragOrgData(
                            list,
                            componentId = UiText.StringResource(R.string.stack_order_docs_list_test_tag)
                        )
                    )
                }
            }
        }
    }


    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.LIST_ITEM_CLICK -> {
                val type = event.data ?: return
                _navigation.tryEmit(Navigation.ToStackTypedOrder(type))
            }
            UIActionKeysCompose.TITLE_GROUP_MLC -> {
                event.action?.type?.let {
                    if (it == ACTION_NAVIGATE_BACK) {
                        _navigation.tryEmit(BaseNavigation.Back)
                    }
                }
            }
        }
    }

    private fun getDocOrder(documents: List<ListItemDragMlcData>): List<DocumentOrderModel> {
        return if (docType == DocsConst.DOCUMENT_TYPE_ALL) {
            documents.mapIndexed { index, documentCard ->
                DocOrder(documentCard.id, index.inc())
            }
        } else {
            documents.mapIndexed { index, documentCard ->
                TypeDefinedDocOrder(documentCard.id, index.inc())
            }
        }
    }

    private fun List<DiiaDocumentWithMetadata>?.toTypeStateList(): SnapshotStateList<ListItemDragMlcData> {
        val list = SnapshotStateList<ListItemDragMlcData>()
        if (this == null) return list
        forEach {
            if (it.diiaDocument?.getStatus() == Http.HTTP_204) return@forEach
            it.toTypedDragMlcData()?.let { d -> list.add(d) }
        }
        return list
    }

    private fun List<DiiaDocumentWithMetadata>?.toStateList(countOfDocs: (type: String) -> Int): SnapshotStateList<ListItemDragMlcData> {
        val list = SnapshotStateList<ListItemDragMlcData>()
        if (this == null) return list
        forEach {
            if (it.diiaDocument?.getStatus() == Http.HTTP_204) return@forEach
            it.toDragMlcData(countOfDocs)?.let { d -> list.add(d) }
        }
        return list
    }

    private fun DiiaDocumentWithMetadata.toDragMlcData(countOfDocs: (type: String) -> Int): ListItemDragMlcData? {
        val label = getStackLabel(diiaDocument)
        return if (label != null) {
            ListItemDragMlcData(
                id = type,
                label = label,
                countOfDocGroup = countOfDocs(type)
            )
        } else null
    }

    private fun DiiaDocumentWithMetadata.toTypedDragMlcData(): ListItemDragMlcData? {
        if (diiaDocument == null) return null
        val doc = diiaDocument as DiiaDocument
        val id = doc.getDocNum() ?: doc.docId()
        val num = doc.getDocOrderLabel()
        val date = doc.getDocOrderDescription() ?: doc.getDisplayDate()
        return if (!num.isNullOrEmpty()) {
            ListItemDragMlcData(
                id = id,
                label = UiText.DynamicString(num),
                desc = UiText.DynamicString(date)
            )
        } else null
    }

    private fun getStackLabel(diiaDocument: DiiaDocument?): UiText? {
        return if (diiaDocument?.getDocStackTitle() != null && diiaDocument.getDocStackTitle()
                .isNotEmpty()
        ) {
            diiaDocument.getDocStackTitle().toDynamicString()
        } else if (diiaDocument?.getDocName() != null) {
            diiaDocument.getDocName()?.let {
                UiText.DynamicString(it)
            }
        } else {
            null
        }
    }

    fun onMove(a: Int, b: Int) {
        _bodyData.findAndChangeFirstByInstance<ListItemDragOrgData> {
            val list = it.items.toMutableList()
            list.add(a, list.removeAt(b))

            val state = SnapshotStateList<ListItemDragMlcData>().apply { addAll(list) }
            ListItemDragOrgData(state)
        }
    }

    fun saveCurrentOrder() {
        val element = (_bodyData.find { it is ListItemDragOrgData } as? ListItemDragOrgData)
        val currentOrder = getDocOrder(element?.items.orEmpty())

        if (docType == DocsConst.DOCUMENT_TYPE_ALL) {
            documentsDataSource.saveDocTypeOrder(currentOrder as List<DocOrder>)
        } else {
            documentsDataSource.saveDocOrderForSpecificType(
                docOrders = currentOrder as List<TypeDefinedDocOrder>,
                docType = docType
            )
        }
    }

    private fun getNavigationData(docData: DiiaDocumentWithMetadata?): UIElementData {
        return when (docType) {
            DocsConst.DOCUMENT_TYPE_ALL -> {
                TopGroupOrgData(
                    titleGroupMlcData = TitleGroupMlcData(
                        heroText = UiText.StringResource(R.string.stack_order_title),
                        leftNavIcon = TitleGroupMlcData.LeftNavIcon(
                            code = DiiaResourceIcon.BACK.code,
                            accessibilityDescription = UiText.StringResource(R.string.accessibility_back_button),
                            action = DataActionWrapper(
                                type = ACTION_NAVIGATE_BACK,
                                subtype = null,
                                resource = null
                            )
                        ),
                        componentId = UiText.StringResource(R.string.stack_order_title_test_tag)
                    )
                )
            }

            else -> {
                val name =
                    if (docData?.diiaDocument?.getDocStackTitle() != null
                        && (docData.diiaDocument as DiiaDocument).getDocStackTitle().isNotEmpty()
                    ) {
                        (docData.diiaDocument as DiiaDocument).getDocStackTitle().toDynamicString()
                    } else {
                        docData?.diiaDocument?.getDocName().toDynamicString()
                    }
                generateComposeNavigationPanel(uiTextTitle = name)
            }
        }
    }

    sealed class Navigation : NavigationPath {
        data class ToStackTypedOrder(val type: String) : Navigation()
    }

}
