package ua.gov.diia.ui_base.components.molecule.message

import android.annotation.SuppressLint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import kotlin.math.roundToInt

const val ANIMATION_DURATION = 100
const val MIN_DRAG_AMOUNT = 6

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun DraggableMessageMolecule(
    data: MessageMoleculeData,
    isRevealed: Boolean,
    onUIAction: (UIAction) -> Unit,
    cardOffset: Float,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
) {
    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }
    val transition = updateTransition(transitionState, "cardTransition")

    val offsetTransition by transition.animateFloat(
        label = "cardOffsetTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) cardOffset else 0f },

        )
    val cardElevation by transition.animateDp(
        label = "cardElevation",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) 2.dp else 0.dp }
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .offset { IntOffset(offsetTransition.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    when {
                        dragAmount >= MIN_DRAG_AMOUNT -> onCollapse()
                        dragAmount < -MIN_DRAG_AMOUNT -> onExpand()
                    }
                }
            }
            .alpha(if (data.isRead == true) 0.5f else 1.0f)
            .clickable { onUIAction(UIAction(actionKey = data.actionKey, data = data.id)) }
            .background(Color.White, shape = RoundedCornerShape(16.dp)),
        elevation = elevatedCardElevation(cardElevation),
        content = {
            data.title?.let {
                Text(
                    text = it,
                    style = DiiaTextStyle.t2TextDescription,
                    color = Black,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                )
            }
            data.shortText?.let {
                Text(
                    text = it,
                    style = DiiaTextStyle.t1BigText,
                    color = Black,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                )
            }
            data.creationDate?.let {
                Text(
                    text = it,
                    style = DiiaTextStyle.t3TextBody,
                    color = BlackAlpha30,
                    modifier = Modifier.padding(
                        top = 8.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                )
            }
        }
    )
}

@Composable
@Preview
fun MessageMoleculePreviewUnreadD() {
    val state = MessageMoleculeData(
        id = "1",
        title = "Label",
        shortText = "Text text text text text text text text text text text text text text text text text text text text text text text text text text text text text text",
        creationDate = "Date and time"
    )
    MessageMolecule(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun MessageMoleculePreviewReadD() {
    val state = MessageMoleculeData(
        id = "1",
        title = "Label",
        shortText = "Text text text text text text text text text text text text text text text text text text text text text text text text text text text text text text",
        creationDate = "Date and time",
        isRead = true
    )
    MessageMolecule(modifier = Modifier, data = state) {}
}