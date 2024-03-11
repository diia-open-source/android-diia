package ua.gov.diia.ui_base.components.atom.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.IconWithBadge
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Red

@Composable
fun DownloadListItemAtom(
    modifier: Modifier = Modifier,
    data: DownloadListItemAtomData,
    onUIAction: (UIAction) -> Unit
) {
    Row(modifier = modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        val title = data.title?.asString()
        Box(modifier = modifier.size(24.dp)) {
            if (data.progressState == UIState.Progress.NotDownloaded
                || data.progressState == UIState.Progress.Downloaded
                || data.progressState == UIState.Progress.Failed
                || data.progressState == UIState.Progress.NotAvailable
                || data.progressState == UIState.Progress.UpdateAvailable
            ) {
                val color = if (data.progressState == UIState.Progress.Failed) {
                    Red
                } else if (data.interactionState == UIState.Interaction.Disabled) {
                    BlackAlpha10
                } else {
                    Black
                }
                val icon = when (data.progressState) {
                    UIState.Progress.Downloaded -> R.drawable.ic_delete
                    UIState.Progress.Failed -> R.drawable.ic_download_retry
                    UIState.Progress.NotAvailable -> R.drawable.ic_download_disabled
                    UIState.Progress.InProgress,
                    UIState.Progress.NotDownloaded -> R.drawable.ic_download
                    UIState.Progress.UpdateAvailable -> R.drawable.ic_download_update
                }
                IconWithBadge(
                    modifier = Modifier
                        .fillMaxSize()
                        .noRippleClickable {
                            if (data.progressState == UIState.Progress.NotAvailable) return@noRippleClickable
                            onUIAction(
                                UIAction(
                                    actionKey = data.altActionKey,
                                    data = title,
                                    optionalId = data.id,
                                )
                            )
                        },
                    image = UiText.StringResource(icon),
                    imageColor = color
                )
            }

            if (data.progressState == UIState.Progress.InProgress) {
                LoaderCircularEclipse23Subatomic(
                    modifier = Modifier.size(16.dp).align(Alignment.Center)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
                .noRippleClickable {
                    if (data.interactionState == UIState.Interaction.Disabled) return@noRippleClickable
                    if (data.progressState == UIState.Progress.Downloaded) {
                        onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
                    } else if (data.progressState == UIState.Progress.NotDownloaded || data.progressState == UIState.Progress.Failed) {
                        onUIAction(
                            UIAction(
                                actionKey = data.altActionKey,
                                data = title,
                                optionalId = data.id,
                            )
                        )
                    }
                }
        ) {
            Text(
                modifier = modifier.alpha(if (data.progressState == UIState.Progress.NotAvailable) 0.3f else 1f),
                text = data.title?.asString() ?: "",
                style = DiiaTextStyle.t1BigText,
                color = Black
            )

            data.descriptionForState()?.let {
                val text = if (data.progressState == UIState.Progress.Failed && data.failedReason != null) {
                    when(data.failedReason) {
                        DownloadListItemAtomData.FailedReason.NO_SPACE -> "Недостатньо памʼяті"
                        DownloadListItemAtomData.FailedReason.DOWNLOAD_FAILED -> "Помилка завантаження"
                        DownloadListItemAtomData.FailedReason.HTTP_ERROR, DownloadListItemAtomData.FailedReason.NO_INTERNET -> "Немає інтернету"
                    }
                } else it

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = text,
                    style = DiiaTextStyle.t2TextDescription,
                    color = if (data.progressState == UIState.Progress.Failed) Red else BlackAlpha30
                )
            }
        }
        if (data.interactionState == UIState.Interaction.Enabled
            && (data.progressState == UIState.Progress.Downloaded || data.progressState == UIState.Progress.UpdateAvailable)) {
            IconWithBadge(
                modifier = Modifier
                    .size(20.dp)
                    .noRippleClickable {
                        onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
                    },
                image = UiText.StringResource(R.drawable.ic_forward),
                imageColor = Black
            )
        }

        if (data.progressState == UIState.Progress.InProgress) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "${data.progressValue ?: 0}%",
                style = DiiaTextStyle.t1BigText,
                color = Black
            )
        }
    }
}


data class DownloadListItemAtomData(
    val actionKey: String = UIActionKeysCompose.LIST_ITEM_CLICK,
    val altActionKey: String = UIActionKeysCompose.LIST_ITEM_ALTERNATIVE_CLICK,
    val id: String? = "",
    val updateDate: String = "",
    val mapLink: String? = null,
    val title: UiText? = null,
    val progressValue: String? = null,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val progressState: UIState.Progress = UIState.Progress.NotDownloaded,
    val descriptionsMap: Map<UIState.Progress, String> = emptyMap(),
    val failedReason : FailedReason? = null,
    val order: Int
) : UIElementData {

    fun descriptionForState() = descriptionsMap[progressState]
    enum class FailedReason {
        NO_SPACE,
        HTTP_ERROR,
        DOWNLOAD_FAILED,
        NO_INTERNET
    }
}

@Composable
@Preview
fun DownloadListItemAtom_NotDownloaded() {
    val descriptionsMap = mutableMapOf<UIState.Progress, String>()
    descriptionsMap[UIState.Progress.NotDownloaded] = "description"

    val state = DownloadListItemAtomData(
        title = UiText.DynamicString("NotDownloaded"),
        interactionState = UIState.Interaction.Enabled,
        descriptionsMap = descriptionsMap,
        order = 0
    )
    DownloadListItemAtom(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun DownloadListItemAtom_InProgress() {
    val descriptionsMap = mutableMapOf<UIState.Progress, String>()
    descriptionsMap[UIState.Progress.InProgress] = "description"

    val state = DownloadListItemAtomData(
        title = UiText.DynamicString("InProgress"),
        interactionState = UIState.Interaction.Enabled,
        progressState = UIState.Progress.InProgress,
        progressValue = "25",
        descriptionsMap = descriptionsMap,
        order = 0
    )
    DownloadListItemAtom(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun DownloadListItemAtom_FailedDownloaded() {
    val descriptionsMap = mutableMapOf<UIState.Progress, String>()
    descriptionsMap[UIState.Progress.Failed] = "description"

    val state = DownloadListItemAtomData(
        title = UiText.DynamicString("FailedDownloaded"),
        interactionState = UIState.Interaction.Enabled,
        progressState = UIState.Progress.Failed,
        descriptionsMap = descriptionsMap,
        order = 0
    )
    DownloadListItemAtom(modifier = Modifier, data = state) {}
}


@Composable
@Preview
fun DownloadListItemAtom_Downloaded() {
    val descriptionsMap = mutableMapOf<UIState.Progress, String>()
    descriptionsMap[UIState.Progress.Downloaded] = "description"

    val state = DownloadListItemAtomData(
        title = UiText.DynamicString("Downloaded"),
        interactionState = UIState.Interaction.Enabled,
        progressState = UIState.Progress.Downloaded,
        descriptionsMap = descriptionsMap,
        order = 0
    )
    DownloadListItemAtom(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun DownloadListItemAtom_NotAvailable() {
    val descriptionsMap = mutableMapOf<UIState.Progress, String>()
    descriptionsMap[UIState.Progress.NotAvailable] = "description"

    val state = DownloadListItemAtomData(
        title = UiText.DynamicString("NotAvailable"),
        interactionState = UIState.Interaction.Enabled,
        progressState = UIState.Progress.NotAvailable,
        descriptionsMap = descriptionsMap,
        order = 0
    )
    DownloadListItemAtom(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun DownloadListItemAtom_Disabled() {
    val descriptionsMap = mutableMapOf<UIState.Progress, String>()
    descriptionsMap[UIState.Progress.NotDownloaded] = "description"

    val state = DownloadListItemAtomData(
        title = UiText.DynamicString("Label"),
        interactionState = UIState.Interaction.Disabled,
        progressState = UIState.Progress.NotDownloaded,
        descriptionsMap = descriptionsMap,
        order = 0
    )
    DownloadListItemAtom(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun DownloadListItemAtom_UpdateAvailable() {
    val descriptionsMap = mutableMapOf<UIState.Progress, String>()
    descriptionsMap[UIState.Progress.UpdateAvailable] = "description"

    val state = DownloadListItemAtomData(
        title = UiText.DynamicString("UpdateAvailable"),
        interactionState = UIState.Interaction.Enabled,
        progressState = UIState.Progress.UpdateAvailable,
        descriptionsMap = descriptionsMap,
        order = 0
    )
    DownloadListItemAtom(modifier = Modifier, data = state) {}
}
