package dev.saied.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BaseButton(
    background: Surface2,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    background(content, modifier)
}

@Composable
fun WireframeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    BaseButton(
        background = WireframeButtonDefaults.surface(
            WireframeButtonDefaults.defaultColors,
            interactionSource
        ),
        modifier = modifier.clickable(enabled = true, onClick = onClick, interactionSource = interactionSource, indication = null),
        content = content
    )
}

@Preview
@Composable
fun Surface2PG() {
    WireframeButton(onClick = {}) {
        BasicText("Saeed")
    }
}

fun interface Surface2 {
    @Composable
    operator fun invoke(content: @Composable () -> Unit, modifier: Modifier)
}

val LocalIsEnabled = compositionLocalOf { true }

object WireframeButtonDefaults {

    @Composable
    fun surface(
        colors: Colors,
        interactionSource: InteractionSource
    ): Surface2 = Surface2 { content, modifier ->
        val isPressed by interactionSource.collectIsPressedAsState()
        val isEnabled = LocalIsEnabled.current

        val color = if (!isEnabled)
            colors.color.disabled
        else if (isPressed)
            colors.color.pressed
        else
            colors.color.default

        Box(modifier.background(color)) {
            content()
        }
    }

    val defaultColors = Colors(
        color = StateColors(
            default = Color.Transparent,
            pressed = Color(0xFFD8CDCD),
            disabled = Color.Transparent
        ),
        borderColor = StateColors(
            default = Color.Transparent,
            pressed = Color.Transparent,
            disabled = Color.Transparent
        ),
        contentColor = StateColors(
            default = Color(0xFF020202),
            pressed = Color(0xFF020202),
            disabled = Color(0xFFBDBDBD)
        ),
    )

    data class Colors(
        val color: StateColors,
        val borderColor: StateColors,
        val contentColor: StateColors
    )

    data class StateColors(val default: Color, val pressed: Color, val disabled: Color)
}

