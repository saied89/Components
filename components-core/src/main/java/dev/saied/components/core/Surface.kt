package dev.saied.components.core

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

typealias Surface = @Composable (
    modifier: Modifier,
    color: Color,
    contentColor: Color,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
    content: @Composable () -> Unit
) -> Unit

typealias ClickableSurface = @Composable (
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
    content: @Composable () -> Unit
) -> Unit

// TODO Extend for multiple color states
// Possible Multicolor extension type
//abstract class StateColors<S : Enum<S>> {
//    abstract operator fun get(key: S): StateColor
//}
private data class SurfaceIndicationFactory(
    private val stateColor: StateColor,
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode =
        SurfaceIndicationNode(stateColor, interactionSource)
}

internal class SurfaceIndicationNode(
    private val stateColor: StateColor,
    private val interactionSource: InteractionSource
) : Modifier.Node(), DrawModifierNode, CompositionLocalConsumerModifierNode {

    private var pressed by mutableStateOf(false)
    override fun onAttach() {
        interactionSource.interactions.onEach {
            when (it) {
                is PressInteraction.Press -> {
                    pressed = true
                }

                is PressInteraction.Cancel, is PressInteraction.Release -> {
                    pressed = false
                }
            }
        }.launchIn(coroutineScope)
    }

    override fun ContentDrawScope.draw() {
        val isEnabled = LocalEnabled.current
        if (!isEnabled)
        // disabled
            drawRect(stateColor.disabled)
        else if (pressed)
        // pressed
            drawRect(stateColor.pressed)
        else
        // default
            drawRect(stateColor.default)
    }
}

val LocalEnabled = compositionLocalOf { false }

class StateColor(val default: Color, val disabled: Color, val pressed: Color)


