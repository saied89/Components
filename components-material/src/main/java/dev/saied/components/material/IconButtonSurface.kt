package dev.saied.components.material

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

//@Composable
//fun IconButtonSurface(
//    onClick: () -> Unit,
//    modifier: Modifier,
//    enabled: Boolean,
//    shape: Shape,
//    interactionSource: MutableInteractionSource,
//    content: @Composable () -> Unit
//) {
//    Box(
//        modifier =
//            modifier
//                .size(24.dp)
////                .clip(shape)
////                .background(color = colors.containerColor(enabled), shape = shape)
//                .clickable(
//                    onClick = onClick,
//                    enabled = enabled,
//                    role = Role.Button,
//                    interactionSource = interactionSource,
//                    indication = ripple()
//                )
//                .childSemantics()
//                .interactionSourceData(interactionSource),
//        contentAlignment = Alignment.Center
//    ) {
//
//    }
//}