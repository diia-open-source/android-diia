package ua.gov.diia.documents.ui.fullinfo

import android.os.Parcelable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.snapshots.SnapshotStateList
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.documents.DocTest
import ua.gov.diia.documents.barcode.DocumentBarcode
import ua.gov.diia.documents.barcode.DocumentBarcodeRepository
import ua.gov.diia.documents.barcode.DocumentBarcodeRepositoryResult
import ua.gov.diia.documents.barcode.DocumentBarcodeSuccessfulLoadResult
import ua.gov.diia.documents.getOrAwaitValue
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.LocalizationType
import ua.gov.diia.documents.rules.MainDispatcherRule
import ua.gov.diia.documents.ui.DocumentComposeMapper
import ua.gov.diia.documents.ui.ToggleId
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.button.BtnToggleMlcData
import ua.gov.diia.ui_base.components.organism.document.DocCodeOrgData
import ua.gov.diia.ui_base.components.organism.document.Localization
import ua.gov.diia.ui_base.components.organism.group.ToggleButtonGroupData
import java.util.concurrent.TimeoutException

class FullInfoFComposeVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProvider: DispatcherProvider = mock()
    private val errorHandling: WithErrorHandlingOnFlow = mock()
    private val withRetryLastAction: WithRetryLastAction = mock()
    private val composeMapper: DocumentComposeMapper = mock()
    private val barcodeRepository: DocumentBarcodeRepository = mock()
    private val docFullComposeMapper: DocFullInfoComposeMapper =
        object : DocFullInfoComposeMapper {
            override fun mapDocToBody(
                document: DiiaDocument,
                bodyData: SnapshotStateList<UIElementData>
            ) {
                bodyData.add(DocCodeOrgData(
                    "",
                    Localization.ua,
                    Mockito.mock(ToggleButtonGroupData::class.java),
                    null,
                    null,
                    null,
                    null,
                    null,
                    false,
                    true,
                    false
                ))
            }

        }
    private lateinit var vm: FullInfoFComposeVM

    @Before
    fun before() {
        Mockito.`when`(dispatcherProvider.ioDispatcher())
            .thenReturn(mainDispatcherRule.testDispatcher)
        vm = FullInfoFComposeVM(
            dispatcherProvider,
            errorHandling,
            withRetryLastAction,
            composeMapper,
            barcodeRepository,
            docFullComposeMapper
        )
    }

    @After
    fun after() {
        Mockito.clearAllCaches()
    }

    @Test
    fun `test configure body`() = runTest {
        val mockDock = Mockito.mock(DiiaDocument::class.java)

        vm.progressIndicator.test {
            vm.configureBody(mockDock)
            skipItems(1)
            val item = awaitItem()
            Assert.assertEquals(
                UIActionKeysCompose.DOC_CODE_ORG_DATA,
                item.first
            )
            Assert.assertFalse(item.second)
            cancelAndIgnoreRemainingEvents()
        }

        Assert.assertEquals(mockDock, vm.documentCardData.getOrAwaitValue())
    }

    @Test
    fun `test configure body without doc`() = runTest {
        val mockDock = Mockito.mock(Parcelable::class.java)
        vm.configureBody(mockDock)
        Assert.assertThrows(TimeoutException::class.java) { vm.documentCardData.getOrAwaitValue() }
    }

    @Test
    fun `test configure doc code with null data`() = runTest {
        val doc = DocTest()
        val barcode = Mockito.mock(DocumentBarcode::class.java)
        val barcodeResult = DocumentBarcodeSuccessfulLoadResult(
            shareQr = barcode,
            null,
            null,
            0,
            null,
            null
        )
        val mockData = DocCodeOrgData(
            "",
            Localization.eng,
            Mockito.mock(ToggleButtonGroupData::class.java),
            null,
            null,
            null,
            null,
            null,
            false,
            true,
            false
        )
        val barcodeRepoResult =
            DocumentBarcodeRepositoryResult(barcodeResult, false)
        Mockito.`when`(barcodeRepository.loadBarcode(doc, 0, true))
            .thenReturn(barcodeRepoResult)
        Mockito.`when`(
            composeMapper.toComposeDocCodeOrg(
                barcodeResult, LocalizationType.ua,
                false
            )
        ).thenReturn(mockData)
        vm.configureBody(doc)
        advanceUntilIdle()

        verify(composeMapper, times(1)).toComposeDocCodeOrg(
            any(),
            any(),
            any(),
            any()
        )
       val index = vm.bodyData.indexOfFirst { it is DocCodeOrgData }
        val data = vm.bodyData[index] as DocCodeOrgData
        Assert.assertEquals(data, mockData)
    }

    @Test
    fun `test configure doc code`() = runTest {
        val doc = DocTest()
        val barcode = Mockito.mock(DocumentBarcode::class.java)
        val barcodeResult = DocumentBarcodeSuccessfulLoadResult(
            shareQr = barcode,
            null,
            null,
            0,
            null,
            null
        )
        val barcodeRepoResult =
            DocumentBarcodeRepositoryResult(barcodeResult, false)
        Mockito.`when`(barcodeRepository.loadBarcode(doc, 0, true))
            .thenReturn(barcodeRepoResult)
        Mockito.`when`(
            composeMapper.toComposeDocCodeOrg(
                barcodeResult, LocalizationType.ua,
                false
            )
        )
            .thenReturn(null)
        vm.configureBody(doc)
        advanceUntilIdle()

        verify(composeMapper, times(1)).toComposeDocCodeOrg(
            any(),
            any(),
            any(),
            any()
        )
    }

    //actions

    @Test
    fun `test TOGGLE_BUTTON_MOLECULE qr action`() = runTest {
        val doc = DocTest()
        val barcode = Mockito.mock(DocumentBarcode::class.java)
        val barcodeResult = DocumentBarcodeSuccessfulLoadResult(
            shareQr = barcode,
            null,
            null,
            0,
            null,
            null
        )
        val mockData = DocCodeOrgData(
            "",
            Localization.eng,
            ToggleButtonGroupData(BtnToggleMlcData(), BtnToggleMlcData()),
            null,
            null,
            null,
            null,
            null,
            false,
            true,
            false
        )
        val barcodeRepoResult =
            DocumentBarcodeRepositoryResult(barcodeResult, false)
        Mockito.`when`(barcodeRepository.loadBarcode(doc, 0, true))
            .thenReturn(barcodeRepoResult)
        Mockito.`when`(
            composeMapper.toComposeDocCodeOrg(
                barcodeResult, LocalizationType.ua,
                false
            )
        )
            .thenReturn(mockData)
        vm.configureBody(doc)
        advanceUntilIdle()

        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.TOGGLE_BUTTON_MOLECULE,
                    data = ToggleId.qr.value
                )
            )
            Assert.assertEquals(
                awaitItem(),
                FullInfoFComposeVM.DocActions.DefaultBrightness
            )
        }
    }


    @Test
    fun `test TOGGLE_BUTTON_MOLECULE ean action`() = runTest {
        val doc = DocTest()
        val barcode = Mockito.mock(DocumentBarcode::class.java)
        val barcodeResult = DocumentBarcodeSuccessfulLoadResult(
            shareQr = barcode,
            null,
            null,
            0,
            null,
            null
        )
        val mockData = DocCodeOrgData(
            "",
            Localization.eng,
            ToggleButtonGroupData(BtnToggleMlcData(), BtnToggleMlcData()),
            null,
            null,
            null,
            null,
            null,
            false,
            true,
            false
        )
        val barcodeRepoResult =
            DocumentBarcodeRepositoryResult(barcodeResult, false)
        Mockito.`when`(barcodeRepository.loadBarcode(doc, 0, true))
            .thenReturn(barcodeRepoResult)
        Mockito.`when`(
            composeMapper.toComposeDocCodeOrg(
                barcodeResult, LocalizationType.ua,
                false
            )
        )
            .thenReturn(mockData)
        vm.configureBody(doc)
        advanceUntilIdle()

        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.TOGGLE_BUTTON_MOLECULE,
                    data = ToggleId.ean.value
                )
            )
            Assert.assertEquals(
                awaitItem(),
                FullInfoFComposeVM.DocActions.HighBrightness
            )
        }
    }

    @Test
    fun `test TOGGLE_BUTTON_MOLECULE null data action`() = runTest {
        val doc = DocTest()
        val barcode = Mockito.mock(DocumentBarcode::class.java)
        val barcodeResult = DocumentBarcodeSuccessfulLoadResult(
            shareQr = barcode,
            null,
            null,
            0,
            null,
            null
        )
        val mockData = DocCodeOrgData(
            "",
            Localization.eng,
            ToggleButtonGroupData(BtnToggleMlcData(), BtnToggleMlcData()),
            null,
            null,
            null,
            null,
            null,
            false,
            true,
            false
        )
        val barcodeRepoResult =
            DocumentBarcodeRepositoryResult(barcodeResult, false)
        Mockito.`when`(barcodeRepository.loadBarcode(doc, 0))
            .thenReturn(barcodeRepoResult)
        Mockito.`when`(
            composeMapper.toComposeDocCodeOrg(
                barcodeResult, LocalizationType.ua,
                false
            )
        )
            .thenReturn(mockData)
        vm.configureBody(doc)
        advanceUntilIdle()
        var emissionFailed = false
        try {
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.TOGGLE_BUTTON_MOLECULE,
                    data = null
                )
            )
            awaitItem()
        }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test REFRESH_BUTTON action`() = runTest {
        val doc = DocTest()
        val barcode = Mockito.mock(DocumentBarcode::class.java)
        val barcodeResult = DocumentBarcodeSuccessfulLoadResult(
            shareQr = barcode,
            null,
            null,
            0,
            null,
            null
        )
        val mockData = DocCodeOrgData(
            "",
            Localization.eng,
            ToggleButtonGroupData(BtnToggleMlcData(), BtnToggleMlcData()),
            null,
            null,
            null,
            null,
            null,
            false,
            true,
            false
        )
        val barcodeRepoResult =
            DocumentBarcodeRepositoryResult(barcodeResult, false)
        Mockito.`when`(barcodeRepository.loadBarcode(doc, 0, true))
            .thenReturn(barcodeRepoResult)
        Mockito.`when`(
            composeMapper.toComposeDocCodeOrg(
                barcodeResult, LocalizationType.ua,
                false
            )
        )
            .thenReturn(mockData)
        vm.configureBody(doc)
        advanceUntilIdle()

        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.REFRESH_BUTTON
                )
            )
            advanceUntilIdle()
            verify(composeMapper, times(2)).toComposeDocCodeOrg(
                any(),
                any(),
                any(),
                any()
            )
        }
    }

    @Test
    fun `test REFRESH_BUTTON null document card ction`() = runTest {
        val doc = DocTest()
        val barcode = Mockito.mock(DocumentBarcode::class.java)
        val barcodeResult = DocumentBarcodeSuccessfulLoadResult(
            shareQr = barcode,
            null,
            null,
            0,
            null,
            null
        )
        val mockData = DocCodeOrgData(
            "",
            Localization.eng,
            ToggleButtonGroupData(BtnToggleMlcData(), BtnToggleMlcData()),
            null,
            null,
            null,
            null,
            null,
            false,
            true,
            false
        )
        val barcodeRepoResult =
            DocumentBarcodeRepositoryResult(barcodeResult, false)
        Mockito.`when`(barcodeRepository.loadBarcode(doc, 0))
            .thenReturn(barcodeRepoResult)
        Mockito.`when`(
            composeMapper.toComposeDocCodeOrg(
                barcodeResult, LocalizationType.ua,
                false
            )
        )
            .thenReturn(mockData)
        advanceUntilIdle()

        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.REFRESH_BUTTON
                )
            )
            advanceUntilIdle()
            verify(composeMapper, times(0)).toComposeDocCodeOrg(
                any(),
                any(),
                any(),
                any()
            )
        }
    }


    @Test
    fun `test DOC_NUMBER_COPY action`() = runTest {
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.DOC_NUMBER_COPY,
                    data = ""
                )
            )
            Assert.assertEquals(
                awaitItem(),
                FullInfoFComposeVM.DocActions.DocNumberCopy("")
            )
        }
    }

    @Test
    fun `test DOC_NUMBER_COPY null data action`() = runTest {
        var emissionFailed = false
        try {
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.DOC_NUMBER_COPY,
                    data = null
                )
            )
            awaitItem()
        }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test VERTICAL_TABLE_ITEM action`() = runTest {
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.VERTICAL_TABLE_ITEM,
                    data = ""
                )
            )
            Assert.assertEquals(
                awaitItem(),
                FullInfoFComposeVM.DocActions.ItemVerticalValueCopy("")
            )
        }
    }

    @Test
    fun `test VERTICAL_TABLE_ITEM null data action`() = runTest {
        var emissionFailed = false
        try {
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.VERTICAL_TABLE_ITEM,
                    data = null
                )
            )
            awaitItem()
        }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test HORIZONTAL_TABLE_ITEM action`() = runTest {
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.HORIZONTAL_TABLE_ITEM,
                    data = ""
                )
            )
            Assert.assertEquals(
                awaitItem(),
                FullInfoFComposeVM.DocActions.ItemHorizontalValueCopy("")
            )
        }
    }

    @Test
    fun `test HORIZONTAL_TABLE_ITEM null data action`() = runTest {
        var emissionFailed = false
        try {
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.HORIZONTAL_TABLE_ITEM,
                    data = null
                )
            )
            awaitItem()
        }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test PRIMARY_TABLE_ITEM action`() = runTest {
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.PRIMARY_TABLE_ITEM,
                    data = ""
                )
            )
            Assert.assertEquals(
                awaitItem(),
                FullInfoFComposeVM.DocActions.ItemPrimaryValueCopy("")
            )
        }
    }

    @Test
    fun `test PRIMARY_TABLE_ITEM null data action`() = runTest {
        var emissionFailed = false
        try {
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.PRIMARY_TABLE_ITEM,
                    data = null
                )
            )
            awaitItem()
        }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test BOTTOM_SHEET_DISMISS action`() = runTest {
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.BOTTOM_SHEET_DISMISS,
                    data = ""
                )
            )
            Assert.assertEquals(
                awaitItem(),
                FullInfoFComposeVM.DocActions.DismissDoc
            )
        }
    }

}