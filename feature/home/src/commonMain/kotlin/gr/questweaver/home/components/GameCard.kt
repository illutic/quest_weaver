package gr.questweaver.home.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gr.questweaver.core.ui.sizes
import gr.questweaver.home.GameSession
import gr.questweaver.home.HomeStrings

@Composable
fun GameCard(
    game: GameSession,
    strings: HomeStrings,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(sizes.four),
        elevation = CardDefaults.cardElevation(defaultElevation = sizes.eleven),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(sizes.four)) {
            Row(verticalAlignment = Alignment.Top) {
                // Game Icon Placeholder
                Box(
                    modifier =
                        Modifier.size(48.dp) // Specific size, keep dp
                            .clip(RoundedCornerShape(sizes.eight))
                            .background(
                                MaterialTheme.colorScheme
                                    .secondaryContainer
                            ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(modifier = Modifier.width(sizes.six))

                // Title and Details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = game.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${game.type} • Level ${game.level}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Status Pill
                StatusPill(isLive = game.isLive, strings = strings)
            }

            Spacer(modifier = Modifier.height(sizes.four))

            // Player Count (Bottom Left)
            Text(
                text = strings.gamePlayers.replace("%s", game.players.toString()),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StatusPill(isLive: Boolean, strings: HomeStrings) {
    val color = if (isLive) Color(0xFF00C853) else Color.Gray // Green vs Gray
    val text = if (isLive) strings.gameLive else strings.gameOffline

    val alpha =
        if (isLive) {
            val infiniteTransition = rememberInfiniteTransition()
            infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 0.4f,
                animationSpec =
                    infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse
                    )
            )
                .value
        } else {
            1f
        }

    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = color.copy(alpha = alpha),
        fontWeight = FontWeight.Bold
    )
}
