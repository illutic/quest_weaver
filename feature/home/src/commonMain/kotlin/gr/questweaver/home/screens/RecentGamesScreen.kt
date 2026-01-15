package gr.questweaver.home.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import gr.questweaver.core.ui.sizes
import gr.questweaver.home.HomeState
import gr.questweaver.home.components.GameCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentGamesScreen(state: HomeState, onGameClick: (String) -> Unit, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.strings.recentGamesTitle) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(sizes.four),
            verticalArrangement = Arrangement.spacedBy(sizes.four)
        ) {
            items(state.recentGames) { game ->
                GameCard(game = game, strings = state.strings, onClick = { onGameClick(game.id) })
            }
        }
    }
}
