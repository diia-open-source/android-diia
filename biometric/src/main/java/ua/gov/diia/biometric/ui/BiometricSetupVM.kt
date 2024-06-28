package ua.gov.diia.biometric.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ua.gov.diia.biometric.R
import ua.gov.diia.biometric.store.BiometricRepository
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.BUTTON_ALTERNATIVE
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.BUTTON_REGULAR
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import javax.inject.Inject

@HiltViewModel
class BiometricSetupVM @Inject constructor(
    private val biometricRepository: BiometricRepository,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction
) : ViewModel(),
    WithRetryLastAction by retryLastAction,
    WithErrorHandlingOnFlow by errorHandling {

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    private val _uiData = mutableStateListOf<UIElementData>()
    val uiData: SnapshotStateList<UIElementData> = _uiData

    init {
        _uiData.add(
            TopGroupOrgData(
                titleGroupMlcData = TitleGroupMlcData(
                    heroText = UiText.StringResource(R.string.biometric_screen_title_text),
                    componentId = UiText.StringResource(R.string.biometric_screen_title_test_tag)
                )
            )
        )
        _uiData.add(TextLabelMlcData(text = UiText.StringResource(R.string.biometric_screen_description_text)))
        _uiData.add(BtnPrimaryDefaultAtmData(
            title = UiText.StringResource(R.string.biometric_screen_button_text),
            componentId = UiText.StringResource(R.string.biometric_screen_btn_allow_test_tag)
        ))
        _uiData.add(
            BtnPlainAtmData(
                actionKey = BUTTON_ALTERNATIVE,
                id = "",
                title = UiText.StringResource(R.string.biometric_screen_alt_button_text),
                interactionState = UIState.Interaction.Enabled,
                componentId = UiText.StringResource(R.string.biometric_screen_btn_later__test_tag)
            )
        )
    }

    private fun enableBiometric(enable: Boolean) {
        executeActionOnFlow {
            if (enable) {
                biometricRepository.enableBiometricAuth(enable = true)
            }
            _navigation.emit(Navigation.CompletePinCreation)
        }
    }

    fun onUIAction(uiAction: UIAction) {
        when (uiAction.actionKey) {
            BUTTON_REGULAR -> {
                enableBiometric(true)
            }

            BUTTON_ALTERNATIVE -> {
                enableBiometric(false)
            }
        }
    }

    sealed class Navigation : NavigationPath {
        object CompletePinCreation : Navigation()
    }
}