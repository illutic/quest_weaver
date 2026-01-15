package gr.questweaver.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gr.questweaver.core.ui.sizes
import gr.questweaver.home.Resource

@Composable
fun ResourceCard(resource: Resource, onClick: () -> Unit, modifier: Modifier = Modifier) {
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
        Column {
            // Image Placeholder area
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .aspectRatio(1.8f) // Wide aspect ratio
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = 0.5f
                            )
                        ),
                contentAlignment = Alignment.Center
            ) {
                // TODO: specific image loading when library is available
                if (resource.imageUrl != null) {
                    // Placeholder for AsyncImage
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        modifier = Modifier.fillMaxWidth(0.2f).aspectRatio(1f)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth(0.2f).aspectRatio(1f)
                    )
                }
            }

            Column(modifier = Modifier.padding(sizes.four)) {
                Text(
                    text = resource.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(sizes.ten))
                Text(
                    text = resource.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
        }
    }
}
