package dev.saied.components.core

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

//@Composable
//inline fun BaseButton(
//    noinline onClick: () -> Unit,
//    modifier: Modifier = Modifier,
//    enabled: Boolean = true,
//    interactionSource: MutableInteractionSource? = null,
//    background: ClickableSurface,
//    noinline content: @Composable () -> Unit
//) {
//    @Suppress("NAME_SHADOWING")
//    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
//    background(onClick, modifier, enabled, interactionSource, content)
//}
