package ua.gov.diia.ui_base.components.organism.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.checkbox.SmallCheckIconOrg
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.checkbox.SmallCheckIconMlc
import ua.gov.diia.ui_base.components.molecule.checkbox.SmallCheckIconMlcData
import ua.gov.diia.ui_base.components.molecule.checkbox.SmallCheckIconMlcData.IconType
import ua.gov.diia.ui_base.components.molecule.checkbox.generateSmallCheckIconMlcMockData
import ua.gov.diia.ui_base.components.molecule.checkbox.toUIModel
import ua.gov.diia.ui_base.components.organism.list.ChipVerticalGrid
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import kotlin.random.Random

@Composable
fun SmallCheckIconOrg(
    modifier: Modifier = Modifier,
    data: SmallCheckIconOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .background(
                color = White,
                shape = RoundedCornerShape(8.dp)
            )
            .testTag(data.componentId?.asString() ?: "")
    ) {
        data.title?.let {
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = it,
                color = Black,
                style = DiiaTextStyle.t3TextBody,
            )
            Spacer(modifier = Modifier.size(16.dp))
            DividerSlimAtom(color = BlackSqueeze)
            if (data.items.isEmpty()) {
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
        if (data.items.isNotEmpty()) {
            ChipVerticalGrid(spacing = 16.dp, modifier = Modifier.padding(8.dp)) {
                data.items.forEach {
                    SmallCheckIconMlc(data = it, onUIAction = onUIAction)
                }
            }
        }
    }
}

data class SmallCheckIconOrgData(
    val id: String = "",
    val inputCode: String? = null,
    val componentId: UiText? = null,
    val title: String?,
    val selectedCheckIconType: String?,
    val items: List<SmallCheckIconMlcData>
) : UIElementData {
    fun changeSelectedState(selectedCheckIconType: String?): SmallCheckIconOrgData {
        return this.copy(
            selectedCheckIconType = selectedCheckIconType,
            items = buildList {
                items.forEach {
                    if (it.icon.type == selectedCheckIconType) {
                        add(it.copy(selectionState = UIState.Selection.Selected))
                    } else {
                        add(it.copy(selectionState = UIState.Selection.Unselected))
                    }
                }
            }
        )
    }
}

fun generateSmallCheckIconOrgMockData(title: String?, itemsNumber: Int): SmallCheckIconOrgData {
    val values = IconType.entries.toTypedArray()
    val list = buildList {
        for (i in 0..<itemsNumber) {
            add(
                generateSmallCheckIconMlcMockData(
                    values[Random.nextInt(values.size)],
                    selectionState = if (i == 0) {
                        UIState.Selection.Selected
                    } else {
                        UIState.Selection.Unselected
                    }
                )
            )
        }
    }
    return SmallCheckIconOrgData(
        title = title,
        selectedCheckIconType = null,
        items = list
    )
}

fun SmallCheckIconOrg.toUIModel(): SmallCheckIconOrgData {
    val selectedCheckIconType =
        items.firstOrNull { it.smallCheckIconMlc.state == SmallCheckIconMlcData.State.SELECTED.type }?.smallCheckIconMlc?.icon
    val smallCheckDataItems = buildList {
        items.forEach {
            val selectionState = if (it.smallCheckIconMlc.icon == selectedCheckIconType) {
                UIState.Selection.Selected
            } else {
                UIState.Selection.Unselected
            }
            add(it.smallCheckIconMlc.toUIModel(selectionState = selectionState))
        }
    }
    return SmallCheckIconOrgData(
        id = id.orEmpty(),
        componentId = UiText.DynamicString(componentId.orEmpty()),
        inputCode = inputCode,
        title = title,
        items = smallCheckDataItems,
        selectedCheckIconType = selectedCheckIconType
    )
}

@Composable
@Preview
fun SmallCheckIconOrgNoItemsPreview() {
    SmallCheckIconOrg(
        data = generateSmallCheckIconOrgMockData(title = "Title", 0),
        onUIAction = {}
    )
}

@Composable
@Preview
fun SmallCheckIconOrgNoTitlePreview() {
    SmallCheckIconOrg(
        data = generateSmallCheckIconOrgMockData(title = null, 1),
        onUIAction = {}
    )
}

@Composable
@Preview
fun SmallCheckIconOrgPreview() {
    SmallCheckIconOrg(
        data = generateSmallCheckIconOrgMockData(title = null, 5),
        onUIAction = {}
    )
}

@Composable
@Preview
fun SmallCheckIconOrgLinesPreview() {
    SmallCheckIconOrg(
        data = generateSmallCheckIconOrgMockData(title = null, 7),
        onUIAction = {}
    )
}

