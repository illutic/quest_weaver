package gr.questweaver.bottombar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults.elevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import gr.questweaver.core.ui.sizes

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    viewModel: BottomBarViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsState()

    AnimatedVisibility(
        visible = state.mode !is BottomBarMode.Empty,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        Box(
            modifier =
                modifier.fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = sizes.four)
                    .padding(horizontal = sizes.two),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(sizes.two, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.animateContentSize()
            ) {
                if (state.showBackButton) {
                    FloatingActionButton(
                        onClick = { viewModel.onEvent(BottomBarEvent.OnBackClick) },
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        elevation =
                            elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                focusedElevation = 0.dp,
                                hoveredElevation = 0.dp
                            ),
                        shape = CircleShape
                    ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                }

                Surface(
                    shape = MaterialTheme.shapes.largeIncreased,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier.heightIn(min = 64.dp)
                ) {
                    when (val mode = state.mode) {
                        is BottomBarMode.Standard -> {
                            StandardContent(items = state.items, onEvent = viewModel::onEvent)
                        }

                        BottomBarMode.Empty -> {
                            /* Should not happen due to outer visibility check */
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StandardContent(items: List<BottomBarItem>, onEvent: (BottomBarEvent) -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = sizes.two),
        horizontalArrangement = Arrangement.spacedBy(sizes.two),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            BottomBarNavItem(
                item = item,
                onClick = { onEvent(BottomBarEvent.OnItemClick(item.route)) }
            )
        }
    }
}

@Composable
private fun BottomBarNavItem(item: BottomBarItem, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = item.icon.toImageVector(),
            contentDescription = item.label,
            tint =
                if (item.selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun BottomBarIcon.toImageVector(): ImageVector {
    return when (this) {
        BottomBarIcon.Home -> Icons.Default.Home
        BottomBarIcon.Search -> Icons.Default.Search
        BottomBarIcon.Settings -> Icons.Default.Settings
        BottomBarIcon.Back -> Icons.AutoMirrored.Filled.ArrowBack
    }
}
