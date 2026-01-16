package gr.questweaver.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import gr.questweaver.core.ui.sizes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceDetailsScreen(resource: Resource?) {
    if (resource != null) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Image
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
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
                Spacer(modifier = Modifier.height(sizes.two))
                Text(text = resource.description, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
