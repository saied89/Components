package dev.saied.components.core

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.ExperimentalAnimatableApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateMeasurement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun BasicSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    thumb: @Composable () -> Unit,
    enabled: Boolean = true,
    animationSpec: FiniteAnimationSpec<Float> = BasicSwitchDefaults.animationSpec,
    interactionSource: MutableInteractionSource? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val toggleableModifier =
        if (onCheckedChange != null) {
            modifier
                .toggleable(
                    value = checked,
                    onValueChange = onCheckedChange,
                    enabled = enabled,
                    role = Role.Switch,
                    interactionSource = interactionSource,
                    indication = null
                )
        } else {
            modifier
        }

    Box(toggleableModifier) {
        Box(
            modifier =
                Modifier
                    .align(Alignment.CenterStart)
                    .then(
                        ThumbElement(
                            interactionSource = interactionSource,
                            checked = checked,
                            animationSpec = animationSpec
                        )
                    ),
            contentAlignment = Alignment.Center
        ) { thumb() }
    }
}

object BasicSwitchDefaults {
    val animationSpec: FiniteAnimationSpec<Float> =
        spring<Float>(dampingRatio = 0.9f, stiffness = 1400.0f)
}

@Preview
@Composable
fun Switch2Preview() {
    var checked by remember { mutableStateOf(false) }
    BasicSwitch(
        checked,
        { checked = it },
        modifier = Modifier
            .size(90.dp, 60.dp)
            .background(Color.Yellow),
        thumb = {
            Spacer(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.Cyan, shape = CircleShape)
            )
        }
    )
}

private data class ThumbElement(
    val interactionSource: InteractionSource,
    val checked: Boolean,
    val animationSpec: FiniteAnimationSpec<Float>,
) : ModifierNodeElement<ThumbNode>() {
    override fun create() = ThumbNode(interactionSource, checked, animationSpec)

    override fun update(node: ThumbNode) {
        node.interactionSource = interactionSource
        if (node.checked != checked) {
            node.invalidateMeasurement()
        }
        node.checked = checked
        node.animationSpec = animationSpec
        node.update()
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "switchThumb"
        properties["interactionSource"] = interactionSource
        properties["checked"] = checked
        properties["animationSpec"] = animationSpec
    }
}

private class ThumbNode(
    var interactionSource: InteractionSource,
    var checked: Boolean,
    var animationSpec: FiniteAnimationSpec<Float>,
) : Modifier.Node(), LayoutModifierNode {

    override val shouldAutoInvalidate: Boolean
        get() = false

    private var isPressed = false
    private var offsetAnim: Animatable<Float, AnimationVector1D>? = null
    private var initialOffset: Float = Float.NaN

    override fun onAttach() {
        coroutineScope.launch {
            var pressCount = 0
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> pressCount++
                    is PressInteraction.Release -> pressCount--
                    is PressInteraction.Cancel -> pressCount--
                }
                val pressed = pressCount > 0
                if (isPressed != pressed) {
                    isPressed = pressed
                    invalidateMeasurement()
                }
            }
        }
    }

    @OptIn(ExperimentalAnimatableApi::class)
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val width = measurable.maxIntrinsicWidth(constraints.maxHeight)
        val height = measurable.maxIntrinsicWidth(constraints.maxWidth)

        val travel = constraints.maxWidth - width
        val anim: Animatable<Float, AnimationVector1D> = offsetAnim ?: run {
            initialOffset = if (checked) travel.toFloat() else 0f
            Animatable(initialOffset)
        }.also { offsetAnim = it }

        coroutineScope.launch {
            if (checked)
                anim.animateTo(travel.toFloat(), animationSpec)
            else (anim.animateTo(0f, animationSpec))
        }

        val placeable = measurable.measure(constraints)

        return layout(width, height) {
            placeable.placeRelative(anim.value.toInt(), 0)
        }
    }

    fun update() {
        if (offsetAnim == null && !initialOffset.isNaN()) offsetAnim = Animatable(initialOffset)
    }
}