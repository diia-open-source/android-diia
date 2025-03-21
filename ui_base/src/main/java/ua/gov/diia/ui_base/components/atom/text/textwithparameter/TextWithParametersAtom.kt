package ua.gov.diia.ui_base.components.atom.text.textwithparameter

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import ua.gov.diia.core.models.TextWithParameters
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

private const val LINK = "link"
private const val PHONE = "phone"
private const val EMAIL = "email"

@Composable
fun TextWithParametersAtom(
    modifier: Modifier = Modifier,
    data: TextWithParametersData,
    style: TextStyle = DiiaTextStyle.t3TextBody,
    onUIAction: (UIAction) -> Unit
) {
    val annotatedText: AnnotatedString =
        data.text.buildWithMetadataComposeEdition(data.parameters ?: emptyList())
    val context = LocalContext.current

    ClickableText(
        modifier = modifier.fillMaxWidth(),
        text = annotatedText,
        style = style
    ) {
        val annotation = annotatedText.getStringAnnotations(it, it)
        if (annotation.isNotEmpty()) {
            when (annotation[0].tag) {
                EMAIL -> emailLinkHandler(context, mailto = annotation[0].item)
                PHONE -> phoneLinkHandler(context, phone = annotation[0].item)
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

data class TextWithParametersData(
    val actionKey: String = UIActionKeysCompose.TEXT_WITH_PARAMETERS,
    val text: UiText,
    val parameters: List<TextParameter>? = null
) : UIElementData

fun TextWithParameters?.toComposeTextWithParameters(actionKey: String = UIActionKeysCompose.TEXT_WITH_PARAMETERS): TextWithParametersData? {
    val entity = this
    if (entity?.text == null) return null
    return entity.text?.let { text ->
        TextWithParametersData(
            actionKey = actionKey,
            text = UiText.DynamicString(text),
            parameters = if (entity.parameters != null) {
                mutableListOf<TextParameter>().apply {
                    entity.parameters?.forEach {
                        add(
                            TextParameter(
                                data = TextParameter.Data(
                                    name = it.data?.name?.let { n ->
                                        UiText.DynamicString(
                                            n
                                        )
                                    },
                                    resource = it.data?.resource?.let { r ->
                                        UiText.DynamicString(
                                            r
                                        )
                                    },
                                    alt = it.data?.alt?.let { a ->
                                        UiText.DynamicString(
                                            a
                                        )
                                    },
                                ),
                                type = it.type
                            )
                        )
                    }
                }
            } else {
                emptyList()
            }
        )
    }
}

fun String?.toComposeTextWithParameters(): TextWithParametersData? {
    if (this.isNullOrEmpty()) return null
    return TextWithParametersData(
        text = this.toDynamicString(),
        parameters = emptyList()
    )
}

@Preview
@Composable
fun TextWithParametersAtomPreview() {
    val linkText =
        "Міністерство цифрової трансформації та UNITED24 розпочинають проєкт «Армія дронів»! {details}"
    val linkParameter = TextParameter(
        type = TextWithParametersConstants.TYPE_LINK,
        data = TextParameter.Data(
            name = UiText.DynamicString("details"),
            alt = UiText.DynamicString("Детальніше на сайті UNITED24"),
            resource = UiText.DynamicString("https://u24.gov.ua/"),
        )
    )

    val phoneText =
        "Щоб вирішити це питання, будь ласка, зверніться до ДМС України за номером {dmsPhoneNumber}. Дякую!"
    val phoneParameter = TextParameter(
        type = TextWithParametersConstants.TYPE_PHONE,
        data = TextParameter.Data(
            name = UiText.DynamicString("dmsPhoneNumber"),
            alt = UiText.DynamicString("+38 (044) 278-34-02"),
            resource = UiText.DynamicString("+380442783402"),
        )
    )

    val emailText = "Напишіть нам і ми обов'язково вам відповімо {myCustomEmail}!."
    val emailParameter = TextParameter(
        type = TextWithParametersConstants.TYPE_MAIL,
        data = TextParameter.Data(
            name = UiText.DynamicString("myCustomEmail"),
            alt = UiText.DynamicString("ababagalamaga@gmail.com"),
            resource = UiText.DynamicString("ababagalamaga@gmail.com"),
        )
    )

    val textWithList =
        UiText.DynamicString("У межах державної програми єВідновлення можна отримати виплату на відновлення житла для тих, хто постраждав від збройної агресії росії. \n\nКошти надаються, якщо майно: \n• пошкоджене через бойові дії; \n• розміщене на неокупованій території; \n• підлягає відновленню, тобто не повністю зруйноване; \n• не було відремонтоване самостійно. \n\nОтримати виплату можна на ремонт: \n• приватних будинків; \n• квартир; \n• кімнат, наприклад, у гуртожитках. \n\nВажливо: громадяни, які вчиняли злочини проти основ національної безпеки або  перебувають під санкціями, не можуть отримати грошову допомогу. Це також стосується їхніх спадкоємців.")

    val testMetadata = listOf(linkParameter, phoneParameter, emailParameter)

    val textWithParametersData =
        TextWithParametersData(text = textWithList, parameters = testMetadata)

    TextWithParametersAtom(
        modifier = Modifier.padding(16.dp),
        data = textWithParametersData
    ) {
    }
}

@Preview
@Composable
fun TextWithParametersAtomPreview_Link() {
    val linkText =
        UiText.DynamicString("Міністерство цифрової трансформації та UNITED24 розпочинають проєкт «Армія дронів»! {details}")
    val linkParameter = TextParameter(
        type = TextWithParametersConstants.TYPE_LINK,
        data = TextParameter.Data(
            name = UiText.DynamicString("details"),
            alt = UiText.DynamicString("Детальніше на сайті UNITED24"),
            resource = UiText.DynamicString("https://u24.gov.ua/"),
        )
    )

    val textWithParametersData =
        TextWithParametersData(text = linkText, parameters = listOf(linkParameter))

    TextWithParametersAtom(
        modifier = Modifier.padding(16.dp),
        data = textWithParametersData
    ) {
    }
}

@Preview
@Composable
fun TextWithParametersAtomPreview_Phone() {
    val context = LocalContext.current
    val phoneText =
        UiText.DynamicString("Щоб вирішити це питання, будь ласка, зверніться до ДМС України \nза номером {dmsPhoneNumber}")
    val phoneParameter = TextParameter(
        type = TextWithParametersConstants.TYPE_PHONE,
        data = TextParameter.Data(
            name = UiText.DynamicString("dmsPhoneNumber"),
            alt = UiText.DynamicString("+38 (044) 278-34-02"),
            resource = UiText.DynamicString("+380442783402"),
        )
    )

    val textWithParametersData =
        TextWithParametersData(text = phoneText, parameters = listOf(phoneParameter))

    TextWithParametersAtom(
        modifier = Modifier.padding(16.dp),
        data = textWithParametersData
    ) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", it.data, null));
        context.startActivity(intent)
    }
}

@Preview
@Composable
fun TextWithParametersAtomPreview_Email() {
    val context = LocalContext.current
    val emailText =
        UiText.DynamicString("Напишіть нам і ми обов'язково вам відповімо {myCustomEmail}!.")
    val emailParameter = TextParameter(
        type = TextWithParametersConstants.TYPE_MAIL,
        data = TextParameter.Data(
            name = UiText.DynamicString("myCustomEmail"),
            alt = UiText.DynamicString("ababagalamaga@gmail.com"),
            resource = UiText.DynamicString("ababagalamaga@gmail.com"),
        )
    )

    val textWithParametersData =
        TextWithParametersData(text = emailText, parameters = listOf(emailParameter))

    TextWithParametersAtom(
        modifier = Modifier.padding(16.dp),
        data = textWithParametersData
    ) {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            val to = arrayOf(it.data?.replace("mailto:", ""))
            putExtra(Intent.EXTRA_EMAIL, to)
        }
        context.startActivity(Intent.createChooser(emailIntent, ""))
    }
}

private fun emailLinkHandler(context: Context, mailto: String?) {
    val emailIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        val to = arrayOf(mailto?.replace("mailto:", ""))
        putExtra(Intent.EXTRA_EMAIL, to)
    }
    context.startActivity(Intent.createChooser(emailIntent, ""))
}

private fun phoneLinkHandler(context: Context, phone: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
    context.startActivity(intent)
}

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

@Composable
fun UiText.buildWithMetadataComposeEdition(metadata: List<TextParameter>): AnnotatedString {
    val source = this.asString()
    return buildAnnotatedString {
        if (metadata.isEmpty()) {
            append(source)
        } else {
            var result = source
            val annotationsList = mutableListOf<StringAnnotation>()

            val regex = Regex("\\{(.*?)\\}") // Example: "{example}"
            val matches = regex.findAll(result)
            val itemsToReplace = matches.map { it.groupValues[1] }

            for (item in itemsToReplace) {
                metadata.find {
                    it.data?.name?.asString() == item
                }.let { textParameter ->
                    val old = "{${textParameter?.data?.name?.asString()}}"
                    val new = textParameter?.data?.alt?.asString() ?: ""
                    val oldValueOccurrenceCounter = result.windowed(old.length).count { it == old }
                    repeat(oldValueOccurrenceCounter) {
                        val startIndex = result.indexOf(old)
                        val endIndex = startIndex + new.length
                        result = result.replaceFirst(old, new)
                        annotationsList.add(
                            StringAnnotation(
                                startIndex,
                                endIndex,
                                textParameter?.type ?: "no type from params",
                                textParameter?.data?.resource?.asString() ?: "no resource for param"
                            )
                        )
                    }
                }
            }
            append(result)
            for (item in annotationsList) {
                addStringAnnotation(
                    tag = item.tag,
                    annotation = item.annotation,
                    start = item.startIndex,
                    end = item.endIndex
                )
                addStyle(
                    SpanStyle(textDecoration = TextDecoration.Underline),
                    item.startIndex,
                    item.endIndex
                )
            }
        }
    }
}

private data class StringAnnotation(
    val startIndex: Int,
    val endIndex: Int,
    val tag: String,
    val annotation: String
)