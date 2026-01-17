package gr.questweaver.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gr.questweaver.core.ui.sizes
import gr.questweaver.home.components.AiAssistantCard
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ResourcesSection(
    strings: HomeStrings,
    resources: ImmutableList<Resource>,
    onAiAssistantClick: () -> Unit,
    onResourceClick: (String) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier =
                Modifier.fillMaxWidth()
                    .padding(horizontal = sizes.four, vertical = sizes.eight),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = strings.usefulResourcesTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = strings.usefulResourcesViewAll,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(onClick = onViewAllClick)
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = sizes.four),
            verticalArrangement = Arrangement.spacedBy(sizes.four)
        ) {
            AiAssistantCard(strings = strings, onClick = onAiAssistantClick)

            resources.forEach { resource ->
                ResourceCard(resource = resource, onClick = { onResourceClick(resource.id) })
            }
        }
    }
}
