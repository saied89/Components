package dev.saied.components.core

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFold
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastMap
import kotlinx.coroutines.launch

@Immutable
class TabPosition internal constructor(val left: Dp, val width: Dp, val contentWidth: Dp) {

    val right: Dp
        get() = left + width

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabPosition) return false

        if (left != other.left) return false
        if (width != other.width) return false
        if (contentWidth != other.contentWidth) return false

        return true
    }

    override fun hashCode(): Int {
        var result = left.hashCode()
        result = 31 * result + width.hashCode()
        result = 31 * result + contentWidth.hashCode()
        return result
    }

    override fun toString(): String {
        return "TabPosition(left=$left, right=$right, width=$width, contentWidth=$contentWidth)"
    }
}

interface TabIndicatorScope {

    fun Modifier.tabIndicatorLayout(
        measure:
        MeasureScope.(
            Measurable,
            Constraints,
            List<TabPosition>,
        ) -> MeasureResult
    ): Modifier

    /**
     * A Modifier that follows the default offset and animation
     *
     * @param selectedTabIndex the index of the current selected tab
     * @param matchContentSize this modifier can also animate the width of the indicator \ to match
     *   the content size of the tab.
     */
    fun Modifier.tabIndicatorOffset(
        selectedTabIndex: Int,
        matchContentSize: Boolean = false
    ): Modifier
}

internal interface TabPositionsHolder {

    fun setTabPositions(positions: List<TabPosition>)
}

@Composable
fun BasicTabsRow(
    modifier: Modifier,
    tabIndicatorAnimationSpec: FiniteAnimationSpec<Dp> = BasicTabsRowDefaults.tabIndicatorAnimationSpec,
    indicator: @Composable TabIndicatorScope.() -> Unit,
    divider: @Composable () -> Unit,
    tabs: @Composable () -> Unit
) {

    val scope = remember {
        object : TabIndicatorScope, TabPositionsHolder {

            val tabPositions = mutableStateOf<(List<TabPosition>)>(listOf())

            override fun Modifier.tabIndicatorLayout(
                measure:
                MeasureScope.(
                    Measurable,
                    Constraints,
                    List<TabPosition>,
                ) -> MeasureResult
            ): Modifier =
                this.layout { measurable: Measurable, constraints: Constraints ->
                    measure(
                        measurable,
                        constraints,
                        tabPositions.value,
                    )
                }

            override fun Modifier.tabIndicatorOffset(
                selectedTabIndex: Int,
                matchContentSize: Boolean
            ): Modifier =
                this.then(
                    TabIndicatorModifier(
                        tabPositions,
                        selectedTabIndex,
                        matchContentSize,
                        tabIndicatorAnimationSpec
                    )
                )

            override fun setTabPositions(positions: List<TabPosition>) {
                tabPositions.value = positions
            }
        }
    }

    Layout(
        modifier = modifier.fillMaxWidth(),
        contents =
            listOf(
                tabs,
                divider,
                { scope.indicator() },
            )
    ) { (tabMeasurables, dividerMeasurables, indicatorMeasurables), constraints ->
        val tabRowWidth = constraints.maxWidth
        val tabCount = tabMeasurables.size
        var tabWidth = 0
        if (tabCount > 0) {
            tabWidth = (tabRowWidth / tabCount)
        }
        val tabRowHeight =
            tabMeasurables.fastFold(initial = 0) { max, curr ->
                maxOf(curr.maxIntrinsicHeight(tabWidth), max)
            }

        scope.setTabPositions(
            List(tabCount) { index ->
                var contentWidth =
                    minOf(tabMeasurables[index].maxIntrinsicWidth(tabRowHeight), tabWidth)
                        .toDp()
                // TODO check how to generalize
                val HorizontalTextPadding = 16.dp
                contentWidth -= HorizontalTextPadding * 2
                // Enforce minimum touch target of 24.dp
                val indicatorWidth = maxOf(contentWidth, 24.dp)

                TabPosition(tabWidth.toDp() * index, tabWidth.toDp(), indicatorWidth)
            }
        )

        val tabPlaceables =
            tabMeasurables.fastMap {
                it.measure(
                    constraints.copy(
                        minWidth = tabWidth,
                        maxWidth = tabWidth,
                        minHeight = tabRowHeight,
                        maxHeight = tabRowHeight,
                    )
                )
            }

        val dividerPlaceables =
            dividerMeasurables.fastMap { it.measure(constraints.copy(minHeight = 0)) }

        val indicatorPlaceables =
            indicatorMeasurables.fastMap {
                it.measure(
                    constraints.copy(
                        minWidth = tabWidth,
                        maxWidth = tabWidth,
                        minHeight = 0,
                        maxHeight = tabRowHeight
                    )
                )
            }

        layout(tabRowWidth, tabRowHeight) {
            tabPlaceables.fastForEachIndexed { index, placeable ->
                placeable.placeRelative(index * tabWidth, 0)
            }

            dividerPlaceables.fastForEach { placeable ->
                placeable.placeRelative(0, tabRowHeight - placeable.height)
            }

            indicatorPlaceables.fastForEach { it.placeRelative(0, tabRowHeight - it.height) }
        }
    }
}

object BasicTabsRowDefaults {
    val tabIndicatorAnimationSpec = spring<Dp>(
        dampingRatio = 0.9f,
        stiffness = 700f
    )
}

internal data class TabIndicatorModifier(
    val tabPositionsState: State<List<TabPosition>>,
    val selectedTabIndex: Int,
    val followContentSize: Boolean,
    val animationSpec: FiniteAnimationSpec<Dp>
) : ModifierNodeElement<TabIndicatorOffsetNode>() {

    override fun create(): TabIndicatorOffsetNode {
        return TabIndicatorOffsetNode(
            tabPositionsState = tabPositionsState,
            selectedTabIndex = selectedTabIndex,
            followContentSize = followContentSize,
            animationSpec = animationSpec
        )
    }

    override fun update(node: TabIndicatorOffsetNode) {
        node.tabPositionsState = tabPositionsState
        node.selectedTabIndex = selectedTabIndex
        node.followContentSize = followContentSize
        node.animationSpec = animationSpec
    }

    override fun InspectorInfo.inspectableProperties() {
        // Show nothing in the inspector.
    }
}

internal class TabIndicatorOffsetNode(
    var tabPositionsState: State<List<TabPosition>>,
    var selectedTabIndex: Int,
    var followContentSize: Boolean,
    var animationSpec: FiniteAnimationSpec<Dp>
) : Modifier.Node(), LayoutModifierNode {

    private var offsetAnimatable: Animatable<Dp, AnimationVector1D>? = null
    private var widthAnimatable: Animatable<Dp, AnimationVector1D>? = null
    private var initialOffset: Dp? = null
    private var initialWidth: Dp? = null

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        if (tabPositionsState.value.isEmpty()) {
            return layout(0, 0) {}
        }

        val currentTabWidth =
            if (followContentSize) {
                tabPositionsState.value[selectedTabIndex].contentWidth
            } else {
                tabPositionsState.value[selectedTabIndex].width
            }

        if (initialWidth != null) {
            val widthAnim =
                widthAnimatable
                    ?: Animatable(initialWidth!!, Dp.VectorConverter).also { widthAnimatable = it }

            if (currentTabWidth != widthAnim.targetValue) {
                coroutineScope.launch { widthAnim.animateTo(currentTabWidth, animationSpec) }
            }
        } else {
            initialWidth = currentTabWidth
        }

        val indicatorOffset = tabPositionsState.value[selectedTabIndex].left

        if (initialOffset != null) {
            val offsetAnim =
                offsetAnimatable
                    ?: Animatable(initialOffset!!, Dp.VectorConverter).also {
                        offsetAnimatable = it
                    }

            if (indicatorOffset != offsetAnim.targetValue) {
                coroutineScope.launch { offsetAnim.animateTo(indicatorOffset, animationSpec) }
            }
        } else {
            initialOffset = indicatorOffset
        }

        val offset = offsetAnimatable?.value ?: indicatorOffset

        val width = widthAnimatable?.value ?: currentTabWidth

        val placeable =
            measurable.measure(
                constraints.copy(minWidth = width.roundToPx(), maxWidth = width.roundToPx())
            )

        return layout(placeable.width, placeable.height) { placeable.place(offset.roundToPx(), 0) }
    }
}
