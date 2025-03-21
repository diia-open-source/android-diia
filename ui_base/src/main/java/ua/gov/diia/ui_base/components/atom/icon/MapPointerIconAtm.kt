package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.state.UIState


@Composable
fun MapPointerIconAtm(
    modifier: Modifier = Modifier,
    expand: UIState.Expand,
) {
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val offset = animateDpAsState(
            targetValue = if (expand == UIState.Expand.Expanded) (-6).dp else 4.dp,
            animationSpec = tween(durationMillis = 400),
            label = "animateDpAsState map pointer"
        )
        Image(
            modifier = modifier.offset(y = offset.value),
            painter = painterResource(id = R.drawable.ic_map_pointer_top),
            contentDescription = ""
        )
        val scale = animateFloatAsState(
            targetValue = if (expand == UIState.Expand.Expanded) 1.4f else 1f,
            animationSpec = tween(durationMillis = 400),
            label = "animateFloatAsState map pointer"
        )
        Image(
            modifier = modifier.scale(scale.value),
            painter = painterResource(id = R.drawable.ic_map_pointer_bottom),
            contentDescription = ""
        )
    }
}


@Composable
@Preview
fun MapPointerIconAtmPreview() {
    MapPointerIconAtm(expand = UIState.Expand.Expanded)
}

@Composable
@Preview
fun MapPointerIconAtmPreview_Collapsed() {
    MapPointerIconAtm(expand = UIState.Expand.Collapsed)
}

@Composable
@Preview
fun MapPointerIconAtmPreview_Animated() {
    val coroutineScope = rememberCoroutineScope()
    val state = remember {
        mutableStateOf<UIState.Expand>(UIState.Expand.Collapsed)
    }
    MapPointerIconAtm(expand = state.value)
    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            reverseState(state)
        }
    }
}

private suspend fun reverseState(state: MutableState<UIState.Expand>) {
    state.value = state.value.reverse()
    delay(2000)
    reverseState(state)
}