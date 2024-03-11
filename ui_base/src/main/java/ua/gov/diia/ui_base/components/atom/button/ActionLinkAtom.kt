package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.IconBase64Subatomic
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun LoadActionAtom(
    modifier: Modifier = Modifier,
    data: LoadActionAtomData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable {
                if (data.interactionState == UIState.Interaction.Disabled ||
                    (progressIndicator.first.isNotEmpty() && progressIndicator.first == data.id && progressIndicator.second)
                ) return@noRippleClickable
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.actionData.orEmpty(),
                        optionalId = data.id
                    )
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(visible = progressIndicator.first == data.id && data.id.isNotEmpty() && progressIndicator.second) {
            LoaderCircularEclipse23Subatomic(modifier = Modifier.size(18.dp))
        }
        AnimatedVisibility(visible = progressIndicator.first != data.id || !progressIndicator.second || data.id.isEmpty()) {
            IconBase64Subatomic(modifier = Modifier.size(18.dp), base64Image = data.icon)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = data.name,
            style = DiiaTextStyle.t2TextDescription,
            color = Black
        )
    }
}

data class LoadActionAtomData(
    val actionKey: String = UIActionKeysCompose.LOAD_ACTION_ATOM,
    val id: String = "",
    val type: String,
    val icon: String,
    val name: String,
    val actionData: String? = null,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled
) : UIElementData

@Composable
@Preview
fun ActionLinkAtomPreview_Enabled() {
    val data = LoadActionAtomData(
        id = "id1",
        type = "Type1",
        icon = "iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABfSURBVHgB7ZBBDoAgDAQH40P9iU+vJ2/WLjSNJDIJJ0inAywGMeGcJDCKJfcART4kUQRHRqIIUCTtZYh374kf328Us9NPV9U0Bd6GjYBpCsJNs4Iff9HnBUaS8oJFyAWR1y49vMiceAAAAABJRU5ErkJggg==",
        name = "Постанова про арешт коштів",
        actionData = "www.google.com",
        interactionState = UIState.Interaction.Enabled
    )
    LoadActionAtom(modifier = Modifier.padding(16.dp), data = data) {
    }
}

@Composable
@Preview
fun ActionLinkAtomPreview_Disabled() {
    val data = LoadActionAtomData(
        id = "id1",
        type = "Type1",
        icon = "iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABfSURBVHgB7ZBBDoAgDAQH40P9iU+vJ2/WLjSNJDIJJ0inAywGMeGcJDCKJfcART4kUQRHRqIIUCTtZYh374kf328Us9NPV9U0Bd6GjYBpCsJNs4Iff9HnBUaS8oJFyAWR1y49vMiceAAAAABJRU5ErkJggg==",
        name = "Постанова про арешт коштів",
        actionData = "www.google.com",
        interactionState = UIState.Interaction.Disabled
    )
    LoadActionAtom(modifier = Modifier.padding(16.dp), data = data) {
    }
}

@Composable
@Preview
fun ActionLinkAtomPreview_Loading() {
    val data = LoadActionAtomData(
        id = "id1",
        type = "Type1",
        icon = "iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABfSURBVHgB7ZBBDoAgDAQH40P9iU+vJ2/WLjSNJDIJJ0inAywGMeGcJDCKJfcART4kUQRHRqIIUCTtZYh374kf328Us9NPV9U0Bd6GjYBpCsJNs4Iff9HnBUaS8oJFyAWR1y49vMiceAAAAABJRU5ErkJggg==",
        name = "Постанова 123344566435 від 12.03.2019 з дуже довгою назвою",
        actionData = "www.google.com",
        interactionState = UIState.Interaction.Enabled
    )
    LoadActionAtom(
        modifier = Modifier.padding(16.dp),
        data = data,
        progressIndicator = Pair("id1", true)
    ) {
    }
}
