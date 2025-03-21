package ua.gov.diia.documents.verificationdata

import ua.gov.diia.documents.R
import ua.gov.diia.core.models.document.LocalizationType
import ua.gov.diia.documents.ui.DocsConst
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.code.QrCodeMlcData
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.organism.document.VerificationCodesOrgData

class DocumentVerificationDataFactory {

    fun getVerificationCodeOrgSuccessStateFromString(qr: String): VerificationCodesOrgData {
        return VerificationCodesOrgData(
            qrCode = QrCodeMlcData(
                qrLink = UiText.DynamicString(qr),
            ),
            idle = false,
            errorStubMessageMlc = null
        )
    }

    fun getVerificationCodeOrgExceptionState(
        localization: LocalizationType
    ): VerificationCodesOrgData {
        return VerificationCodesOrgData(
            qrCode = QrCodeMlcData(
                qrLink = UiText.StringResource(R.string.loading_stub),
            ),
            idle = false,
            errorStubMessageMlc = StubMessageMlcData(
                icon = UiText.StringResource(R.string.warning_stub_emoji),
                title = UiText.StringResource(
                    when (localization) {
                        LocalizationType.ua -> R.string.verification_code_download_fail_ua
                        LocalizationType.eng -> R.string.verification_code_download_fail_en
                    }
                ),
                button = ButtonStrokeAdditionalAtomData(
                    id = "",
                    title = UiText.StringResource(
                        when (localization) {
                            LocalizationType.ua -> R.string.verification_code_refresh_try_again_btn_label_ua
                            LocalizationType.eng -> R.string.verification_code_refresh_try_again_btn_label_en
                        }
                    ),
                    interactionState = UIState.Interaction.Enabled,
                    action = DataActionWrapper(
                        type = DocsConst.ACTION_REFRESH
                    )
                )
            )
        )
    }
}