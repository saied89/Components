import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.saied.components.wireframe.Switch

class SwitchScreenshots {

    @Composable
    @Preview
    private fun SwitchPreview() {
        Column {
            val (checked, onCheckedChange) = remember { mutableStateOf(true) }
            Switch(
                checked = checked,
                onCheckedChanged = onCheckedChange,
                modifier = Modifier.padding(vertical = 6.dp)
            )

            Switch(
                checked = true,
                onCheckedChanged = null,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            Switch(
                checked = false,
                onCheckedChanged = null,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            Switch(
                checked = true,
                onCheckedChanged = null,
                enabled = false,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            Switch(
                checked = false,
                onCheckedChanged = null,
                enabled = false,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
    }
}