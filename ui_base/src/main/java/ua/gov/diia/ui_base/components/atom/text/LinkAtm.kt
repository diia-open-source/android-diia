package ua.gov.diia.ui_base.components.atom.text

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersConstants
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.buildWithMetadataComposeEdition
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

private const val LINK = "link"

@Composable
fun LinkAtm(
    modifier: Modifier = Modifier,
    data: LinkAtmData,
    onUIAction: (UIAction) -> Unit
) {
    val tag = UiText.DynamicString("{tag}")
    val annotatedText: AnnotatedString =
        tag.buildWithMetadataComposeEdition(
            listOf(
                TextParameter(
                    type = TextWithParametersConstants.TYPE_LINK,
                    data = TextParameter.Data(
                        name = UiText.DynamicString("tag"),
                        alt = data.text,
                        resource = UiText.DynamicString(data.link),
                    )
                )
            )
        )
    val context = LocalContext.current

    ClickableText(
        modifier = modifier
            .defaultMinSize(minWidth = 40.dp, minHeight = 40.dp)
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp),
        text = annotatedText,
        style = DiiaTextStyle.t3TextBody
    ) {
        val annotation = annotatedText.getStringAnnotations(it, it)
        if (annotation.isNotEmpty()) {
            when (annotation[0].tag) {
                LINK -> openLink(context, link = annotation[0].item)
                else -> onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = annotation[0].item,
                        optionalId = annotation[0].tag
                    )
                )
            }
        }
    }
}

data class LinkAtmData(
    val actionKey: String = UIActionKeysCompose.LINK_ATM,
    val link: String,
    val text: UiText
)

private fun openLink(context: Context, link: String) {
    if (link.startsWith("https:")) {
        val uri = try {
            link.toUri()
        } catch (_: Exception) {
            return
        }
        context.startActivity(Intent(Intent.ACTION_VIEW).apply { data = uri })
    }
}

@Preview
@Composable
fun LinkAtmPreview() {
    val data = LinkAtmData(link = "https://u24.gov.ua/", text = UiText.DynamicString("details"))

    LinkAtm(data = data) {

    }
}