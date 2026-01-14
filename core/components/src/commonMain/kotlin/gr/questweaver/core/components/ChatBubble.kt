package gr.questweaver.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class BubbleType {
    TEXT,
    ROLL,
}

@Composable
fun ChatBubble(
    message: String,
    sender: String,
    type: BubbleType = BubbleType.TEXT,
    modifier: Modifier = Modifier,
    isMe: Boolean = false,
) {
    val alignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    val containerColor =
        if (type == BubbleType.ROLL) {
            MaterialTheme.colorScheme.tertiaryContainer
        } else if (isMe) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    val contentColor =
        if (type == BubbleType.ROLL) {
            MaterialTheme.colorScheme.onTertiaryContainer
        } else if (isMe) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = alignment,
    ) {
        Column(
            modifier =
                Modifier.fillMaxWidth(0.5f)
                    .clip(
                        CutCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isMe) 16.dp else 4.dp,
                            bottomEnd = if (isMe) 4.dp else 16.dp,
                        ),
                    )
                    .background(containerColor)
                    .padding(12.dp),
        ) {
            if (!isMe) {
                Text(
                    text = sender,
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.padding(2.dp))
            }

            if (type == BubbleType.ROLL) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Placeholder for a dice icon if needed
                    Text(
                        text = "🎲 ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor,
                    )
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = contentColor,
                    )
                }
            } else {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = contentColor,
                )
            }
        }
    }
}
