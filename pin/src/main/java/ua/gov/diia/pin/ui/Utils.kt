package ua.gov.diia.pin.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.tile.NumButtonTileOrganismData

@Composable
inline fun <reified T> List<T>.asSnapshotStateList() =
    remember { mutableStateListOf(*this.toTypedArray()) }

fun getPinTestData(headerText: String, bodyText: String, pinLength: Int = 4) = listOf(
    TopGroupOrgData(
        titleGroupMlcData = TitleGroupMlcData(
            heroText = UiText.DynamicString(headerText),
            leftNavIcon = TitleGroupMlcData.LeftNavIcon(
                code = DiiaResourceIcon.BACK.code,
                accessibilityDescription = UiText.StringResource(R.string.accessibility_back_button),
                action = DataActionWrapper(
                    type = "back",
                    subtype = null,
                    resource = null
                )
            )
        )
    ),
    TextWithParametersData(
        text = UiText.DynamicString(bodyText)
    ),
    NumButtonTileOrganismData(pinLength = pinLength)
)