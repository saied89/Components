package dev.saied.components.core

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

typealias Surface = @Composable (
    modifier: Modifier,
    enabled: Boolean,
    interactionSource: InteractionSource,
    contentColor: ColorProducer,
    content: @Composable () -> Unit
) -> Unit

typealias ClickableSurface = @Composable (
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
    color: ColorProducer,
    content: @Composable () -> Unit
) -> Unit

// TODO Extend for multiple color states
// Possible Multicolor extension type
//abstract class StateColors<S : Enum<S>> {
//    abstract operator fun get(key: S): StateColor
//}
//data class SurfaceIndication(
//    private val stateDrawer: StateDrawer,
//) : IndicationNodeFactory {
//
//    constructor(stateColors: StateColors) : this(StateColorStateDrawer(stateColors))
//
//    override fun create(interactionSource: InteractionSource): DelegatableNode =
//        SurfaceIndicationNode(stateDrawer, interactionSource)
//}

//private class SurfaceIndicationNode(
//    private val stateDrawer: StateDrawer,
//    private val interactionSource: InteractionSource
//) : Modifier.Node(), DrawModifierNode, CompositionLocalConsumerModifierNode,
//    StateDrawer by stateDrawer {
//
//    private var pressed by mutableStateOf(false)
//    override fun onAttach() {
//        interactionSource.interactions.onEach {
//            when (it) {
//                is PressInteraction.Press -> {
//                    pressed = true
//                }
//
//                is PressInteraction.Cancel, is PressInteraction.Release -> {
//                    pressed = false
//                }
//            }
//        }.launchIn(coroutineScope)
//    }
//
//    override fun ContentDrawScope.draw() {
//        val isEnabled = LocalEnabled.current
//        draw(isEnabled, pressed)
//    }
//}

val LocalEnabled = compositionLocalOf { false }


fun interface StateDrawer {
    fun ContentDrawScope.draw(enabled: Boolean, pressed: Boolean)
}

class StateColorStateDrawer(private val stateColors: StateColors) : StateDrawer {
    override fun ContentDrawScope.draw(
        enabled: Boolean,
        pressed: Boolean
    ) {
        if (!enabled)
            drawRect(color = stateColors.disabled)
        else if (pressed)
            drawRect(color = stateColors.pressed)
        else
            drawRect(color = stateColors.default)
    }
}

data class StateColors(val default: Color, val pressed: Color, val disabled: Color)

//Experiment Zone *****************

class StateColorProducer(
    val default: Color,
    val pressed: Color,
    val disabled: Color,
    val isPressed: () -> Boolean,
    val isEnabled: () -> Boolean
) : ColorProducer {
    override fun invoke(): Color {
        val isPressed = isPressed()
        val isEnabled = isEnabled()
        return if (!isEnabled)
            disabled
        else if (isPressed)
            pressed
        else
            default
    }
}

// *******************


