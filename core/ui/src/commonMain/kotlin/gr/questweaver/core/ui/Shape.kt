package gr.questweaver.core.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
val AppShapes
    @Composable
    @ReadOnlyComposable
    get() =
        Shapes(
            extraSmall = RoundedCornerShape(sizes.eleven),
            small = RoundedCornerShape(sizes.ten),
            medium = RoundedCornerShape(sizes.eight),
            large = RoundedCornerShape(sizes.four), // Standard Button
            largeIncreased = RoundedCornerShape(sizes.three),
            extraLarge = RoundedCornerShape(sizes.two),
            extraLargeIncreased = RoundedCornerShape(sizes.one),
            extraExtraLarge = RoundedCornerShape(sizes.zero),
        )
