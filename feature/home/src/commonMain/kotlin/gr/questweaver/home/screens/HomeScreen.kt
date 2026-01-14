package gr.questweaver.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import gr.questweaver.navigation.Route

@Composable
fun HomeUiRoute(
        onNavigate: (Route) -> Unit,
        viewModel: HomeViewModel = viewModel { HomeViewModel() }
) {
    val state by viewModel.state.collectAsState()

    HomeScreen(state = state, onNavigate = onNavigate)
}

@Composable
fun HomeScreen(state: HomeState, onNavigate: (Route) -> Unit) {
    HomeContent()
}

@Composable
fun HomeContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Home Screen")
    }
}
