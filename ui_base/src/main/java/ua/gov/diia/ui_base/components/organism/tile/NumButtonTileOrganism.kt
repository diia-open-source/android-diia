package ua.gov.diia.ui_base.components.organism.tile

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.PIN_CLEARED_NUM_BUTTON_ORGANISM
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.PIN_CREATED_NUM_BUTTON_ORGANISM
import ua.gov.diia.ui_base.components.molecule.input.tile.NumButtonTileMlc
import ua.gov.diia.ui_base.components.molecule.progress.EllipseStepperMolecule
import ua.gov.diia.ui_base.components.molecule.progress.EllipseStepperMoleculeData

@Composable
fun NumButtonTileOrganism(
    modifier: Modifier = Modifier,
    data: NumButtonTileOrganismData,
    onUIAction: (UIAction) -> Unit
) {
    val userPinInput = remember { mutableStateOf("") }

    LaunchedEffect(key1 = data.clearPin) {
        if (data.clearPin) {
            userPinInput.value = ""
            onUIAction.invoke(UIAction(PIN_CLEARED_NUM_BUTTON_ORGANISM, userPinInput.value))
        }
    }

    LaunchedEffect(userPinInput.value) {
        if (userPinInput.value.length == data.pinLength) {
            onUIAction(UIAction(PIN_CREATED_NUM_BUTTON_ORGANISM, userPinInput.value))
        }
    }

    Column(
        modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EllipseStepperMolecule(
            modifier = Modifier
                .fillMaxWidth()
                .shake(enabled = data.clearWithShake) {
                    userPinInput.value = ""
                    onUIAction.invoke(UIAction(PIN_CLEARED_NUM_BUTTON_ORGANISM, userPinInput.value))
                },
            data = EllipseStepperMoleculeData(data.pinLength, userPinInput.value.length)
        )
        Spacer(modifier = Modifier.size(32.dp))
        NumButtonTileMlc(
            hasBiometric = data.hasBiometric,
            onUIAction = {
                when (it.actionKey) {
                    UIActionKeysCompose.NUM_BUTTON -> {
                        val number = it.data ?: return@NumButtonTileMlc
                        val currentPin = userPinInput.value
                        if (currentPin.length >= data.pinLength) return@NumButtonTileMlc
                        userPinInput.value = (currentPin + number)
                    }

                    UIActionKeysCompose.NUM_BUTTON_REMOVE -> {
                        val currentPin = userPinInput.value
                        if (currentPin.isEmpty()) return@NumButtonTileMlc
                        userPinInput.value = currentPin.dropLast(1)
                    }

                    else -> {
                        onUIAction.invoke(it)
                    }
                }
            })
    }
}


fun Modifier.shake(enabled: Boolean, finishedListener: ((Float) -> Unit)) = composed(
    factory = {
        val translation = animateFloatAsState(
            targetValue = if (enabled) 25f else 0f,
            animationSpec = repeatable(
                iterations = 4,
                animation = tween(durationMillis = 100, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            finishedListener = finishedListener,
            label = ""
        )

        Modifier.graphicsLayer {
            translationX = if (enabled) translation.value else 0f
        }
    },
    inspectorInfo = debugInspectorInfo {
        name = "translation"
        properties["enabled"] = enabled
    }
)

data class NumButtonTileOrganismData(
    val actionKey: String = UIActionKeysCompose.NUM_BUTTON,
    val pinLength: Int = 4,
    val hasBiometric: Boolean = false,
    val clearPin: Boolean = false,
    val clearWithShake: Boolean = false,
) : UIElementData

@Composable
@Preview
fun NumButtonTileOrganismPreview() {
    val data = remember {
        NumButtonTileOrganismData(
            pinLength = 4,
            clearWithShake = false,
            hasBiometric = true
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        NumButtonTileOrganism(data = data) {}
    }
}

