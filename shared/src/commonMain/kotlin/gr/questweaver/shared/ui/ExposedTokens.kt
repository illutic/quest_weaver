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
}

class SharedColorScheme(materialColorScheme: ColorScheme) {
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

