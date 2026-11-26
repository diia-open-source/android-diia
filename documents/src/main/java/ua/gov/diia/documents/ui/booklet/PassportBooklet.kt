package ua.gov.diia.documents.ui.booklet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.gov.diia.documents.R
import ua.gov.diia.documents.models.PassportBookletData

/** The host owns loading, document status, image decoding, fullscreen and dismissal.
 * Pass the separating hinge width as [pageGap] for a centered dual-screen display.
 */
@Composable
fun PassportBooklet(
    data: PassportBookletData,
    modifier: Modifier = Modifier,
    photo: ImageBitmap? = null,
    signature: ImageBitmap? = null,
    pageGap: Dp = 1.dp
) {
    val gap = pageGap.coerceAtLeast(0.dp)
    CompositionLocalProvider(
        androidx.compose.material3.LocalContentColor provides Color(0xFF1F2B3D)
    ) {
        BoxWithConstraints(modifier.background(Color(0xFFF7F0D1))) {
            if (maxWidth - gap >= 600.dp) {
                Row(Modifier.fillMaxSize().background(Color(0xFF1F2B3D).copy(alpha = 0.2f))) {
                    TitlePage(
                        data, Modifier.weight(1f).fillMaxHeight().background(Color(0xFFF7F0D1))
                            .verticalScroll(rememberScrollState())
                    )
                    Spacer(Modifier.width(gap))
                    IdentityPage(
                        data, photo, signature,
                        Modifier.weight(1f).fillMaxHeight().background(Color(0xFFF7F0D1))
                            .verticalScroll(rememberScrollState())
                    )
                }
            } else {
                Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                    TitlePage(data, Modifier.fillMaxWidth())
                    IdentityPage(data, photo, signature, Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun TitlePage(data: PassportBookletData, modifier: Modifier) {
    val flagDescription = stringResource(R.string.booklet_flag)
    Column(modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Column(Modifier.size(96.dp, 64.dp).semantics {
            contentDescription = flagDescription
        }) {
            Box(Modifier.fillMaxWidth().weight(1f).background(Color(0xFF005DBA)))
            Box(Modifier.fillMaxWidth().weight(1f).background(Color(0xFFFFD100)))
        }
        Text(
            stringResource(R.string.booklet_title),
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = FontFamily.Serif,
            modifier = Modifier.semantics { heading() }
        )
        Text(data.displayNumber, style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily.Monospace)
        BookletField(stringResource(R.string.booklet_issued_at), data.issuance.issuedAt)
        BookletField(stringResource(R.string.booklet_issued_by), data.issuance.issuedBy)
        OptionalField(stringResource(R.string.booklet_valid_until), data.issuance.validUntil)
        data.additionalFields.orEmpty().forEach { BookletField(it.label, it.value) }
    }
}

@Composable
private fun IdentityPage(
    data: PassportBookletData,
    photo: ImageBitmap?,
    signature: ImageBitmap?,
    modifier: Modifier
) {
    Column(modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (photo != null) {
            Image(photo, stringResource(R.string.booklet_photo),
                Modifier.size(108.dp, 144.dp), contentScale = ContentScale.Fit)
        } else {
            Box(Modifier.size(108.dp, 144.dp).background(Color.Black.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.booklet_no_photo),
                    modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.bodySmall)
            }
        }
        BookletField(stringResource(R.string.booklet_last_name), data.identity.lastName)
        BookletField(stringResource(R.string.booklet_first_name), data.identity.firstName)
        OptionalField(stringResource(R.string.booklet_middle_name), data.identity.middleName)
        BookletField(stringResource(R.string.booklet_birth_date), data.identity.birthDate)
        BookletField(stringResource(R.string.booklet_birth_place), data.identity.birthPlace)
        OptionalField(stringResource(R.string.booklet_sex), data.identity.sex)
        if (signature != null) {
            Image(signature, stringResource(R.string.booklet_signature),
                Modifier.width(144.dp).height(56.dp), contentScale = ContentScale.Fit)
        }
    }
}

@Composable
private fun BookletField(label: String, value: String) {
    Column(Modifier.semantics(mergeDescendants = true) {},
        verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun OptionalField(label: String, value: String?) {
    if (!value.isNullOrBlank()) BookletField(label, value)
}

@Preview(widthDp = 840, heightDp = 600, showBackground = true)
@Composable
private fun PassportBookletPreview() {
    MaterialTheme {
        PassportBooklet(
            data = PassportBookletData(
                identity = PassportBookletData.Identity(
                    lastName = "ПРИКЛАД", firstName = "Надія", middleName = "Тестівна",
                    birthDate = "24.08.1991", birthPlace = "м. Київ"
                ),
                issuance = PassportBookletData.Issuance(
                    series = "АА", number = "000000", issuedAt = "24.08.2007",
                    issuedBy = "Демонстраційні дані"
                )
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}
