package dev.saied.components.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toolingGraphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Icon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Icon(
        painter = rememberVectorPainter(imageVector),
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint
    )
}

// Default Value from Material
private val DefaultIconSize = 24.dp

@Composable
fun Icon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    fallbackIconSize: Dp = DefaultIconSize,
    tint: Color = LocalContentColor.current
) {
    val colorFilter =
        remember(tint) { if (tint == Color.Unspecified) null else ColorFilter.tint(tint) }
    val semantics =
        if (contentDescription != null) {
            Modifier.semantics {
                this.contentDescription = contentDescription
                this.role = Role.Image
            }
        } else {
            Modifier
        }
    Box(
        modifier
            .toolingGraphicsLayer()
            .defaultSizeFor(painter, fallbackIconSize)
            .paint(painter, colorFilter = colorFilter, contentScale = ContentScale.Fit)
            .then(semantics)
    )
}

@Composable
fun Icon(
    painter: Painter,
    tint: ColorProducer?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    fallbackIconSize: Dp = DefaultIconSize
) {
    val semantics =
        if (contentDescription != null) {
            Modifier.semantics {
                this.contentDescription = contentDescription
                this.role = Role.Image
            }
        } else {
            Modifier
        }

    Box(
        modifier
            .toolingGraphicsLayer()
            .defaultSizeForColorProducer(painter, fallbackIconSize)
            .drawBehind {
                with(painter) {
                    draw(size = size, colorFilter = tint?.invoke()?.let { ColorFilter.tint(it) })
                }
            }
            .then(semantics)
    )
}

private fun Modifier.defaultSizeForColorProducer(painter: Painter, defaultSize: Dp) =
    this.then(
        if (painter.intrinsicSize == Size.Unspecified || painter.intrinsicSize.isInfinite()) {
            Modifier.size(defaultSize)
        } else {
            val intrinsicSize = painter.intrinsicSize
            val srcWidth = intrinsicSize.width

            val srcHeight = intrinsicSize.height

            Modifier.layout { measurable, _ ->
                val placeable =
                    measurable.measure(Constraints.fixed(srcWidth.toInt(), srcHeight.toInt()))
                layout(placeable.width, placeable.height) { placeable.place(0, 0) }
            }
        }
    )

private fun Modifier.defaultSizeFor(painter: Painter, defaultSize: Dp) =
    this.then(
        if (painter.intrinsicSize == Size.Unspecified || painter.intrinsicSize.isInfinite()) {
            Modifier.size(defaultSize)
        } else {
            Modifier
        }
    )

private fun Size.isInfinite() = width.isInfinite() && height.isInfinite()
