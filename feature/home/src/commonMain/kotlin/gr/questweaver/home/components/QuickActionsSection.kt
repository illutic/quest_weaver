package gr.questweaver.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gr.questweaver.core.ui.sizes
import gr.questweaver.home.HomeStrings

@Composable
fun QuickActionsSection(
    strings: HomeStrings,
    onCreateGameClick: () -> Unit,
    onJoinGameClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = strings.quickActionsTitle,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = sizes.four, vertical = sizes.eight)
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = sizes.four),
            horizontalArrangement = Arrangement.spacedBy(sizes.four)
        ) {
            QuickActionButton(
                text = strings.createGameButton,
                icon = Icons.Default.Add,
                gradient =
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF7F52FF), Color(0xFFC852FF))
                    ),
                onClick = onCreateGameClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                text = strings.joinGameButton,
                icon = Icons.Default.Group,
                gradient =
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFFC852FF), Color(0xFFFF52C8))
                    ),
                onClick = onJoinGameClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    text: String,
    icon: ImageVector,
    gradient: Brush,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
            modifier.height(80.dp)
                .clip(RoundedCornerShape(sizes.four))
                .background(gradient)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp) // Icon size standard
            )
            Spacer(modifier = Modifier.height(sizes.ten))
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
