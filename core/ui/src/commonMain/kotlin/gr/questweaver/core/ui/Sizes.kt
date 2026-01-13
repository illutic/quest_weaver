package gr.questweaver.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Sizes(
    val zero: Dp,
    val one: Dp,
    val two: Dp,
    val three: Dp,
    val four: Dp,
    val five: Dp,
    val six: Dp,
    val seven: Dp,
    val eight: Dp,
    val nine: Dp,
    val ten: Dp,
    val eleven: Dp,
)

val defaultSizes =
    Sizes(
        zero = 34.dp,
        one = 22.dp,
        two = 20.dp,
        three = 18.dp,
        four = 16.dp,
        five = 14.dp,
        six = 12.dp,
        seven = 10.dp,
        eight = 8.dp,
        nine = 6.dp,
        ten = 4.dp,
        eleven = 2.dp,
    )

val LocalSizes = staticCompositionLocalOf { defaultSizes }

val sizes: Sizes
    @Composable @ReadOnlyComposable
    get() = LocalSizes.current
