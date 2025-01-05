package dev.saied.components.wireframe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import dev.saied.components.core.ClickableSurface

internal val clickableSurface: ClickableSurface = { onClick, modifier, enabled, interactionSource, content ->
    Box(
        modifier = modifier.clickable(
            indication = ripple(),
            enabled = enabled,
            onClick = onClick,
            interactionSource = interactionSource
        ),

    )
}
