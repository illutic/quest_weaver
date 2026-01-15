package gr.questweaver.home.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier =
            modifier.fillMaxWidth()
                .scaleOnPress(interactionSource)
                .glassEffect()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
    ) {
        Column(modifier = Modifier.padding(sizes.four)) {
            Row(verticalAlignment = Alignment.Top) {
                // Game Icon Placeholder
                Box(
                    modifier =
                        Modifier.size(48.dp)
                            .clip(RoundedCornerShape(sizes.eight))
                            .background(
                                Brush.linearGradient(
                                    colors =
                                        listOf(
                                            MaterialTheme.colorScheme
                                                .primary.copy(
                                                    alpha = 0.1f
                                                ),
                                            MaterialTheme.colorScheme
                                                .secondary.copy(
                                                    alpha = 0.1f
                                                )
                                        )
                                )
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

            // Player Count (Bottom Let)
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
    val backgroundColor =
        if (isLive) Color(0xFF00C853).copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f)
    val contentColor = if (isLive) Color(0xFF00C853) else Color.Gray
    val text = if (isLive) strings.gameLive else strings.gameOffline

    val alpha =
        if (isLive) {
            val infiniteTransition = rememberInfiniteTransition()
            infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 0.6f,
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

    Box(
        modifier =
            Modifier.alpha(alpha)
                .background(backgroundColor, RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            fontWeight = FontWeight.Bold
        )
    }
}
