package ua.gov.diia.documents.ui.actions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.documents.di.GlobalActionUpdateDocument
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.LocalizationType
import ua.gov.diia.documents.ui.actions.VerificationActions.VERIFICATION_CODE_EAN13
import ua.gov.diia.documents.ui.actions.VerificationActions.VERIFICATION_CODE_QR
import ua.gov.diia.documents.util.BaseDocActionItemProcessor
import ua.gov.diia.ui_base.components.infrastructure.event.DocAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import javax.inject.Inject

@HiltViewModel
class DocActionsVMCompose @Inject constructor(
    @GlobalActionUpdateDocument val globalActionUpdateDocument: MutableStateFlow<UiDataEvent<DiiaDocument>?>,
    private val docActionsProvider: DocActionsProvider,
    private val actionItemProcessorList: List<@JvmSuppressWildcards BaseDocActionItemProcessor>
) : ViewModel(),
    DocActionsProvider by docActionsProvider {

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

    private val _dismiss = MutableSharedFlow<UiEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val dismiss = _dismiss.asSharedFlow()

    fun switchLocalization(diiaDocument: DiiaDocument, code: LocalizationType) {
        viewModelScope.launch {
            val doc = diiaDocument.makeCopy()
            doc.setLocalization(code)
            globalActionUpdateDocument.emit(UiDataEvent(doc))
        }
    }

    fun onUIAction(event: UIAction) {
        val eventName = event.action?.type ?: event.actionKey
        for (itemProcessor in actionItemProcessorList) {
            itemProcessor.processEvent(eventName, _docAction, _dismiss, _navigation)
        }

        when (eventName) {
            ContextMenuType.FAQS.name -> {
                _navigation.tryEmit(Navigation.NavToFaqs)
            }

            ContextMenuType.PNP.name -> {
                _navigation.tryEmit(Navigation.ToNavPnp)
            }

            ContextMenuType.REPLACE_DRIVER_LICENSE.name -> {
                _navigation.tryEmit(Navigation.NavToDrl)
            }

            ContextMenuType.INSURANCE.name -> {
                _navigation.tryEmit(Navigation.NavToVehicleInsurance)
            }

            ContextMenuType.RESIDENCE_CERT.name -> {
                _navigation.tryEmit(Navigation.NavToResidenceCert)
            }

            ContextMenuType.RESIDENCE_CERT_CHILD.name -> {
                _navigation.tryEmit(Navigation.NavToResidenceCertChild)
            }

            ContextMenuType.PENSION_CARD.name -> {
                _navigation.tryEmit(Navigation.NavToPensionCard)
            }

            ContextMenuType.FULL_DOC.name -> {
                _navigation.tryEmit(Navigation.NavToFullInfo)
            }

            ContextMenuType.HOUSING_CERTIFICATES.name -> {
                _navigation.tryEmit(Navigation.NavToHousingCert)
            }

            ContextMenuType.FOUNDING_REQUEST.name -> {
                _navigation.tryEmit(Navigation.NavToFoundingRequest)
            }

            ContextMenuType.CHANGE_DOC_ORDERING.name -> {
                _navigation.tryEmit(Navigation.ToDocStackOrder)
            }

            ContextMenuType.CHANGE_DISPLAY_ORDER.name -> {
                _navigation.tryEmit(Navigation.ToDocStackOrderWithType(event.data!!))
            }

            ContextMenuType.REMOVE_DOC.name -> {
                _docAction.tryEmit(DocActions.RemoveDoc)
            }

            ContextMenuType.TRANSLATE_TO_UA.name -> {
                _docAction.tryEmit(DocActions.TranslateToUa)
            }

            ContextMenuType.TRANSLATE_TO_ENG.name -> {
                _docAction.tryEmit(DocActions.TranslateToEng)
            }

            ContextMenuType.RATE_DOCUMENT.name -> {
                _docAction.tryEmit(DocActions.RateDocument)
            }

            ContextMenuType.SHARE_WITH_FRIENDS.name -> {
                _docAction.tryEmit(DocActions.ShareWithFriends)
            }

            ContextMenuType.VERIFICATION_CODE.name -> {
                _docAction.tryEmit(DocActions.OpenVerificationCode(event.data!!))
            }

            ContextMenuType.VEHICLE_RE_REGISTRATION.name -> {
                _navigation.tryEmit(Navigation.VehicleReRegistration)
            }

            ContextMenuType.BIRTH_CERTIFICATE.code -> {
                _navigation.tryEmit(Navigation.BirthCertificate)
            }

            ContextMenuType.VACCINATION_CERTIFICATE.code -> {
                _navigation.tryEmit(Navigation.VaccinationCertificate)
            }

            ContextMenuType.CHILD_VACCINATION_CERTIFICATE.code -> {
                _navigation.tryEmit(Navigation.ChildVaccinationCertificate)
            }

            ContextMenuType.PROPER_USER_SHARE.name -> {
                _navigation.tryEmit(Navigation.ProperUserShare)
            }

            ContextMenuType.PROPER_USER_OWNER_CANCEL.name,
            ContextMenuType.PROPER_USER_PROPER_CANCEL.name -> {
                _navigation.tryEmit(Navigation.ProperUserCancel)
            }

            ContextMenuType.INTERNALLY_DISPLACED_CERT_CANCEL.name -> {
                _navigation.tryEmit(Navigation.InternallyDisplacedCertCancel)
            }

            ContextMenuType.EDIT_INTERNALLY_DISPLACED_PERSON_ADDRESS.name -> {
                _navigation.tryEmit(Navigation.EditInternallyDisplacedPersonAddress)
            }

            ContextMenuType.PENSION_CARD.code -> {
                _navigation.tryEmit(Navigation.PensionCard)
            }

            ContextMenuType.RESIDENCE_PERMIT_PERMANENT.code -> {
                _navigation.tryEmit(Navigation.ResidencePermitPermanent)
            }

            ContextMenuType.RESIDENCE_PERMIT_TEMPORARY.code -> {
                _navigation.tryEmit(Navigation.ResidencePermitTemporary)
            }

            ContextMenuType.DOWNLOAD_CERTIFICATE_PDF.name -> {
                _navigation.tryEmit(Navigation.DownloadPdf)
            }

            UIActionKeysCompose.BUTTON_REGULAR -> {
                _dismiss.tryEmit(UiEvent())
            }

            VERIFICATION_CODE_QR -> {
                event.data?.let {
                    _docAction.tryEmit(DocActions.OpenQr(event.data!!))
                }
            }

            VERIFICATION_CODE_EAN13 -> {
                event.data?.let {
                    _docAction.tryEmit(DocActions.OpenEan13(event.data!!))
                }
            }

            ContextMenuType.UPDATE_DOC.name -> {
                event.data?.let {
                    _docAction.tryEmit(DocActions.UpdateDoc(event.data!!))
                }
            }

            else -> {}
        }
    }

    sealed class Navigation : NavigationPath {
        object NavToFaqs : Navigation()
        object ToNavPnp : Navigation()
        object NavToDrl : Navigation()
        object NavToVehicleInsurance : Navigation()
        object NavToResidenceCert : Navigation()
        object NavToResidenceCertChild : Navigation()
        object NavToPensionCard : Navigation()
        object NavToFullInfo : Navigation()
        object NavToHousingCert : Navigation()
        object NavToFoundingRequest : Navigation()
        object BirthCertificate : Navigation()
        object InternallyDisplacedCertCancel : Navigation()
        object EditInternallyDisplacedPersonAddress : Navigation()

        object VaccinationCertificate : Navigation()
        object ChildVaccinationCertificate : Navigation()

        object ProperUserShare : Navigation()

        object ProperUserCancel : Navigation()

        object PensionCard : Navigation()
        object ResidencePermitPermanent : Navigation()
        object ResidencePermitTemporary : Navigation()
        object DownloadPdf : Navigation()
        object ToDocStackOrder : Navigation()

        object VehicleReRegistration : Navigation()

        data class ToDocStackOrderWithType(val docType: String) : Navigation()

    }

    sealed class DocActions : DocAction {
        object RemoveDoc : DocActions()
        object TranslateToUa : DocActions()
        object TranslateToEng : DocActions()
        object RateDocument : DocActions()
        object ShareWithFriends : DocActions()
        data class UpdateDoc(val type: String) : DocActions()
        data class OpenQr(val id: String) : DocActions()
        data class OpenEan13(val id: String) : DocActions()
        data class OpenVerificationCode(val id: String) : DocActions()

    }
}