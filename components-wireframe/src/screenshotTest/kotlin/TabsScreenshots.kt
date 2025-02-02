import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.saied.components.core.Text
import dev.saied.components.wireframe.Tab
import dev.saied.components.wireframe.TabRow

class TabsScreenshots {
    @Preview()
    @Composable
    fun TabRowPreview(selectedIndex: Int = 0) {
        val titles = listOf("TAB 1", "TAB 2", "TAB 3")
        TabRow(selectedIndex) {
            titles.forEachIndexed { index, title ->
                Tab(
                    content = { Text(title) },
                    selected = selectedIndex == index,
                    onClick = { }
                )
            }
        }
    }
}
