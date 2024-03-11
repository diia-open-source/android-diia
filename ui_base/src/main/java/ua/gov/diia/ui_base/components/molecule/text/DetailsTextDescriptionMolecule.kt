package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.subatomic.icon.IconBase64Subatomic
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Images
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun DetailsTextDescriptionMolecule(
    modifier: Modifier = Modifier,
    data: DetailsTextDescriptionMoleculeData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .conditional(data.iconType != null) {
                clickable {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = data.description,
                            optionalId = data.id
                        )
                    )
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        data.logoBase64Image?.let {
            IconBase64Subatomic(modifier = Modifier.size(64.dp), base64Image = data.logoBase64Image)
        }
        Column(modifier = Modifier
            .conditional(data.logoBase64Image != null) {
                padding(start = 12.dp)
            }) {
            Text(
                text = data.title,
                style = DiiaTextStyle.t2TextDescription,
                color = BlackAlpha30
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f, fill = false),
                    text = data.description,
                    style = DiiaTextStyle.t2TextDescription,
                    color = Black
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    data.iconType?.let {
                        if (data.iconType == DetailsTextDescriptionMoleculeIconType.ICON_COPY) {
                            Image(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .size(24.dp),
                                painter = painterResource(id = R.drawable.diia_icon_copy_to_clipboard),
                                contentDescription = stringResource(id = R.string.copy_to_clipboard),
                                colorFilter = ColorFilter.tint(Black)
                            )
                        }
                    }
                }
            }

        }
    }
}

data class DetailsTextDescriptionMoleculeData(
    val actionKey: String = UIActionKeysCompose.DETAILS_TEXT_DESCRIPTION_MOLECULE,
    val id: String? = "",
    val title: String,
    val description: String,
    val logoBase64Image: String? = null,
    val iconType: DetailsTextDescriptionMoleculeIconType? = null
) : DetailsText

@Composable
@Preview
fun DetailsTextDescriptionMoleculePreview_WithImage() {
    val state = DetailsTextDescriptionMoleculeData(title = "Title", description = "Description", logoBase64Image = PreviewBase64Images.alphaBank)
    DetailsTextDescriptionMolecule(modifier = Modifier.fillMaxWidth(), data = state) {

    }
}

@Composable
@Preview
fun DetailsTextDescriptionMoleculePreview_NoImage() {
    val state = DetailsTextDescriptionMoleculeData(title = "Title", description = "Description")
    DetailsTextDescriptionMolecule(modifier = Modifier.fillMaxWidth(), data = state) {

    }
}

@Composable
@Preview
fun DetailsTextDescriptionMoleculePreview_WithIcon() {
    val state = DetailsTextDescriptionMoleculeData(
        title = "Title",
        description = LoremIpsum(40).values.joinToString(),
        logoBase64Image = PreviewBase64Images.alphaBank,
        iconType = DetailsTextDescriptionMoleculeIconType.ICON_COPY
    )
    DetailsTextDescriptionMolecule(modifier = Modifier.fillMaxWidth(), data = state) {

    }
}

@Composable
@Preview
fun DetailsTextDescriptionMoleculePreview_WithIconSmallText() {
    val state = DetailsTextDescriptionMoleculeData(
        title = "Title",
        description = LoremIpsum(2).values.joinToString(),
        iconType = DetailsTextDescriptionMoleculeIconType.ICON_COPY
    )
    DetailsTextDescriptionMolecule(modifier = Modifier.fillMaxWidth(), data = state) {

    }
}

enum class DetailsTextDescriptionMoleculeIconType{
    ICON_COPY
}