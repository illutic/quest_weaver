package gr.questweaver.shared.ui

import gr.questweaver.core.ui.ColorScheme
import gr.questweaver.core.ui.appDarkColorScheme
import gr.questweaver.core.ui.appLightColorScheme
import gr.questweaver.core.ui.defaultSizes

object SharedUi {
    val tokens = SharedTokens
}

object SharedTokens {
    val sizes = defaultSizes
    val lightColorScheme: SharedColorScheme = SharedColorScheme(appLightColorScheme)
    val darkColorScheme: SharedColorScheme = SharedColorScheme(appDarkColorScheme)
    val typography: SharedTypography = SharedTypography(gr.questweaver.core.ui.AppTypography)
}

class SharedTypography(typography: androidx.compose.material3.Typography) {
    val displayLarge =
        SharedTextStyle(
            fontSize = typography.displayLarge.fontSize.value,
            fontWeight = typography.displayLarge.fontWeight?.weight ?: 400,
            fontFamily = "Serif" // Hand-mapped from Type.kt
        )
    val headlineMedium =
        SharedTextStyle(
            fontSize = typography.headlineMedium.fontSize.value,
            fontWeight = typography.headlineMedium.fontWeight?.weight ?: 400,
            fontFamily = "Serif"
        )
    val titleMedium =
        SharedTextStyle(
            fontSize = typography.titleMedium.fontSize.value,
            fontWeight = typography.titleMedium.fontWeight?.weight ?: 400,
            fontFamily = "SansSerif"
        )
    val bodyLarge =
        SharedTextStyle(
            fontSize = typography.bodyLarge.fontSize.value,
            fontWeight = typography.bodyLarge.fontWeight?.weight ?: 400,
            fontFamily = "SansSerif"
        )
    val labelMedium =
        SharedTextStyle(
            fontSize = typography.labelMedium.fontSize.value,
            fontWeight = typography.labelMedium.fontWeight?.weight ?: 400,
            fontFamily = "SansSerif"
        )
}

data class SharedTextStyle(val fontSize: Float, val fontWeight: Int, val fontFamily: String)

class SharedColorScheme(
    materialColorScheme: ColorScheme,
) {
    val primary = materialColorScheme.primary
    val onPrimary = materialColorScheme.onPrimary
    val primaryContainer = materialColorScheme.primaryContainer
    val onPrimaryContainer = materialColorScheme.onPrimaryContainer
    val secondary = materialColorScheme.secondary
    val onSecondary = materialColorScheme.onSecondary
    val secondaryContainer = materialColorScheme.secondaryContainer
    val onSecondaryContainer = materialColorScheme.onSecondaryContainer
    val tertiary = materialColorScheme.tertiary
    val onTertiary = materialColorScheme.onTertiary
    val tertiaryContainer = materialColorScheme.tertiaryContainer
    val onTertiaryContainer = materialColorScheme.onTertiaryContainer
    val error = materialColorScheme.error
    val onError = materialColorScheme.onError
    val errorContainer = materialColorScheme.errorContainer
    val onErrorContainer = materialColorScheme.onErrorContainer
    val background = materialColorScheme.background
    val onBackground = materialColorScheme.onBackground
    val surface = materialColorScheme.surface
    val onSurface = materialColorScheme.onSurface
    val surfaceVariant = materialColorScheme.surfaceVariant
    val onSurfaceVariant = materialColorScheme.onSurfaceVariant
    val outline = materialColorScheme.outline
    val inverseOnSurface = materialColorScheme.inverseOnSurface
    val inverseSurface = materialColorScheme.inverseSurface
    val inversePrimary = materialColorScheme.inversePrimary
}
