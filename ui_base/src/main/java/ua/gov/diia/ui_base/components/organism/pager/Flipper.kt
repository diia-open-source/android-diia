package ua.gov.diia.ui_base.components.organism.pager

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.GullGray
import ua.gov.diia.ui_base.components.theme.SystemColor

@Composable
fun Flipper(
    modifier: Modifier = Modifier,
    data: FlipCardData,
    onUIAction: (UIAction) -> Unit
) {
    val currentSide = remember {
        mutableStateOf(data.cardFace)
    }
    var angleA by remember {
        mutableFloatStateOf(0f)
    }
    var visibilityA by remember {
        mutableStateOf(true)
    }
    var angleB by remember {
        mutableFloatStateOf(-90f)
    }
    var visibilityB by remember {
        mutableStateOf(false)
    }

    val rotationA = animateFloatAsState(
        targetValue = angleA,
        animationSpec = tween(
            durationMillis = 200,
            delayMillis = if (visibilityA) 0 else 200,
            easing = FastOutSlowInEasing,
        ),
        finishedListener = {
            if (it != 0f && visibilityA) {
                visibilityA = false
                angleA = -90f
                visibilityB = true
            }
        }
    )

    val rotationB = animateFloatAsState(
        targetValue = angleB,
        animationSpec = tween(
            durationMillis = 200,
            delayMillis = if (visibilityA) 200 else 0,
            easing = FastOutSlowInEasing,
        ),
        finishedListener = {
            if (it != 0f && visibilityB) {
                visibilityB = false
                angleB = -90f
                visibilityA = true
            }
        }
    )

    LaunchedEffect(key1 = data.cardFace) {
        if (currentSide.value != data.cardFace) {
            if (data.cardFace == CardFace.Back) {
                angleA = 90f
                angleB = 0f
            } else {
                angleA = 0f
                angleB = 90f
            }
            currentSide.value = data.cardFace
        }

    }
    Box(modifier = modifier
        .noRippleClickable {
            if (data.enableFlip) {
                onUIAction(UIAction(actionKey = UIActionKeysCompose.DOC_CARD_FLIP))
            }
        }
    ) {
        if (visibilityA) {
            Box(
                Modifier.graphicsLayer {
                    rotationY = rotationA.value
                    cameraDistance = 12f * density
                }
            ) {
                data.front()
            }
        }
        if (visibilityB) {
            Box(
                Modifier.graphicsLayer {
                    rotationY = rotationB.value
                    cameraDistance = 12f * density
                },
            ) {
                data.back()
            }
        }
    }
}

@Preview
@Composable
fun NewFlipperPreview() {
    var cardFaceHolder by remember {
        mutableStateOf(CardFace.Front)
    }
        Flipper(
            modifier = Modifier
                .background(Color.Transparent)
                .size(width = 200.dp, height = 400.dp),
            data = FlipCardData(
                front = {
                    Box(
                        modifier = Modifier
                            .background(GullGray)
                            .size(width = 250.dp, height = 400.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Front side",
                            color = Color.Black
                        )
                    }
                },
                back = {
                    Box(
                        modifier = Modifier
                            .background(SystemColor)
                            .size(width = 250.dp, height = 400.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Back side",
                            color = Color.White
                        )
                    }
                },
                cardFace = cardFaceHolder
            )
        ) {
            cardFaceHolder = when (cardFaceHolder) {
                CardFace.Front -> CardFace.Back
                CardFace.Back -> CardFace.Front
            }
        }
}