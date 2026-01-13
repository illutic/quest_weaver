package gr.questweaver.shared.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

object IosUiHelper {
    fun getRed(color: Color): Float = color.red
    fun getGreen(color: Color): Float = color.green
    fun getBlue(color: Color): Float = color.blue
    fun getAlpha(color: Color): Float = color.alpha

    fun getDpValue(dp: Dp): Float = dp.value
}
