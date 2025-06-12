package gr.questweaver.ui

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF151218,
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    backgroundColor = 0xFFFEF7FF,
)
annotation class PreviewLightDark

@Preview(
    name = "Pixel 5 Landscape",
    device = "spec:parent=pixel_5,orientation=landscape",
    showSystemUi = true,
    backgroundColor = 0xFF151218,
)
@Preview(
    name = "Pixel 5 Portrait",
    showSystemUi = true,
    backgroundColor = 0xFFFEF7FF,
)
annotation class PreviewInAllOrientations
