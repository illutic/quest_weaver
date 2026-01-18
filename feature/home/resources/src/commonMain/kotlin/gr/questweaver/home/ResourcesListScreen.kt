package gr.questweaver.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import gr.questweaver.core.ui.sizes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourcesListScreen(
    state: HomeState,
    onResourceClick: (String) -> Unit
) {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(sizes.four),
            verticalArrangement = Arrangement.spacedBy(sizes.four)
        ) {
            items(state.resources) { resource ->
                ResourceCard(resource = resource, onClick = { onResourceClick(resource.id) })
            }
        }
    }
}
