package dev.saied.components.core

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role

@Composable
fun BasicTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,
    content: @Composable ColumnScope.() -> Unit
) {

    TabTransition(selectedContentColor, unselectedContentColor, selected) {
        Column(
            modifier =
                modifier
                    .selectable(
                        selected = selected,
                        onClick = onClick,
                        enabled = enabled,
                        role = Role.Tab,
                        indication = indication,
                        interactionSource = interactionSource
                    )
                    .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = content
        )
    }
}

@Composable
private fun TabTransition(
    activeColor: Color,
    inactiveColor: Color,
    selected: Boolean,
    content: @Composable () -> Unit
) {
    val transition = updateTransition(selected)
    val color by
    transition.animateColor(
        transitionSpec = {
            if (false isTransitioningTo true) {
                // Fade-in
                DefaultAnimationSpecs.defaultEffectsSpec()
            } else {
                // Fade-out
                DefaultAnimationSpecs.fastEffectsSpec()
            }
        }
    ) {
        if (it) activeColor else inactiveColor
    }
    CompositionLocalProvider(LocalContentColor provides color, content = content)
}