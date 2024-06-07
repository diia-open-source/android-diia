package ua.gov.diia.ui_base.components.subatomic.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons

/**
 * In case of [UiIcon.DynamicIconBase64] use [contentDescription] parameter to support
 * accessibility. Use modifier to set size.
 *
 * In case of [UiIcon.DrawableResource] DiiaResourceIcon.getContentDescription(icon.code) content
 * will be used. Use modifier to set size.
 *
 * In case of [UiIcon.PlainString] use [fontSize] to setup icon size (as text).
 * Use [contentDescription] parameter to support accessibility
 *
 * In case of [UiIcon.URLIcon] use [contentDescription] parameter to support
 * accessibility. Use modifier to set size.
 *
 * Set [useContentDescription] to false to set contentDescription as null
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UiIconWrapperSubatomic(
    modifier: Modifier = Modifier,
    icon: UiIcon,
    fontSize: TextUnit = TextUnit.Unspecified,
    useContentDescription: Boolean = true,
    contentDescription: UiText? = null
) {
    when (icon) {
        is UiIcon.DynamicIconBase64 -> {
            IconBase64Subatomic(
                modifier = modifier,
                contentDescription = contentDescription?.asString(),
                base64Image = icon.value
            )
        }

        is UiIcon.DrawableResource -> {
            Image(
                modifier = modifier,
                painter = painterResource(
                    id = DiiaResourceIcon.getResourceId(icon.code)
                ),
                contentDescription = if (useContentDescription) {
                    stringResource(
                        id = DiiaResourceIcon.getContentDescription(
                            icon.code
                        )
                    )
                } else {
                    null
                }
            )
        }

        is UiIcon.PlainString -> {
            Text(
                modifier = modifier,
                text = icon.value,
                fontSize = fontSize
            )
        }

        is UiIcon.URLIcon -> {
            GlideImage(
                model = icon,
                contentDescription = contentDescription?.asString(),
                modifier = modifier,
                contentScale = ContentScale.Fit
            ) {
                it.error(R.drawable.diia_article_placeholder)
                    .placeholder(R.drawable.diia_article_placeholder)
                    .load(icon.link)
            }
        }
        is UiIcon.DrawableResInt ->{

        }
    }
}

@Preview
@Composable
fun UiIconWrapperSubatomic_DynamicIconBase64() {
    UiIconWrapperSubatomic(
        modifier = Modifier.size(24.dp),
        icon =
        UiIcon.DynamicIconBase64(PreviewBase64Icons.apple)
    )
}

@Preview
@Composable
fun UiIconWrapperSubatomic_DrawableResource() {
    UiIconWrapperSubatomic(
        modifier = Modifier.size(24.dp),
        icon =
        UiIcon.DrawableResource(DiiaResourceIcon.MENU.code)
    )
}

@Preview
@Composable
fun UiIconWrapperSubatomic_PlainString() {
    UiIconWrapperSubatomic(
        icon = UiIcon.PlainString("\uD83D\uDEAB"),
        fontSize = 20.sp
    )
}

@Preview
@Composable
fun UiIconWrapperSubatomic_URLIcon() {
    UiIconWrapperSubatomic(
        modifier = Modifier.size(24.dp),
        icon = UiIcon.URLIcon("https://www.uz.gov.ua/files/socials/394614.png"),
    )
}