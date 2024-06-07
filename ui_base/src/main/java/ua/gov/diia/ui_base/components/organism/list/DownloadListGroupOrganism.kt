package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.list.DownloadListItemGroupOrg
import ua.gov.diia.core.models.common_compose.org.list.ProgressTypes
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.atom.list.DownloadListItemAtom
import ua.gov.diia.ui_base.components.atom.list.DownloadListItemAtomData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun DownloadListGroupOrganism(
    modifier: Modifier = Modifier,
    data: DownloadListGroupOrganismData,
    onUIAction: (UIAction) -> Unit
) {

    val localData = remember { mutableStateOf(data) }
    LaunchedEffect(key1 = data) {
        localData.value = data
    }

    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        val downloadedList = data.itemList
            .filter { it.progressState == UIState.Progress.Downloaded || it.progressState == UIState.Progress.UpdateAvailable }
            .sortedBy { it.order }
            .toMutableList()
        val pendingList = data.itemList
            .sortedBy { it.order }
            .toMutableList()
        Column(modifier = modifier.background(color = White, shape = RoundedCornerShape(16.dp))) {
            if (downloadedList.isNotEmpty()) {
                Text(
                    modifier = modifier.padding(
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp
                    ),
                    text = data.loadedListHeader.asString(),
                    style = DiiaTextStyle.t3TextBody,
                    color = Black
                )
                DividerSlimAtom(color = BlackSqueeze)
            }

            downloadedList.forEachIndexed { index, item ->
                DownloadListItemAtom(
                    data = item,
                    onUIAction = onUIAction,
                )
                if (index != data.itemList.size - 1) {
                    DividerSlimAtom(color = BlackSqueeze)
                }
            }
        }

        if (downloadedList.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
        }

        Column(
            modifier = modifier.background(
                color = White,
                shape = RoundedCornerShape(16.dp)
            )
        ) {
            if (pendingList.isNotEmpty()) {
                Text(
                    modifier = modifier.padding(
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp
                    ),
                    text = data.listHeader.asString(),
                    style = DiiaTextStyle.t3TextBody,
                    color = Black
                )
                DividerSlimAtom(color = BlackSqueeze)
            }

            pendingList.forEachIndexed { index, item ->
                DownloadListItemAtom(
                    data = item,
                    onUIAction = onUIAction,
                )
                if (index != data.itemList.size - 1) {
                    DividerSlimAtom(color = BlackSqueeze)
                }
            }
        }
    }
}


data class DownloadListGroupOrganismData(
    val listHeader: UiText,
    val loadedListHeader: UiText,
    val itemList: SnapshotStateList<DownloadListItemAtomData>,
) : UIElementData

fun DownloadListItemGroupOrg.toUiModel(
    savedRegions: List<String>,
    inProgressRegions: List<String>,
    updateRequiredRegions: List<String>,
): DownloadListGroupOrganismData {
    val itemList: SnapshotStateList<DownloadListItemAtomData> = SnapshotStateList()

    items.forEachIndexed { index, item ->
        if (item.state != ua.gov.diia.core.models.common_compose.org.list.ActionTypes.invisible) {
            val state = when {
                inProgressRegions.contains(item.id) -> ProgressTypes.inProgress
                updateRequiredRegions.contains(item.id) -> ProgressTypes.updateAvailable
                savedRegions.contains(item.id) ->  ProgressTypes.downloaded
                item.state == ua.gov.diia.core.models.common_compose.org.list.ActionTypes.disabled -> ProgressTypes.notAvailable
                else -> ProgressTypes.notDownloaded
            }
            itemList.add(item.toDownloadListItemAtomData(index, state))
        }
    }

    return DownloadListGroupOrganismData(
        UiText.DynamicString(listHeader),
        UiText.DynamicString(loadedListHeader),
        itemList
    )
}

private fun DownloadListItemGroupOrg.Item.toDownloadListItemAtomData(
    index: Int,
    progressState: ProgressTypes
): DownloadListItemAtomData {
    val descriptionsMap = mutableMapOf<UIState.Progress, String>()
    description?.forEach { descriptionsMap[it.type.mapToProgressState()] = it.text }

    return DownloadListItemAtomData(
        id = this.id,
        updateDate = this.updateDate ?: "1970",
        mapLink = this.mapLink,
        title = UiText.DynamicString(label),
        progressState = progressState.mapToProgressState(),
        descriptionsMap = descriptionsMap,
        order = index
    )
}

private fun ProgressTypes.mapToProgressState() = when (this) {
    ProgressTypes.notDownloaded -> UIState.Progress.NotDownloaded
    ProgressTypes.inProgress -> UIState.Progress.InProgress
    ProgressTypes.failedDownloaded -> UIState.Progress.Failed
    ProgressTypes.downloaded -> UIState.Progress.Downloaded
    ProgressTypes.notAvailable -> UIState.Progress.NotAvailable
    ProgressTypes.updateAvailable -> UIState.Progress.UpdateAvailable
    ProgressTypes.failedUpdate -> UIState.Progress.Failed
}

@Preview
@Composable
fun DownloadListGroupOrganismPreview() {
    val itemList: SnapshotStateList<DownloadListItemAtomData> = SnapshotStateList()

    val descriptionsMap = mutableMapOf<UIState.Progress, String>()
    descriptionsMap[UIState.Progress.NotDownloaded] = "description1"
    descriptionsMap[UIState.Progress.InProgress] = "description2"
    descriptionsMap[UIState.Progress.Failed] = "description3"
    descriptionsMap[UIState.Progress.Downloaded] = "description4"
    descriptionsMap[UIState.Progress.NotAvailable] = "description5"
    descriptionsMap[UIState.Progress.UpdateAvailable] = "description"

    itemList.add(
        DownloadListItemAtomData(
            title = UiText.DynamicString("NotDownloaded"),
            interactionState = UIState.Interaction.Enabled,
            descriptionsMap = descriptionsMap,
            order = 0
        )
    )

    itemList.add(
        DownloadListItemAtomData(
            title = UiText.DynamicString("InProgress"),
            interactionState = UIState.Interaction.Enabled,
            progressState = UIState.Progress.InProgress,
            progressValue = "25%",
            descriptionsMap = descriptionsMap,
            order = 1
        )
    )

    itemList.add(
        DownloadListItemAtomData(
            title = UiText.DynamicString("FailedDownloaded"),
            interactionState = UIState.Interaction.Enabled,
            progressState = UIState.Progress.Failed,
            descriptionsMap = descriptionsMap,
            order = 2
        )
    )

    itemList.add(
        DownloadListItemAtomData(
            title = UiText.DynamicString("NotAvailable"),
            interactionState = UIState.Interaction.Enabled,
            progressState = UIState.Progress.NotAvailable,
            descriptionsMap = descriptionsMap,
            order = 3
        )
    )

    itemList.add(
        DownloadListItemAtomData(
            title = UiText.DynamicString("Downloaded"),
            interactionState = UIState.Interaction.Enabled,
            progressState = UIState.Progress.Downloaded,
            descriptionsMap = descriptionsMap,
            order = 4
        )
    )

    itemList.add(
        DownloadListItemAtomData(
            title = UiText.DynamicString("UpdateAvailable"),
            interactionState = UIState.Interaction.Enabled,
            progressState = UIState.Progress.UpdateAvailable,
            descriptionsMap = descriptionsMap,
            order = 4
        )
    )

    val data = DownloadListGroupOrganismData(
        UiText.DynamicString("Всі області:"),
        UiText.DynamicString("Завантажені:"),
        itemList
    )
    DownloadListGroupOrganism(data = data) {}
}