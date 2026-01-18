package gr.questweaver.home.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import gr.questweaver.home.HomeEvent
import gr.questweaver.home.HomeRoute
import gr.questweaver.home.HomeViewModel
import gr.questweaver.home.ResourceDetailsScreen
import gr.questweaver.home.create.CreateGameScreen
import gr.questweaver.navigation.SheetRoute

@Composable
fun HomeSheetUi(
    route: SheetRoute,
    onBack: () -> Unit,
    viewModel: HomeViewModel = viewModel { HomeViewModel() }
) {
    if (route !is HomeRoute) return

    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val title =
                when (route) {
                    is HomeRoute.ResourceDetails -> route.title
                    is HomeRoute.CreateGame -> state.strings.createGameModalTitle
                    else -> ""
                }

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            Text(text = "Close", modifier = Modifier.clickable { onBack() }.padding(8.dp))
        }
        HorizontalDivider()

        when (route) {
            is HomeRoute.CreateGame -> {
                CreateGameScreen(
                    onSubmit = { title, type ->
                        viewModel.onEvent(HomeEvent.OnSubmitCreateGame(title, type))
                    },
                    strings = state.strings
                )
            }
            is HomeRoute.ResourceDetails -> {
                val resource = state.resources.find { it.id == route.resourceId }
                if (resource != null) {
                    ResourceDetailsScreen(resource = resource)
                } else {
                    Text("Resource not found")
                }
            }
            else -> {
                Text("Unknown Home Sheet: $route")
            }
        }
    }
}
