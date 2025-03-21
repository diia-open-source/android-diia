package ua.gov.diia.documents.ui.stack

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.snapshots.SnapshotStateList
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor
import ua.gov.diia.core.models.share.ShareByteArr
import ua.gov.diia.core.network.connectivity.ConnectivityObserver
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRatingDialogOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.diia_storage.store.datasource.DataSourceDataResult
import ua.gov.diia.documents.DocTest
import ua.gov.diia.core.models.document.barcode.DocumentBarcode
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeRepository
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeRepositoryResult
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeSuccessfulLoadResult
import ua.gov.diia.documents.data.api.ApiDocuments
import ua.gov.diia.documents.data.repository.DocumentsDataRepository
import ua.gov.diia.documents.getOrAwaitValue
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.doc_manual_options.models.DocManualOptions
import ua.gov.diia.core.models.document.LocalizationType
import ua.gov.diia.documents.rules.MainDispatcherRule
import ua.gov.diia.ui_base.mappers.document.DocumentComposeMapper
import ua.gov.diia.ui_base.mappers.document.ToggleId
import ua.gov.diia.documents.ui.WithCheckLocalizationDocs
import ua.gov.diia.documents.ui.WithPdfCertificate
import ua.gov.diia.documents.ui.WithRemoveDocument
import ua.gov.diia.documents.ui.gallery.DocActions
import ua.gov.diia.documents.ui.gallery.DocFSettings
import ua.gov.diia.documents.util.WithUpdateExpiredDocs
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.button.BtnToggleMlcData
import ua.gov.diia.ui_base.components.organism.document.DocCodeOrgData
import ua.gov.diia.ui_base.components.organism.document.DocOrgData
import ua.gov.diia.ui_base.components.organism.document.DocPhotoOrgData
import ua.gov.diia.ui_base.components.organism.document.Localization
import ua.gov.diia.ui_base.components.organism.group.ToggleButtonGroupData
import ua.gov.diia.ui_base.components.organism.pager.CardFace
import ua.gov.diia.ui_base.components.organism.pager.DocCardFlipData
import ua.gov.diia.ui_base.components.organism.pager.DocCarouselOrgData
import ua.gov.diia.ui_base.components.organism.pager.DocsCarouselItem
import kotlin.time.Duration.Companion.seconds

@ExperimentalCoroutinesApi
class DocStackVMComposeTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val apiDocs: ApiDocuments = mock()
    private lateinit var globalActionConfirmDocumentRemoval: MutableStateFlow<UiDataEvent<String>?>
    private lateinit var globalActionUpdateDocument: MutableStateFlow<UiDataEvent<DiiaDocument>?>
    private lateinit var globalActionFocusOnDocument: MutableStateFlow<UiDataEvent<String>?>
    private lateinit var globalActionSelectedMenuItem: MutableStateFlow<UiDataEvent<HomeMenuItemConstructor>?>
    private val connectivityObserver: ConnectivityObserver = mock()
    private val barcodeRepository: DocumentBarcodeRepository = mock()
    private val documentsDataSource: DocumentsDataRepository = mock()
    private val dispatcherProvider: DispatcherProvider = mock()
    private val errorHandling: WithErrorHandlingOnFlow = mock()
    private val withRetryLastAction: WithRetryLastAction = mock()
    private val withRatingDialog: WithRatingDialogOnFlow = mock()
    private val composeMapper: DocumentComposeMapper = mock()
    private val withUpdateExpiredDocs: WithUpdateExpiredDocs = mock()
    private val withPdfCertificate: WithPdfCertificate = mock()
    private val withCheckLocalizationDocs: WithCheckLocalizationDocs = mock()
    private val withRemoveDocument: WithRemoveDocument = mock()
    private val documentsHelper: DocumentsHelper = mock()

    private val mockDoc =
        DiiaDocumentWithMetadata(ua.gov.diia.doc_manual_options.models.DocManualOptions(), "", "", 200, "doc_manual_options")
    private val mockDoc2 =
        DiiaDocumentWithMetadata(DocTest(), "", "", 200, "doc_manual_options")

    private val documentsFlow: Flow<DataSourceDataResult<List<DiiaDocumentWithMetadata>>> =
        flowOf(DataSourceDataResult.successful(listOf(mockDoc2, mockDoc)))

    private val documentsFlow2: Flow<DataSourceDataResult<List<DiiaDocumentWithMetadata>>> =
        flowOf(DataSourceDataResult.successful(listOf(mockDoc, mockDoc2)))
    private lateinit var vm: DocStackVMCompose

    private fun initVM() {
        vm = DocStackVMCompose(
            apiDocs,
            globalActionConfirmDocumentRemoval,
            globalActionUpdateDocument,
            globalActionFocusOnDocument,
            globalActionSelectedMenuItem,
            barcodeRepository,
            documentsDataSource,
            dispatcherProvider,
            errorHandling,
            withRetryLastAction,
            withRatingDialog,
            composeMapper,
            withUpdateExpiredDocs,
            withPdfCertificate,
            withCheckLocalizationDocs,
            withRemoveDocument,
            documentsHelper
        )
    }

    @Before
    fun before() {
        globalActionConfirmDocumentRemoval = MutableStateFlow(null)
        globalActionUpdateDocument = MutableStateFlow(null)
        globalActionFocusOnDocument = MutableStateFlow(null)
        globalActionSelectedMenuItem = MutableStateFlow(null)
        val mockUiElement: DocCarouselOrgData = DocCarouselOrgData(
            data = SnapshotStateList<DocsCarouselItem>().apply {
                add(
                    DocCardFlipData(
                        UIActionKeysCompose.DOC_ORG_DATA,
                        "",
                        "test_doc",
                        0,
                        CardFace.Front,
                        DocPhotoOrgData(),
                        Mockito.mock(DocCodeOrgData::class.java),
                        true,
                    )
                )
            },
            focusOnDoc = 0
        )
        Mockito.`when`(connectivityObserver.observe()).thenReturn(flowOf())
        Mockito.`when`(dispatcherProvider.ioDispatcher())
            .thenReturn(StandardTestDispatcher())
        Mockito.`when`(documentsDataSource.data).thenReturn(documentsFlow)
        Mockito.`when`(composeMapper.toDocCarousel(any(), anyOrNull()))
            .thenReturn(mockUiElement)
    }
    @After
    fun after() {
        Mockito.clearAllCaches()
    }

    @Test
    fun `test do init`() = runTest {
        initVM()
        advanceUntilIdle()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        assert(vm.documentCardData.getOrAwaitValue().isNotEmpty())
    }

    @Test
    fun `test current doc id`() = runTest {
        initVM()
        vm.showRating(DocTest())
        Assert.assertEquals("123", vm.currentDocId())
    }

    @Test
    fun `test current doc id null`() = runTest {
        initVM()
        Assert.assertEquals(null, vm.currentDocId())
    }

    @Test
    fun `test configure body`() = runTest {
        val docsFlow = MutableStateFlow(
            DataSourceDataResult.successful(
                listOf(
                    mockDoc2,
                    mockDoc
                )
            )
        )
        Mockito.`when`(documentsDataSource.data).thenReturn(docsFlow)
        initVM()
        advanceUntilIdle()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        docsFlow.emit(
            DataSourceDataResult.successful(
                listOf(
                    mockDoc,
                    mockDoc2
                )
            )
        )
        advanceUntilIdle()
        verify(composeMapper, times(2)).toDocCarousel(any(), any())
    }

    @Test
    fun `test remove doc`() = runTest {
        initVM()
        val mock = Mockito.mock(DiiaDocument::class.java)
        vm.removeDoc(mock)
        advanceUntilIdle()
        verify(withRemoveDocument, times(1)).removeDocument(any(), any())
    }

    @Test
    fun `test invalidate datasource`() = runTest {
        initVM()
        vm.invalidateDataSource()
        verify(documentsDataSource, times(1)).invalidate()
    }

    @Test
    fun `test getCertificatePdf`() = runTest {
        initVM()
        vm.getCertificatePdf(DocTest())
        advanceUntilIdle()
        verify(withPdfCertificate, times(1)).loadCertificatePdf(any())
    }

    @Test
    fun `test addAwardToGallery`() = runTest {
        initVM()
        vm.addDocToGallery()
        advanceUntilIdle()
        verify(documentsHelper, times(1)).addDocToGallery()
    }

    @Test
    fun `test loadImageAndShare`() = runTest {
        initVM()
        val bytes = ShareByteArr(fileName = "", byteArray = ByteArray(0))
        Mockito.`when`(documentsHelper.loadDocImageInByteArray(any())).thenReturn(bytes)
        vm.docAction.test {
            vm.loadImageAndShare("123")
            advanceUntilIdle()
            verify(documentsHelper, times(1)).loadDocImageInByteArray(any())
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.DocActions.ShareImage(bytes)
            )
        }
    }

    @Test
    fun `test remove military bond`() = runTest {
        initVM()
        vm.removeMilitaryBondFromGallery("", "")
        advanceUntilIdle()
        verify(
            withRemoveDocument,
            times(1)
        ).removeMilitaryBondFromGallery(any(), any(), any())
    }

    @Test
    fun `test confirm delete doc`() = runTest {
        initVM()
        vm.showConfirmDeleteTemplateLocal("")
        advanceUntilIdle()
        verify(withRemoveDocument, times(1)).confirmRemoveDocument(
            any(),
            any(),
            any(),
            any()
        )
    }

    // actions

    @Test
    fun `test TOGGLE_BUTTON_MOLECULE qr action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.TOGGLE_BUTTON_MOLECULE,
                    optionalId = "0",
                    data = ToggleId.qr.value
                )
            )
            advanceUntilIdle()
            Assert.assertEquals(
                DocStackVMCompose.DocActions.DefaultBrightness,
                awaitItem()
            )
        }
    }

    @Test
    fun `test TOGGLE_BUTTON_MOLECULE qr wrong optionalId action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.TOGGLE_BUTTON_MOLECULE,
                    optionalId = "",
                    data = ToggleId.qr.value
                )
            )
            advanceUntilIdle()
            Assert.assertEquals(
                DocStackVMCompose.DocActions.DefaultBrightness,
                awaitItem()
            )
        }
    }

    @Test
    fun `test TOGGLE_BUTTON_MOLECULE null data action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.docAction.test {
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.TOGGLE_BUTTON_MOLECULE,
                        optionalId = "0",
                        data = null
                    )
                )
                advanceUntilIdle()
                awaitItem()
            }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test TOGGLE_BUTTON_MOLECULE qr null OptionalId action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.docAction.test {
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.TOGGLE_BUTTON_MOLECULE,
                        optionalId = null,
                        data = ToggleId.qr.value
                    )
                )
                advanceUntilIdle()
                awaitItem()
            }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test TOGGLE_BUTTON_MOLECULE ean action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.TOGGLE_BUTTON_MOLECULE,
                    optionalId = "0",
                    data = ToggleId.ean.value
                )
            )
            advanceUntilIdle()
            Assert.assertEquals(
                DocStackVMCompose.DocActions.HighBrightness,
                awaitItem()
            )
        }
    }

    @Test
    fun `test REFRESH_BUTTON action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.docAction.test {
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
            Mockito.`when`(barcodeRepository.loadBarcode(doc, 0, false))
                .thenReturn(barcodeRepoResult)
            Mockito.`when`(
                composeMapper.toComposeDocCodeOrg(
                    barcodeResult, LocalizationType.ua,
                    false
                )
            )
                .thenReturn(mockData)
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.REFRESH_BUTTON,
                    optionalId = "0",
                    data = "doc_test"
                )
            )
            advanceUntilIdle()
            verify(barcodeRepository, times(1)).loadBarcode(any(), any(), any())
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.DocActions.DefaultBrightness
            )
        }
    }

    @Test
    fun `test REFRESH_BUTTON wrong optionalId action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.docAction.test {
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
            Mockito.`when`(barcodeRepository.loadBarcode(doc, 0, false))
                .thenReturn(barcodeRepoResult)
            Mockito.`when`(
                composeMapper.toComposeDocCodeOrg(
                    barcodeResult, LocalizationType.ua,
                    false
                )
            )
                .thenReturn(mockData)
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.REFRESH_BUTTON,
                    optionalId = "",
                    data = "doc_test"
                )
            )
            advanceUntilIdle()
            verify(barcodeRepository, times(1)).loadBarcode(any(), any(), any())
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.DocActions.DefaultBrightness
            )
        }
    }

    @Test
    fun `test REFRESH_BUTTON data null action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.docAction.test {
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
                    ToggleButtonGroupData(
                        BtnToggleMlcData(),
                        BtnToggleMlcData()
                    ),
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
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.REFRESH_BUTTON,
                        optionalId = "0",
                        data = null
                    )
                )
                advanceUntilIdle()
                awaitItem()
            }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test REFRESH_BUTTON optionalId null action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.docAction.test {
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
                    ToggleButtonGroupData(
                        BtnToggleMlcData(),
                        BtnToggleMlcData()
                    ),
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
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.REFRESH_BUTTON,
                        optionalId = null,
                        data = "doc_test"
                    )
                )
                advanceUntilIdle()
                awaitItem()
            }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test REFRESH_BUTTON doc null action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.docAction.test {
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
                    ToggleButtonGroupData(
                        BtnToggleMlcData(),
                        BtnToggleMlcData()
                    ),
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
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.REFRESH_BUTTON,
                        optionalId = "0",
                        data = "wrong_doc"
                    )
                )
                advanceUntilIdle()
                awaitItem()
            }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test DOC_CARD_FLIP action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.docAction.test {
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
            Mockito.`when`(barcodeRepository.loadBarcode(doc, 0, false))
                .thenReturn(barcodeRepoResult)
            Mockito.`when`(
                composeMapper.toComposeDocCodeOrg(
                    barcodeResult, LocalizationType.ua,
                    false
                )
            )
                .thenReturn(mockData)
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.DOC_CARD_FLIP,
                    optionalId = "0",
                    data = "doc_test"
                )
            )
            advanceUntilIdle()
            verify(barcodeRepository, times(1)).loadBarcode(any(), any(), any())
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.DocActions.DefaultBrightness
            )
        }
    }
    @Test
    fun `test DOC_CARD_FLIP wrong optionalId action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.docAction.test {
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
            Mockito.`when`(barcodeRepository.loadBarcode(doc, 0, false))
                .thenReturn(barcodeRepoResult)
            Mockito.`when`(
                composeMapper.toComposeDocCodeOrg(
                    barcodeResult, LocalizationType.ua,
                    false
                )
            )
                .thenReturn(mockData)
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.DOC_CARD_FLIP,
                    optionalId = "",
                    data = "doc_test"
                )
            )
            advanceUntilIdle()
            verify(barcodeRepository, times(1)).loadBarcode(any(), any(), any())
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.DocActions.DefaultBrightness
            )
        }
    }
    @Test
    fun `test DOC_CARD_FLIP null data action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.docAction.test {
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
                    ToggleButtonGroupData(
                        BtnToggleMlcData(),
                        BtnToggleMlcData()
                    ),
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
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.DOC_CARD_FLIP,
                        optionalId = "0",
                        data = null
                    )
                )
                advanceUntilIdle()
                awaitItem()
            }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test DOC_CARD_FLIP null optionalId action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.docAction.test {
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
                    ToggleButtonGroupData(
                        BtnToggleMlcData(),
                        BtnToggleMlcData()
                    ),
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
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.DOC_CARD_FLIP,
                        optionalId = null,
                        data = "doc_test"
                    )
                )
                advanceUntilIdle()
                awaitItem()
            }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test DOC_CARD_FLIP wrong doc action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.docAction.test {
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
                    ToggleButtonGroupData(
                        BtnToggleMlcData(),
                        BtnToggleMlcData()
                    ),
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
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.DOC_CARD_FLIP,
                        optionalId = "0",
                        data = "wrong_doc_test"
                    )
                )
                advanceUntilIdle()
                awaitItem()
            }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test DOC_CARD_FLIP action without CardFlipData`() = runTest {
        val mockUiElement = DocCarouselOrgData(
            data = SnapshotStateList<DocsCarouselItem>().apply {
                add(
                    DocOrgData(
                        "",
                        UIActionKeysCompose.DOC_ORG_DATA,
                        "",
                        null,
                        null,
                        null,
                        "test_doc",
                        0,
                        true,
                        null,
                        0
                    )
                )
            },
            focusOnDoc = 0
        )
        Mockito.`when`(composeMapper.toDocCarousel(any(), anyOrNull()))
            .thenReturn(mockUiElement)
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.docAction.test {
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
            Mockito.`when`(barcodeRepository.loadBarcode(doc, 0, false))
                .thenReturn(barcodeRepoResult)
            Mockito.`when`(
                composeMapper.toComposeDocCodeOrg(
                    barcodeResult, LocalizationType.ua,
                    false
                )
            )
                .thenReturn(mockData)
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.DOC_CARD_FLIP,
                    optionalId = "0",
                    data = "doc_test"
                )
            )
            advanceUntilIdle()
            verify(barcodeRepository, times(1)).loadBarcode(any(), any(), any())
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.DocActions.DefaultBrightness
            )
        }
    }

    @Test
    fun `test DOC_CARD_FORCE_FLIP qr action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.docAction.test(timeout = 8.seconds) {
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
            Mockito.`when`(barcodeRepository.loadBarcode(doc, 0, false))
                .thenReturn(barcodeRepoResult)
            Mockito.`when`(
                composeMapper.toComposeDocCodeOrg(
                    barcodeResult, LocalizationType.ua,
                    true
                )
            )
                .thenReturn(mockData)
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.DOC_CARD_FORCE_FLIP,
                    optionalId = "0",
                    optionalType = ToggleId.qr.value,
                    data = "doc_test"
                )
            )
            advanceUntilIdle()

            verify(barcodeRepository, times(1)).loadBarcode(any(), any(), any())
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.DocActions.DefaultBrightness
            )
        }
    }

    @Test
    fun `test DOC_CARD_FORCE_FLIP ean action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.docAction.test(timeout = 8.seconds) {
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
            Mockito.`when`(barcodeRepository.loadBarcode(doc, 0, false))
                .thenReturn(barcodeRepoResult)
            Mockito.`when`(
                composeMapper.toComposeDocCodeOrg(
                    barcodeResult, LocalizationType.ua,
                    true
                )
            )
                .thenReturn(mockData)
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.DOC_CARD_FORCE_FLIP,
                    optionalId = "0",
                    optionalType = ToggleId.ean.value,
                    data = "doc_test"
                )
            )
            advanceUntilIdle()

            verify(barcodeRepository, times(1)).loadBarcode(any(), any(), any())
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.DocActions.HighBrightness
            )
        }
    }

    @Test
    fun `test DOC_CARD_FORCE_FLIP wrong optionalId action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.docAction.test(timeout = 8.seconds) {
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
            Mockito.`when`(barcodeRepository.loadBarcode(doc, 0, false))
                .thenReturn(barcodeRepoResult)
            Mockito.`when`(
                composeMapper.toComposeDocCodeOrg(
                    barcodeResult, LocalizationType.ua,
                    true
                )
            )
                .thenReturn(mockData)
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.DOC_CARD_FORCE_FLIP,
                    optionalId = "",
                    optionalType = ToggleId.qr.value,
                    data = "doc_test"
                )
            )
            advanceUntilIdle()

            verify(barcodeRepository, times(1)).loadBarcode(any(), any(), any())
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.DocActions.DefaultBrightness
            )
        }
    }


    @Test
    fun `test DOC_CARD_FORCE_FLIP action without CardFlipData`() = runTest {
        val mockUiElement = DocCarouselOrgData(
            data = SnapshotStateList<DocsCarouselItem>().apply {
                add(
                    DocOrgData(
                        "",
                        UIActionKeysCompose.DOC_ORG_DATA,
                        "",
                        null,
                        null,
                        null,
                        "test_doc",
                        0,
                        true,
                        null,
                        0
                    )
                )
            },
            focusOnDoc = 0
        )
        Mockito.`when`(composeMapper.toDocCarousel(any(), anyOrNull()))
            .thenReturn(mockUiElement)
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.docAction.test(timeout = 8.seconds) {
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
            Mockito.`when`(barcodeRepository.loadBarcode(doc, 0, false))
                .thenReturn(barcodeRepoResult)
            Mockito.`when`(
                composeMapper.toComposeDocCodeOrg(
                    barcodeResult, LocalizationType.ua,
                    true
                )
            )
                .thenReturn(mockData)
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.DOC_CARD_FORCE_FLIP,
                    optionalId = "0",
                    optionalType = ToggleId.qr.value,
                    data = "doc_test"
                )
            )
            advanceUntilIdle()

            verify(barcodeRepository, times(1)).loadBarcode(any(), any(), any())
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.DocActions.DefaultBrightness
            )
        }
        this@runTest.coroutineContext.cancelChildren()
    }

    @Test
    fun `test DOC_CARD_FORCE_FLIP null data action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.docAction.test(timeout = 8.seconds) {
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
                        true
                    )
                )
                    .thenReturn(mockData)
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.DOC_CARD_FORCE_FLIP,
                        optionalId = "0",
                        optionalType = ToggleId.qr.value,
                        data = null
                    )
                )
                advanceUntilIdle()
                awaitItem()
            }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test DOC_CARD_FORCE_FLIP null optionalId action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.docAction.test(timeout = 8.seconds) {
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
                        true
                    )
                )
                    .thenReturn(mockData)
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.DOC_CARD_FORCE_FLIP,
                        optionalId = null,
                        optionalType = ToggleId.qr.value,
                        data = "doc_test"
                    )
                )
                advanceUntilIdle()
                awaitItem()
            }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }
    @Test
    fun `test DOC_CARD_FORCE_FLIP wrong doc action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.docAction.test(timeout = 8.seconds) {
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
                        true
                    )
                )
                    .thenReturn(mockData)
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.DOC_CARD_FORCE_FLIP,
                        optionalId = "0",
                        optionalType = ToggleId.qr.value,
                        data = "wrong_doc_test"
                    )
                )
                advanceUntilIdle()
                awaitItem()
            }
        } catch (e: Throwable) {
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test DOC_ELLIPSE_MENU action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.navigation.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.DOC_ELLIPSE_MENU,
                    optionalId = "0",
                    data = "doc_test"
                )
            )
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.Navigation.ToDocActions(
                    DocTest(),
                    0
                )
            )
        }
    }

    @Test
    fun `test DOC_ELLIPSE_MENU wrong optionalId action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.navigation.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.DOC_ELLIPSE_MENU,
                    optionalId = "",
                    data = "doc_test"
                )
            )
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.Navigation.ToDocActions(
                    DocTest(),
                    0
                )
            )
        }
    }

    @Test
    fun `test DOC_ELLIPSE_MENU data null action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.navigation.test {
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.DOC_ELLIPSE_MENU,
                        optionalId = "0",
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
    fun `test DOC_ELLIPSE_MENU optionalId null action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.navigation.test {
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.DOC_ELLIPSE_MENU,
                        optionalId = null,
                        data = "doc_test"
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
    fun `test DOC_ELLIPSE_MENU doc null action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.navigation.test {
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.DOC_ELLIPSE_MENU,
                        optionalId = "0",
                        data = "wrong_doc"
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
    fun `test TICKER_ATOM_CLICK action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        vm.navigation.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.TICKER_ATOM_CLICK,
                    optionalId = "0",
                    data = "doc_test"
                )
            )
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.Navigation.NavToVehicleInsurance(
                    DocTest()
                )
            )
        }
    }

    @Test
    fun `test TICKER_ATOM_CLICK data null action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.navigation.test {
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.TICKER_ATOM_CLICK,
                        optionalId = "0",
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
    fun `test TICKER_ATOM_CLICK optionalId null action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.navigation.test {
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.TICKER_ATOM_CLICK,
                        optionalId = null,
                        data = "doc_test"
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
    fun `test TICKER_ATOM_CLICK doc null action`() = runTest {
        initVM()
        val settings = DocFSettings()
        vm.subscribeForDocuments(settings)
        advanceUntilIdle()
        var emissionFailed = false
        try {
            vm.navigation.test {
                vm.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.TICKER_ATOM_CLICK,
                        optionalId = "0",
                        data = "wrong doc"
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
    fun `test DOC_CARD_SWIPE_FINISHED action`() = runTest {
        initVM()
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.DOC_CARD_SWIPE_FINISHED,
                )
            )
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.DocActions.DefaultBrightness
            )
        }
    }

    @Test
    fun `test DOC_ACTION_IN_LINE action`() = runTest {
        initVM()
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = DocActions.DOC_ACTION_IN_LINE,
                )
            )
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.DocActions.OpenElectronicQueue
            )
        }
    }

    @Test
    fun `test DOC_ACTION_TO_DRIVER_ACCOUNT action`() = runTest {
        initVM()
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = DocActions.DOC_ACTION_TO_DRIVER_ACCOUNT,
                )
            )
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.DocActions.OpenDriverAccount
            )
        }
    }

    @Test
    fun `test DOC_NUMBER_COPY  action`() = runTest {
        initVM()
        vm.docAction.test {
            vm.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.DOC_NUMBER_COPY,
                    data = ""
                )
            )
            Assert.assertEquals(
                awaitItem(),
                DocStackVMCompose.DocActions.DocNumberCopy("")
            )
        }
    }

    @Test
    fun `test DOC_NUMBER_COPY null data action`() = runTest {
        initVM()
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

}