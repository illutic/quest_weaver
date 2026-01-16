package gr.questweaver.core.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.glassEffect(shape: RoundedCornerShape = RoundedCornerShape(16.dp)): Modifier =
    composed {
        this.shadow(
            elevation = 8.dp,
            shape = shape,
            ambientColor = Color.Black.copy(alpha = 0.1f),
            spotColor = Color.Black.copy(alpha = 0.1f)
        )
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                shape = shape
            )
            .border(
                width = 1.dp,
                brush =
                    Brush.linearGradient(
                        colors =
                            listOf(
                                Color.White.copy(alpha = 0.6f),
                                Color.White.copy(alpha = 0.1f)
                            ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    ),
                shape = shape
            )
            .clip(shape)
    }

fun Modifier.scaleOnPress(interactionSource: MutableInteractionSource): Modifier = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f)
    this.scale(scale)
}
