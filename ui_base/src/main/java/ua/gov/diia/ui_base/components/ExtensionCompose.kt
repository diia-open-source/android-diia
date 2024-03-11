package ua.gov.diia.ui_base.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import ua.gov.diia.ui_base.components.infrastructure.state.UIState

inline fun Modifier.conditional(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick
    )
}

fun Modifier.disableByInteractionState(state: UIState.Interaction): Modifier {
    return if (state == UIState.Interaction.Disabled) {
        this
            .alpha(0.3f)
            .clickable(enabled = false, onClick = {})
    } else {
        this
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.provideTestTagsAsResourceId() : Modifier {
    return this.semantics { testTagsAsResourceId = true }
}