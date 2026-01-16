package gr.questweaver.core.ui

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Light Theme - Parchment & Crimson
internal val primaryLight = Color(0xFF8B0000) // Deep Crimson
internal val onPrimaryLight = Color(0xFFFFFFFF)
internal val primaryContainerLight = Color(0xFFFFDAD6)
internal val onPrimaryContainerLight = Color(0xFF410002)
internal val secondaryLight = Color(0xFFB08C05) // Antique Gold
internal val onSecondaryLight = Color(0xFFFFFFFF)
internal val secondaryContainerLight = Color(0xFFFFE085)
internal val onSecondaryContainerLight = Color(0xFF241A00)

internal val tertiaryLight = Color(0xFF4A6546) // Forest Green (Ranger env)
internal val onTertiaryLight = Color(0xFFFFFFFF)
internal val tertiaryContainerLight = Color(0xFFCCEBC4)
internal val onTertiaryContainerLight = Color(0xFF072106)

internal val errorLight = Color(0xFFBA1A1A)
internal val onErrorLight = Color(0xFFFFFFFF)
internal val errorContainerLight = Color(0xFFFFDAD6)
internal val onErrorContainerLight = Color(0xFF410002)

internal val backgroundLight = Color(0xFFF5E6D3) // Parchment
internal val onBackgroundLight = Color(0xFF1F1B16)
internal val surfaceLight = Color(0xFFF5E6D3) // Parchment
internal val onSurfaceLight = Color(0xFF1F1B16)
internal val surfaceVariantLight = Color(0xFFEBE1CF)
internal val onSurfaceVariantLight = Color(0xFF4D4639)
internal val outlineLight = Color(0xFF7F7667)
internal val outlineVariantLight = Color(0xFFD0C5B4)

// Standard mappings
internal val scrimLight = Color(0xFF000000)
internal val inverseSurfaceLight = Color(0xFF34302A)
internal val inverseOnSurfaceLight = Color(0xFFF9EFE7)
internal val inversePrimaryLight = Color(0xFFFFB4AB)
internal val surfaceDimLight = Color(0xFFE2D8CC)
internal val surfaceBrightLight = Color(0xFFFFF8F3)
internal val surfaceContainerLowestLight = Color(0xFFFFFFFF)
internal val surfaceContainerLowLight = Color(0xFFF9EFE7)
internal val surfaceContainerLight = Color(0xFFF3E9DD)
internal val surfaceContainerHighLight = Color(0xFFEDE4D7)
internal val surfaceContainerHighestLight = Color(0xFFE7DEC1)

// Dark Theme - Obsidian & Ember
internal val primaryDark = Color(0xFFFFB4AB) // Ember Red
internal val onPrimaryDark = Color(0xFF690005)
internal val primaryContainerDark = Color(0xFF93000A)
internal val onPrimaryContainerDark = Color(0xFFFFDAD6)
internal val secondaryDark = Color(0xFFFFD700) // Bright Gold
internal val onSecondaryDark = Color(0xFF3F2E00)
internal val secondaryContainerDark = Color(0xFF5A4400)
internal val onSecondaryContainerDark = Color(0xFFFFE085)

internal val tertiaryDark = Color(0xFFA5D6A0) // Pale Elven Green
internal val onTertiaryDark = Color(0xFF183819)
internal val tertiaryContainerDark = Color(0xFF314E30)
internal val onTertiaryContainerDark = Color(0xFFCCEBC4)

internal val errorDark = Color(0xFFFFB4AB)
internal val onErrorDark = Color(0xFF690005)
internal val errorContainerDark = Color(0xFF93000A)
internal val onErrorContainerDark = Color(0xFFFFDAD6)

internal val backgroundDark = Color(0xFF121212) // Obsidian
internal val onBackgroundDark = Color(0xFFEAE1D9)
internal val surfaceDark = Color(0xFF121212)
internal val onSurfaceDark = Color(0xFFEAE1D9)
internal val surfaceVariantDark = Color(0xFF4D4639)
internal val onSurfaceVariantDark = Color(0xFFD0C5B4)
internal val outlineDark = Color(0xFF998F80)
internal val outlineVariantDark = Color(0xFF4D4639)

internal val scrimDark = Color(0xFF000000)
internal val inverseSurfaceDark = Color(0xFFEAE1D9)
internal val inverseOnSurfaceDark = Color(0xFF1F1B16)
internal val inversePrimaryDark = Color(0xFF8B0000)
internal val surfaceDimDark = Color(0xFF121212)
internal val surfaceBrightDark = Color(0xFF383430)
internal val surfaceContainerLowestDark = Color(0xFF0D0B0A)
internal val surfaceContainerLowDark = Color(0xFF1B1816)
internal val surfaceContainerDark = Color(0xFF1F1C19)
internal val surfaceContainerHighDark = Color(0xFF2A2624)
internal val surfaceContainerHighestDark = Color(0xFF35312E)

val lightScheme =
    lightColorScheme(
        primary = primaryLight,
        onPrimary = onPrimaryLight,
        primaryContainer = primaryContainerLight,
        onPrimaryContainer = onPrimaryContainerLight,
        secondary = secondaryLight,
        onSecondary = onSecondaryLight,
        secondaryContainer = secondaryContainerLight,
        onSecondaryContainer = onSecondaryContainerLight,
        tertiary = tertiaryLight,
        onTertiary = onTertiaryLight,
        tertiaryContainer = tertiaryContainerLight,
        onTertiaryContainer = onTertiaryContainerLight,
        error = errorLight,
        onError = onErrorLight,
        errorContainer = errorContainerLight,
        onErrorContainer = onErrorContainerLight,
        background = backgroundLight,
        onBackground = onBackgroundLight,
        surface = surfaceLight,
        onSurface = onSurfaceLight,
        surfaceVariant = surfaceVariantLight,
        onSurfaceVariant = onSurfaceVariantLight,
        outline = outlineLight,
        outlineVariant = outlineVariantLight,
        scrim = scrimLight,
        inverseSurface = inverseSurfaceLight,
        inverseOnSurface = inverseOnSurfaceLight,
        inversePrimary = inversePrimaryLight,
        surfaceDim = surfaceDimLight,
        surfaceBright = surfaceBrightLight,
        surfaceContainerLowest = surfaceContainerLowestLight,
        surfaceContainerLow = surfaceContainerLowLight,
        surfaceContainer = surfaceContainerLight,
        surfaceContainerHigh = surfaceContainerHighLight,
        surfaceContainerHighest = surfaceContainerHighestLight,
    )

val darkScheme =
    darkColorScheme(
        primary = primaryDark,
        onPrimary = onPrimaryDark,
        primaryContainer = primaryContainerDark,
        onPrimaryContainer = onPrimaryContainerDark,
        secondary = secondaryDark,
        onSecondary = onSecondaryDark,
        secondaryContainer = secondaryContainerDark,
        onSecondaryContainer = onSecondaryContainerDark,
        tertiary = tertiaryDark,
        onTertiary = onTertiaryDark,
        tertiaryContainer = tertiaryContainerDark,
        onTertiaryContainer = onTertiaryContainerDark,
        error = errorDark,
        onError = onErrorDark,
        errorContainer = errorContainerDark,
        onErrorContainer = onErrorContainerDark,
        background = backgroundDark,
        onBackground = onBackgroundDark,
        surface = surfaceDark,
        onSurface = onSurfaceDark,
        surfaceVariant = surfaceVariantDark,
        onSurfaceVariant = onSurfaceVariantDark,
        outline = outlineDark,
        outlineVariant = outlineVariantDark,
        scrim = scrimDark,
        inverseSurface = inverseSurfaceDark,
        inverseOnSurface = inverseOnSurfaceDark,
        inversePrimary = inversePrimaryDark,
        surfaceDim = surfaceDimDark,
        surfaceBright = surfaceBrightDark,
        surfaceContainerLowest = surfaceContainerLowestDark,
        surfaceContainerLow = surfaceContainerLowDark,
        surfaceContainer = surfaceContainerDark,
        surfaceContainerHigh = surfaceContainerHighDark,
        surfaceContainerHighest = surfaceContainerHighestDark,
    )
