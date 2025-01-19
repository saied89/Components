package dev.saied.components.wireframe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.saied.components.core.BasicSwitch
import dev.saied.components.wireframe.theme.ColorTokens
import kotlinx.coroutines.FlowPreview

// TODO: Add thump content
@Composable
fun Switch(
    checked: Boolean,
    onCheckedChanged: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    BasicSwitch(
        modifier = modifier
            .background(
                color = SwitchDefaults.trackColor(
                    checked = checked,
                    enabled = enabled
                ),
                shape = RoundedCornerShape(7.dp)
            )
            .size(width = 34.dp, height = 14.dp)
            .requiredSize(width = 34.dp, height = 20.dp),
        checked = checked,
        onCheckedChange = onCheckedChanged,
        thumb = {
            Box(
                modifier = Modifier
                    .requiredSize(20.dp)
                    .background(
                        SwitchDefaults.thumbColor(enabled = enabled, checked = checked),
                        shape = CircleShape
                    )
            )
        },
        enabled = enabled
    )
}

private object SwitchDefaults {
    @Stable
    fun trackColor(checked: Boolean, enabled: Boolean) =
        if (!checked && !enabled) ColorTokens.grey300
        else ColorTokens.grey400

    @Stable
    fun thumbColor(checked: Boolean, enabled: Boolean) =
        if (enabled) {
            if (checked) ColorTokens.grey900
            else ColorTokens.grey600
        } else {
            if (checked) ColorTokens.grey500
            else ColorTokens.grey400
        }

}