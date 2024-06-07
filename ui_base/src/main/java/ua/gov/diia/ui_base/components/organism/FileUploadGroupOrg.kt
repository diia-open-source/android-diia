package ua.gov.diia.ui_base.components.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.media.FileUploadGroupOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.button.toUiModel
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemUploadMlc
import ua.gov.diia.ui_base.components.molecule.list.ListItemUploadMlcData
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun FileUploadGroupOrg(
    modifier: Modifier = Modifier,
    data: FileUploadGroupOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {

    var isLoading by remember {
        mutableStateOf(
            checkIfAnyElementInLoadingState(data.fileItems?.toList() ?: listOf(), progressIndicator)
        )
    }

    LaunchedEffect(key1 = progressIndicator) {
        isLoading = checkIfAnyElementInLoadingState(data.fileItems ?: listOf(), progressIndicator)
    }

    Column(modifier = modifier
        .padding(top = 24.dp, start = 24.dp, end = 24.dp,)
        .background(
            color = White, shape = RoundedCornerShape(8.dp)
        )) {

        data.title?.let {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                text = it.asString(),
                style = DiiaTextStyle.t3TextBody
            )
        }
        data.description?.let {
            Text(
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                text = it.asString(),
                style = DiiaTextStyle.t4TextSmallDescription,
                color = BlackAlpha50
            )
        }
// FILES LIST
        data.fileItems?.let { list ->
            DividerSlimAtom(color = BlackSqueeze)
            list.forEachIndexed { index, item ->
                ListItemUploadMlc(
                    data = item,
                    onUIAction = {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                data = it.data,
                                optionalId = data.id,
                                action = it.action
                            )
                        )
                    },
                )
                if (index != data.fileItems.size - 1) {
                    DividerSlimAtom(color = BlackSqueeze)
                }
            }
        }

        DividerSlimAtom(
            modifier = Modifier
                .height(1.dp),
            color = BlackSqueeze
        )
// BTN ADD FILE
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    if (data.fileItems?.size?.let { it < 10 } != false) {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                action = data.button.action
                            )
                        )
                    }
                },
            horizontalArrangement = Arrangement.Center
        ) {
            BtnPlainIconAtm(
                modifier = modifier
                    .padding(vertical = 16.dp),
                data = data.button,

            ){
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        action = data.button.action
                    )
                )
            }
        }
    }
}

private fun checkIfAnyElementInLoadingState(
    items: List<ListItemUploadMlcData>,
    progressIndicator: Pair<String, Boolean>
): Boolean {
    return items.any {
        it.id == progressIndicator.first && progressIndicator.second
    }
}

data class FileUploadGroupOrgData(
    val actionKey: String = UIActionKeysCompose.MEDIA_UPLOAD_GROUP_ORG,
    val id: String? = null,
    val title: UiText? = null,
    val description: UiText? = null,
    val maxCount: Int? = null,
    val button: BtnPlainIconAtmData,
    val fileItems: SnapshotStateList<ListItemUploadMlcData>? = null

) : UIElementData {
    fun setContent(list: List<ListItemUploadMlcData>): FileUploadGroupOrgData {
        val newItems = SnapshotStateList<ListItemUploadMlcData>()
        newItems.addAll(list)
        return this.copy(
            fileItems = if (newItems.isEmpty()) null else newItems,
            button = this.button.changeInteractionState(
                fileItems?.size?.let{ it  < 10 } ?: true)
        )
    }
}

fun FileUploadGroupOrg.toUiModel(): FileUploadGroupOrgData {
    return FileUploadGroupOrgData(
        title = this.title?.let { UiText.DynamicString(it) },
        description = this.description?.let { UiText.DynamicString(it) },
        maxCount = maxCount,
        button = btnPlainIconAtm.toUiModel()
    )
}

@Preview
@Composable
fun FileUploadGroupOrgPreview() {
    val data = FileUploadGroupOrgData(
        title = UiText.DynamicString("title"),
        description = UiText.DynamicString("description"),
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("label"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
        )
    )

    FileUploadGroupOrg(data = data) {

    }

}

@Preview
@Composable
fun FileTitleOrgEmptyPreview() {
    val data = FileUploadGroupOrgData(
        title = UiText.DynamicString("Додайте файли"),
        description = UiText.DynamicString("Підтримуються PNG, JPG, HEIC формати файлів. Розмір файлу повинен бути не більше 100 Мб. Можна додати не більше 10 файлів."),
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("Додати файли"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
        ),
    )
    FileUploadGroupOrg(data = data) {}
}

@Preview
@Composable
fun FileTitleOrgPreview() {
    val data = FileUploadGroupOrgData(
        title = UiText.DynamicString("Додайте файли"),
        description = UiText.DynamicString("Підтримуються PNG, JPG, HEIC формати файлів. Розмір файлу повинен бути не більше 100 Мб. Можна додати не більше 10 файлів."),
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("Додати файли"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
        ),
        fileItems = SnapshotStateList<ListItemUploadMlcData>().apply {
            repeat(5) {
                add(
                    ListItemUploadMlcData(
                        id = it.toString(),
                        label = UiText.DynamicString("document_name_1.heic"),
                        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.DELETE.code)
                    )
                )
            }
        },
    )

    FileUploadGroupOrg(data = data) {}
}