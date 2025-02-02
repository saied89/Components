package dev.saied.components.wireframe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.saied.components.core.BasicTabsRow
import dev.saied.components.core.LocalContentColor
import dev.saied.components.core.LocalTextStyle
import dev.saied.components.core.TabIndicatorScope
import dev.saied.components.core.Text
import dev.saied.components.wireframe.theme.ColorTokens

@Composable
fun TabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    indicator: @Composable TabIndicatorScope.() -> Unit = @Composable {
        TabRowDefaults.Indicator(
            Modifier.tabIndicatorOffset(selectedTabIndex, matchContentSize = false)
        )
    },
    divider: @Composable () -> Unit = { TabRowDefaults.Divider() },
    tabs: @Composable () -> Unit
) {
    BasicTabsRow(
        modifier = modifier.background(Color.White),
        indicator = indicator,
        divider = divider,
        tabs = {
            CompositionLocalProvider(
                LocalContentColor provides ColorTokens.grey900,
                LocalTextStyle provides TextStyle(fontWeight = FontWeight.Medium),
                content = tabs
            )
        }
    )
}

object TabRowDefaults {
    @Composable
    fun Divider(
        modifier: Modifier = Modifier,
        thickness: Dp = 1.dp,
        color: Color = ColorTokens.grey900,
    ) {
        Box(
            modifier = modifier
                .background(color)
                .fillMaxWidth()
                .height(thickness)
        )
    }

    @Composable
    fun Indicator(
        modifier: Modifier = Modifier,
        height: Dp = 2.dp,
        color: Color = ColorTokens.grey900
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .height(height)
                .background(color = color)
        )
    }
}

@Preview
@Composable
private fun TabRowPreview() {
    var state by remember { mutableStateOf(0) }
    val titles = listOf("TAB 1", "TAB 2", "TAB 3")
    TabRow(state) {
        titles.forEachIndexed { index, title ->
            Tab(
                content = { Text(title) },
                selected = state == index,
                onClick = { state = index })
        }
    }
}