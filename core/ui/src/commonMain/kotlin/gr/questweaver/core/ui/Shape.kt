package gr.questweaver.core.ui

import androidx.compose.foundation.shape.CutCornerShape
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
            medium = CutCornerShape(sizes.eight),
            large = CutCornerShape(sizes.four),
            largeIncreased = CutCornerShape(sizes.three),
            extraLarge = CutCornerShape(sizes.two),
            extraLargeIncreased = CutCornerShape(sizes.one),
            extraExtraLarge = CutCornerShape(sizes.zero),
        )
