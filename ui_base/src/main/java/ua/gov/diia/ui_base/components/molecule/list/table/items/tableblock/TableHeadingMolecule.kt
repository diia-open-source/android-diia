package ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.IconWithBadge
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun TableHeadingMolecule(
    modifier: Modifier = Modifier,
    data: TableHeadingMoleculeData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            data.title?.let {
                Text(
                    text = it.asString(),
                    color = Black,
                    style = if (data.size == Size.main) {
                        DiiaTextStyle.h4ExtraSmallHeading
                    } else {
                        DiiaTextStyle.t1BigText

                    }
                )
            }
            data.description?.let {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = it.asString(),
                    color = Color.Gray,
                    style = DiiaTextStyle.t2TextDescription
                )
            }
        }
        data.icon?.let {
            IconWithBadge(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp)
                    .noRippleClickable {
                        onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
                    }, image = data.icon
            )
        }
    }
}

data class TableHeadingMoleculeData(
    val actionKey: String = UIActionKeysCompose.TABLE_HEADING_MOLECULE,
    val id: String? = null,
    val size: Size = Size.main,
    val title: UiText? = null,
    val icon: UiText? = null,
    val description: UiText? = null
) : UIElementData

enum class Size {
    main, secondary;
}

@Composable
@Preview
fun TableHeadingMoleculeMainPreview() {
    val state = TableHeadingMoleculeData(
        id = "123",
        title = UiText.DynamicString("Heading"),
        size = Size.main,
        icon = UiText.StringResource(R.drawable.ic_copy)
    )
    TableHeadingMolecule(
        modifier = Modifier.padding(16.dp), data = state
    ) {

    }
}

@Composable
@Preview
fun TableHeadingMoleculeMainWithDescriptionPreview() {
    val state = TableHeadingMoleculeData(
        id = "123",
        title = UiText.DynamicString("Heading"),
        size = Size.main,
        icon = UiText.StringResource(R.drawable.ic_copy),
        description = UiText.DynamicString("Description")
    )
    TableHeadingMolecule(
        modifier = Modifier.padding(16.dp), data = state
    ) {

    }
}

@Composable
@Preview
fun TableHeadingMoleculeSecondaryPreview() {
    val state = TableHeadingMoleculeData(
        id = "123",
        title = UiText.DynamicString("Heading"),
        size = Size.secondary,
        icon = UiText.StringResource(R.drawable.ic_copy)
    )
    TableHeadingMolecule(
        modifier = Modifier.padding(16.dp), data = state
    ) {

    }
}

@Composable
@Preview
fun TableHeadingMoleculeSecondaryWithDescriptionPreview() {
    val state = TableHeadingMoleculeData(
        id = "123",
        title = UiText.DynamicString("Heading"),
        size = Size.secondary,
        icon = UiText.StringResource(R.drawable.ic_copy),
        description = UiText.DynamicString("Description")
    )
    TableHeadingMolecule(
        modifier = Modifier.padding(16.dp), data = state
    ) {

    }
}