package dev.saied.components.wireframe

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.saied.components.core.BasicTabsRow
import dev.saied.components.core.TabIndicatorScope

@Composable
fun TabRow(
    modifier: Modifier = Modifier,
    indicator: @Composable TabIndicatorScope.() -> Unit,
    divider: @Composable () -> Unit,
    tabs: @Composable () -> Unit
) {
    BasicTabsRow(
        modifier = Modifier,
        indicator = {},
        divider = {},
        tabs = {

        }
    )
}

object TabRowDefaults {

}