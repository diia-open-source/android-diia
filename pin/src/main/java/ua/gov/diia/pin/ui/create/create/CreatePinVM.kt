package ua.gov.diia.pin.ui.create.create

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.pin.R
import ua.gov.diia.pin.model.CreatePinFlowType
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.tile.NumButtonTileOrganismData
import ua.gov.diia.ui_base.navigation.BaseNavigation
import javax.inject.Inject

@HiltViewModel
class CreatePinVM @Inject constructor() : ViewModel() {

    private val _uiData = mutableStateListOf<UIElementData>()
    val uiData: SnapshotStateList<UIElementData> = _uiData

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    fun doInit(flowType: CreatePinFlowType) {

        var title = R.string.create_screen_title_text
        var descText = R.string.create_screen_description_text
        var codeSize = 4
        when (flowType) {
            CreatePinFlowType.GENERATE_SIGNATURE -> {
                title = R.string.create_screen_title_text_sign
                descText = R.string.create_screen_description_text_sign
                codeSize = 5
            }

            CreatePinFlowType.RESET_PIN -> {
                title = R.string.pin_reset_create_title_text
                descText = R.string.pin_reset_create_description_text
            }

            else -> {
            }
        }
        _uiData.add(
            TopGroupOrgData(
                titleGroupMlcData = TitleGroupMlcData(
                    leftNavIcon = if (flowType == CreatePinFlowType.RESET_PIN || flowType == CreatePinFlowType.GENERATE_SIGNATURE) {
                        TitleGroupMlcData.LeftNavIcon(
                            code = DiiaResourceIcon.BACK.code,
                            accessibilityDescription = UiText.StringResource(R.string.accessibility_back_button),
                            action = DataActionWrapper(
                                type = ActionsConst.ACTION_NAVIGATE_BACK,
                                subtype = null,
                                resource = null
                            )
                        )
                    } else null,
                    heroText = UiText.StringResource(title),
                    componentId = UiText.StringResource(R.string.create_pin_screen_title_test_tag),
                )
            )
        )
        _uiData.add(
            TextLabelMlcData(
                text = UiText.StringResource(descText),
                componentId = UiText.StringResource(R.string.create_pin_screen_title_test_tag)
            )
        )
        _uiData.add(
            NumButtonTileOrganismData(
                pinLength = codeSize,
                componentId = UiText.StringResource(R.string.create_pin_screen_btn_num_test_tag),
                componentIdEllipse = UiText.StringResource(R.string.create_pin_screen_ellips_test_tag)
            )
        )
    }

    fun onUIAction(uiAction: UIAction) {
        when (uiAction.actionKey) {
            UIActionKeysCompose.PIN_CREATED_NUM_BUTTON_ORGANISM -> {
                _uiData.findAndChangeFirstByInstance<NumButtonTileOrganismData> {
                    it.copy(clearPin = true)
                }
                _navigation.tryEmit(Navigation.ToPinConformation(uiAction.data ?: return))
            }
            UIActionKeysCompose.TITLE_GROUP_MLC -> {
                uiAction.action?.type.let {
                    if (it == ActionsConst.ACTION_NAVIGATE_BACK) {
                        _navigation.tryEmit(BaseNavigation.Back)
                    }
                }
            }
        }
    }

    sealed class Navigation : NavigationPath {
        data class ToPinConformation(val pin: String) : Navigation()
    }
}