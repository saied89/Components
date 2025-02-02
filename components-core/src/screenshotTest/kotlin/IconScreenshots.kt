import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.saied.components.core.Icon

class IconScreenshots {
    @Preview
    @Composable
    fun ImageVectorIconScreenshot() {
        Icon(imageVector = Icons.Default.AccountBalance, null)
    }

    @Preview
    @Composable
    fun IconScreenshot() {
        Icon(imageVector = Icons.Default.AccountBalance, null, modifier = Modifier.size(45.dp))
    }
}