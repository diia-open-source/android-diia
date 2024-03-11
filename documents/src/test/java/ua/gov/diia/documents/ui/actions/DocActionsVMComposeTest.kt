package ua.gov.diia.documents.ui.actions

import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.LocalizationType
import ua.gov.diia.documents.rules.MainDispatcherRule
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose

class DocActionsVMComposeTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var vm: DocActionsVMCompose
    private var globalActionUpdateDocument: MutableStateFlow<UiDataEvent<DiiaDocument>?> = MutableStateFlow(null)

    @Mock
    lateinit var docActionsProvider: DocActionsProvider

    @Before
    fun beforeTest(){
        globalActionUpdateDocument = MutableStateFlow(null)
        docActionsProvider = mock()
        vm = DocActionsVMCompose(globalActionUpdateDocument, docActionsProvider, listOf())
    }

    @Test
    fun `test action using DataActionWrapper`() = runTest{
        vm.docAction.test{
            vm.onUIAction(UIAction(action = DataActionWrapper(type = ContextMenuType.REMOVE_DOC.name), actionKey = ""))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.DocActions.RemoveDoc)
        }
    }


    // doc actions
    @Test
    fun `test remove document action`() = runTest{
        vm.docAction.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.REMOVE_DOC.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.DocActions.RemoveDoc)
        }
    }

    @Test
    fun `test TRANSLATE_TO_UA action`() = runTest{
        vm.docAction.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.TRANSLATE_TO_UA.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.DocActions.TranslateToUa)
        }
    }

    @Test
    fun `test TRANSLATE_TO_ENG action`() = runTest{
        vm.docAction.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.TRANSLATE_TO_ENG.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.DocActions.TranslateToEng)
        }
    }

    @Test
    fun `test RATE_DOCUMENT action`() = runTest{
        vm.docAction.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.RATE_DOCUMENT.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.DocActions.RateDocument)
        }
    }

    @Test
    fun `test SHARE_WITH_FRIENDS action`() = runTest{
        vm.docAction.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.SHARE_WITH_FRIENDS.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.DocActions.ShareWithFriends)
        }
    }

    @Test
    fun `test VERIFICATION_CODE action`() = runTest{
        vm.docAction.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.VERIFICATION_CODE.name, data = ""))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.DocActions.OpenVerificationCode(""))
        }
    }

    @Test
    fun `test VERIFICATION_CODE_QR action`() = runTest{
        vm.docAction.test{
            vm.onUIAction(UIAction(actionKey =  VerificationActions.VERIFICATION_CODE_QR, data = ""))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.DocActions.OpenQr(""))
        }
    }

    @Test
    fun `test VERIFICATION_CODE_QR null action`() = runTest{
        var emissionFailed = false
        try {
            vm.docAction.test{
                vm.onUIAction(UIAction(actionKey =  VerificationActions.VERIFICATION_CODE_QR, data = null))
                awaitError()
            }
        } catch (e: Throwable){
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }

    @Test
    fun `test VERIFICATION_CODE_EAN13 action`() = runTest{
        vm.docAction.test{
            vm.onUIAction(UIAction(actionKey =  VerificationActions.VERIFICATION_CODE_EAN13, data = ""))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.DocActions.OpenEan13(""))
        }
    }

    @Test
    fun `test VERIFICATION_CODE_EAN13 null action`() = runTest{
        var emissionFailed = false
        try {
            vm.docAction.test{
                vm.onUIAction(UIAction(actionKey =  VerificationActions.VERIFICATION_CODE_EAN13, data = null))
                awaitError()
            }
        } catch (e: Throwable){
            emissionFailed = true
        }
        Assert.assertTrue(emissionFailed)
    }
    // navigation actions
    @Test
    fun `test nav to faq action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.FAQS.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.NavToFaqs)
        }
    }

    @Test
    fun `test nav to PNP action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.PNP.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.ToNavPnp)
        }
    }

    @Test
    fun `test nav to Drl action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.REPLACE_DRIVER_LICENSE.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.NavToDrl)
        }
    }

    @Test
    fun `test nav to INSURANCE action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.INSURANCE.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.NavToVehicleInsurance)
        }
    }

    @Test
    fun `test nav to RESIDENCE_CERT action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.RESIDENCE_CERT.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.NavToResidenceCert)
        }
    }

    @Test
    fun `test nav to RESIDENCE_CERT_CHILD action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.RESIDENCE_CERT_CHILD.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.NavToResidenceCertChild)
        }
    }

    @Test
    fun `test nav to PENSION_CARD action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.PENSION_CARD.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.NavToPensionCard)
        }
    }

    @Test
    fun `test nav to PENSION_CARD 2 action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.PENSION_CARD.code))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.PensionCard)
        }
    }

    @Test
    fun `test nav to FULL_DOC action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.FULL_DOC.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.NavToFullInfo)
        }
    }
    @Test
    fun `test nav to HOUSING_CERTIFICATES action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.HOUSING_CERTIFICATES.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.NavToHousingCert)
        }
    }

    @Test
    fun `test nav to FOUNDING_REQUEST action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.FOUNDING_REQUEST.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.NavToFoundingRequest)
        }
    }

    @Test
    fun `test nav to CHANGE_DOC_ORDERING action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.CHANGE_DOC_ORDERING.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.ToDocStackOrder)
        }
    }

    @Test
    fun `test nav to CHANGE_DISPLAY_ORDER action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.CHANGE_DISPLAY_ORDER.name, data = ""))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.ToDocStackOrderWithType(""))
        }
    }

    @Test
    fun `test nav to VEHICLE_RE_REGISTRATION action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.VEHICLE_RE_REGISTRATION.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.VehicleReRegistration)
        }
    }

    @Test
    fun `test nav to BIRTH_CERTIFICATE action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.BIRTH_CERTIFICATE.code))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.BirthCertificate)
        }
    }

    @Test
    fun `test nav to VACCINATION_CERTIFICATE action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.VACCINATION_CERTIFICATE.code))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.VaccinationCertificate)
        }
    }

    @Test
    fun `test nav to CHILD_VACCINATION_CERTIFICATE action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.CHILD_VACCINATION_CERTIFICATE.code))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.ChildVaccinationCertificate)
        }
    }

    @Test
    fun `test nav to PROPER_USER_SHARE action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.PROPER_USER_SHARE.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.ProperUserShare)
        }
    }

    @Test
    fun `test nav to PROPER_USER_OWNER_CANCEL action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.PROPER_USER_OWNER_CANCEL.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.ProperUserCancel)
        }
    }

    @Test
    fun `test nav to PROPER_USER_PROPER_CANCEL action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.PROPER_USER_PROPER_CANCEL.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.ProperUserCancel)
        }
    }

    @Test
    fun `test nav to INTERNALLY_DISPLACED_CERT_CANCEL action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.INTERNALLY_DISPLACED_CERT_CANCEL.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.InternallyDisplacedCertCancel)
        }
    }

    @Test
    fun `test nav to EDIT_INTERNALLY_DISPLACED_PERSON_ADDRESS action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.EDIT_INTERNALLY_DISPLACED_PERSON_ADDRESS.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.EditInternallyDisplacedPersonAddress)
        }
    }

    @Test
    fun `test nav to RESIDENCE_PERMIT_PERMANENT action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.RESIDENCE_PERMIT_PERMANENT.code))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.ResidencePermitPermanent)
        }
    }

    @Test
    fun `test nav to RESIDENCE_PERMIT_TEMPORARY action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.RESIDENCE_PERMIT_TEMPORARY.code))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.ResidencePermitTemporary)
        }
    }

    @Test
    fun `test nav to DOWNLOAD_CERTIFICATE_PDF action`() = runTest{
        vm.navigation.test{
            vm.onUIAction(UIAction(actionKey = ContextMenuType.DOWNLOAD_CERTIFICATE_PDF.name))
            Assert.assertEquals(awaitItem(), DocActionsVMCompose.Navigation.DownloadPdf)
        }
    }

    // Dismiss

    @Test
    fun `test dismiss action`() = runTest{
        vm.dismiss.test{
            vm.onUIAction(UIAction(actionKey = UIActionKeysCompose.BUTTON_REGULAR))
            Assert.assertNotNull(awaitItem())
        }
    }

    //Localization

    @Test
    fun `test switch localization`() = runTest{
        globalActionUpdateDocument.filterNotNull().test{
            val loc = LocalizationType.eng
            val mockDoc = Mockito.mock(DiiaDocument::class.java)
            val mockDocCopy = Mockito.mock(DiiaDocument::class.java)
            Mockito.`when`(mockDoc.makeCopy()).thenReturn(mockDocCopy)
            vm.switchLocalization(mockDoc, loc)
            Assert.assertEquals(mockDocCopy, awaitItem().peekContent())
        }
    }
}