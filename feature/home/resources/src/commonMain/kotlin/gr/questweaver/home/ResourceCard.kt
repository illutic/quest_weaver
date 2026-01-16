package gr.questweaver.home

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import gr.questweaver.core.components.glassEffect
import gr.questweaver.core.components.scaleOnPress
import gr.questweaver.core.ui.sizes

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
            // Image area
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
                AsyncImage(
                    model = resource.imageUrl,
                    contentDescription = resource.title,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    error = rememberVectorPainter(Icons.Default.Image)
                )
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
