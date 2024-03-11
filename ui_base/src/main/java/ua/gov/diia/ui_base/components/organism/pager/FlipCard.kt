package ua.gov.diia.ui_base.components.organism.pager

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.noRippleClickable

@Composable
fun FlipCard(
    modifier: Modifier = Modifier,
    data: FlipCardData,
    onUIAction: (UIAction) -> Unit
) {
    var angle by remember {
        mutableStateOf(0f)
    }
    val rotation = animateFloatAsState(
        targetValue = angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        )
    )

    LaunchedEffect(key1 = data.cardFace, key2 = data.enableFlip) {
        if (!(data.cardFace == CardFace.Front && angle == 0f)) {
            angle += 180f
        }
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        modifier = modifier
            .noRippleClickable {
                if (data.enableFlip) {
                    onUIAction(UIAction(actionKey = UIActionKeysCompose.DOC_CARD_FLIP))
                }
            }
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
            }
    ) {
        if ((rotation.value % 360) <= 90f || (rotation.value % 360) >= 270f) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                data.front()
            }
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .graphicsLayer {
                        rotationY = 180f
                    },
            ) {
                data.back()
            }
        }
    }
}

data class FlipCardData(
    val back: @Composable () -> Unit = {},
    val front: @Composable () -> Unit = {},
    var cardFace: CardFace = CardFace.Front,
    val enableFlip: Boolean = true
)

@Preview
@Composable
fun FlipCardPreview() {
    var cardFace by remember {
        mutableStateOf(CardFace.Front)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        BackgroundAnimation()
        FlipCard(
            modifier = Modifier
                .background(Color.Transparent)
                .size(width = 200.dp, height = 400.dp),
            data = FlipCardData(
                front = {
                    Box(
                        modifier = Modifier
                            .background(Color.Transparent)
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
                            .background(Color.Transparent)
                            .size(width = 250.dp, height = 400.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Back side",
                            color = Color.White
                        )
                    }
                },
                cardFace = CardFace.Front
            )
        ) {}
    }
}

enum class CardFace {
    Front {
        override val next: CardFace
            get() = Back
    },
    Back {
        override val next: CardFace
            get() = Front
    };

    abstract val next: CardFace
}

@Composable
fun BackgroundAnimation() {
    val animatableX = remember { Animatable(0f) }
    val animatableY = remember { Animatable(0f) }

    LaunchedEffect(true) {
        animatableX.animateTo(
            targetValue = 1f,
            animationSpec = repeatable(
                iterations = Int.MAX_VALUE,
                animation = tween(durationMillis = 3000, easing = LinearEasing)
            )
        )
        animatableY.animateTo(
            targetValue = 1f,
            animationSpec = repeatable(
                iterations = Int.MAX_VALUE,
                animation = tween(durationMillis = 3000, easing = LinearEasing)
            )
        )
    }

    val offsetX = (animatableX.value * (1000)).coerceIn(0f, 940f)
    val offsetY = (animatableY.value * (100)).coerceIn(0f, 300f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        Box(
            modifier = Modifier
                .offset(x = offsetX.dp, y = offsetY.dp)
                .size(60.dp)
                .graphicsLayer {
                    translationX = 0f
                    translationY = 850f
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.Green, Color.Transparent),
                        radius = 30f,
                        center = Offset(50f, 50f)
                    )
                )
        )
    }
}

@Preview
@Composable
fun BackgroundAnimationPreview() {
    BackgroundAnimation()
}