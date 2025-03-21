package ua.gov.diia.documents.ui.stack.order

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRatingDialogOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.diia_storage.store.datasource.DataSourceDataResult
import ua.gov.diia.documents.DocTest
import ua.gov.diia.documents.DocTest2
import ua.gov.diia.documents.DocTest3
import ua.gov.diia.documents.DocTest4
import ua.gov.diia.documents.data.repository.DocumentsDataRepository
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.documents.rules.MainDispatcherRule
import ua.gov.diia.documents.ui.DocsConst
import ua.gov.diia.ui_base.mappers.document.DocumentComposeMapper
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.organism.list.ListItemDragOrgData

class StackOrderVMComposeTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val documentsDataSource: DocumentsDataRepository = mock()
    private val errorHandling: WithErrorHandlingOnFlow = mock()
    private val withRetryLastAction: WithRetryLastAction = mock()
    private val withRatingDialog: WithRatingDialogOnFlow = mock()
    private val composeMapper: DocumentComposeMapper = mock()
    private val documentsHelper: DocumentsHelper = mock()
    private val mockDoc = DiiaDocumentWithMetadata(DocTest(), "", "", 200, "")
    private val mockDoc2 = DiiaDocumentWithMetadata(DocTest2(), "", "", 200, "")
    private val mockDoc3 = DiiaDocumentWithMetadata(DocTest3(), "", "", 200, "")
    private val mockDoc4 = DiiaDocumentWithMetadata(DocTest4(), "", "", 200, "")
    private val documentsFlow: Flow<DataSourceDataResult<List<DiiaDocumentWithMetadata>>> =
        flowOf(DataSourceDataResult.successful(listOf(mockDoc, mockDoc2, mockDoc3, mockDoc4)))
    private val documentsFlowNull: Flow<DataSourceDataResult<List<DiiaDocumentWithMetadata>>> =
        flowOf(
            DataSourceDataResult.successful(
                listOf(
                    mockDoc.copy(diiaDocument = null),
                    mockDoc2.copy(diiaDocument = null)
                )
            )
        )

    private lateinit var vm: StackOrderVMCompose

    @Before
    fun before() {
        Mockito.`when`(documentsDataSource.data).thenReturn(documentsFlow)
        Mockito.`when`(documentsHelper.isDocumentValid(any()))
            .thenReturn(true)
        vm = StackOrderVMCompose(
            documentsDataSource,
            errorHandling,
            withRetryLastAction,
            withRatingDialog,
            composeMapper,
            documentsHelper
        )
    }

    @After
    fun after() {
        Mockito.clearAllCaches()
    }


    @Test
    fun `test init`() = runTest {
        vm.doInit(DocsConst.DOCUMENT_TYPE_ALL)
        advanceUntilIdle()
        val state = vm.bodyData.first() as ListItemDragOrgData
        Assert.assertEquals(
            3,
            state.items.size
        )
    }

    @Test
    fun `test init with type`() = runTest {
        vm.doInit("doc_test2")
        advanceUntilIdle()
        val state = vm.bodyData.first() as ListItemDragOrgData
        Assert.assertEquals(
            1,
            state.items.size
        )
    }

    @Test
    fun `test init with type and null doc`() = runTest {
        Mockito.`when`(documentsDataSource.data).thenReturn(documentsFlowNull)
        vm.doInit("doc_test2")
        advanceUntilIdle()
        val state = vm.bodyData.first() as ListItemDragOrgData
        Assert.assertEquals(
            0,
            state.items.size
        )
    }

    @Test
    fun `test init with type and empty doc org label`() = runTest {
        Mockito.`when`(documentsDataSource.data).thenReturn(documentsFlow)
        vm.doInit("doc_test3")
        advanceUntilIdle()
        val state = vm.bodyData.first() as ListItemDragOrgData
        Assert.assertEquals(
            0,
            state.items.size
        )
    }

    @Test
    fun `test move`() = runTest {
        vm.doInit(DocsConst.DOCUMENT_TYPE_ALL)
        advanceUntilIdle()
        val state = vm.bodyData.first() as ListItemDragOrgData
        val items = state.items.toList()
        vm.onMove(0, 1)
        val newState = vm.bodyData.first() as ListItemDragOrgData
        val newItems = newState.items.toList()
        Assert.assertEquals(items[0], newItems[1])
        Assert.assertEquals(items[1], newItems[0])
    }

    @Test
    fun `test save order`() = runTest {
        vm.doInit(DocsConst.DOCUMENT_TYPE_ALL)
        vm.saveCurrentOrder()
        verify(documentsDataSource).saveDocTypeOrder(any())
    }

    @Test
    fun `test save order for specific type`() = runTest {
        vm.doInit("doc_test2")
        vm.saveCurrentOrder()
        verify(documentsDataSource).saveDocOrderForSpecificType(any(), any())
    }

    //actions
    @Test
    fun `test TOOLBAR_NAVIGATION_BACK action`() = runTest {
        vm.navigation.test {
            vm.onUIAction(UIAction(actionKey = UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK))
            Assert.assertEquals(awaitItem(), BaseNavigation.Back)
        }
    }

    @Test
    fun `test LIST_ITEM_CLICK null action`() = runTest {
        var emissionFailed = false
        try {
            vm.navigation.test {
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.LIST_ITEM_CLICK,
                        data = null
                    )
                )
                awaitError()
            }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test LIST_ITEM_CLICK action`() = runTest {
        vm.navigation.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.LIST_ITEM_CLICK,
                    data = ""
                )
            )
            Assert.assertEquals(
                awaitItem(),
                StackOrderVMCompose.Navigation.ToStackTypedOrder("")
            )
        }
    }

    @Test
    fun `test TITLE_GROUP_MLC action`() = runTest {
        vm.navigation.test {
            vm.onUIAction(
                UIAction(
                    action = DataActionWrapper(ActionsConst.ACTION_NAVIGATE_BACK),
                    actionKey = UIActionKeysCompose.TITLE_GROUP_MLC
                )
            )
            Assert.assertEquals(awaitItem(), BaseNavigation.Back)
        }
    }


    @Test
    fun `test TITLE_GROUP_MLC null action`() = runTest {
        var emissionFailed = false
        try {
            vm.navigation.test {
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.LIST_ITEM_CLICK,
                        action = null
                    )
                )
                awaitError()
            }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test TITLE_GROUP_MLC other action`() = runTest {
        var emissionFailed = false
        try {
            vm.navigation.test {
                vm.onUIAction(
                    UIAction(
                        action = DataActionWrapper("other action"),
                        actionKey = UIActionKeysCompose.LIST_ITEM_CLICK
                    )
                )
                awaitError()
            }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }
}