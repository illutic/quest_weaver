package gr.questweaver.home.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import gr.questweaver.core.components.Button
import gr.questweaver.core.components.ButtonType
import gr.questweaver.core.components.TextField
import gr.questweaver.core.ui.sizes
import gr.questweaver.home.GameType
import gr.questweaver.home.HomeStrings

@Composable
fun CreateGameScreen(
    onSubmit: (String, GameType) -> Unit,
    strings: HomeStrings
) {
    var title by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(GameType.Campaign) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(sizes.four),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(strings.createGameInputLabel) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(sizes.four))

        Row(modifier = Modifier.fillMaxWidth()) {
            GameTypeButton(
                label = strings.createGameCampaign,
                selected = selectedType == GameType.Campaign,
                onClick = { selectedType = GameType.Campaign },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(sizes.two))

            GameTypeButton(
                label = strings.createGameOneShot,
                selected = selectedType == GameType.OneShot,
                onClick = { selectedType = GameType.OneShot },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(sizes.six))

        Button(
            onClick = { onSubmit(title, selectedType) },
            buttonType = ButtonType.Primary,
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank()
        ) { Text(strings.startGameButton) }
    }
}

@Composable
private fun GameTypeButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        buttonType = if (selected) ButtonType.Primary else ButtonType.Outlined,
        modifier = modifier
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.padding(end = sizes.two)
            )
        }
        Text(label)
    }
}
