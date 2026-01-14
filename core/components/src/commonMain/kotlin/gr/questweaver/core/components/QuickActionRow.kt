package gr.questweaver.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class QuickAction(
    val id: String,
    val label: String,
    val onClick: () -> Unit,
)

@Composable
fun QuickActionRow(
    actions: List<QuickAction>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(actions) { action ->
            // Reusing DiceChip style for now as 'pill' buttons, or could create a specific
            // ActionButton.
            // DiceChip fits the 'pill' description well enough for quick actions.
            DiceChip(
                label = action.label,
                onClick = action.onClick,
                isSelected = false, // Default state
                // We might want a different visual for actions vs dice toggles, but this is a
                // good start.
            )
        }
    }
}

@Preview
@Composable
private fun PreviewQuickActionRow() {
    QuickActionRow(
        actions =
            listOf(
                QuickAction("1", "Attack", {}),
                QuickAction("2", "Heal", {}),
                QuickAction("3", "Sneak", {}),
            ),
    )
}
