package gr.questweaver.bottombar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import gr.questweaver.core.ui.sizes

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
            BottomBarInternalLayout(
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedVisibility(
                    visible = state.showBackButton,
                    enter = fadeIn() + slideInHorizontally { it },
                    exit = fadeOut() + slideOutHorizontally { it },
                    modifier = Modifier.layoutId("back")
                ) {
                    FloatingActionButton(
                        onClick = { viewModel.onEvent(BottomBarEvent.OnBackClick) },
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        shape = CircleShape
                    ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                }

                Box(modifier = Modifier.layoutId("main")) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shadowElevation = 8.dp,
                        modifier = Modifier.height(64.dp)
                    ) {
                        when (val mode = state.mode) {
                            is BottomBarMode.Standard -> {
                                StandardContent(items = state.items, onEvent = viewModel::onEvent)
                            }

                            is BottomBarMode.TextField -> {
                                TextFieldContent(
                                    placeholder = mode.placeholder,
                                    value = state.inputValue,
                                    onEvent = viewModel::onEvent
                                )
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
}

@Composable
private fun BottomBarInternalLayout(
    modifier: Modifier = Modifier,
    spacing: Dp = sizes.two,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val mainMeasurable = measurables.find { it.layoutId == "main" }
        val backMeasurable = measurables.find { it.layoutId == "back" }

        val looseConstraints = constraints.copy(minWidth = 0)
        val mainPlaceable = mainMeasurable?.measure(looseConstraints)
        val backPlaceable = backMeasurable?.measure(looseConstraints)

        val width = constraints.maxWidth
        val height = maxOf(mainPlaceable?.height ?: 0, backPlaceable?.height ?: 0)

        layout(width, height) {
            val mainWidth = mainPlaceable?.width ?: 0
            val mainX = (width - mainWidth) / 2
            val mainY = (height - (mainPlaceable?.height ?: 0)) / 2

            mainPlaceable?.placeRelative(mainX, mainY)

            if (backPlaceable != null) {
                val spacing = spacing.roundToPx()
                val backX = mainX - spacing - backPlaceable.width
                val backY = (height - backPlaceable.height) / 2
                backPlaceable.placeRelative(backX, backY)
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

@Composable
private fun TextFieldContent(
    placeholder: String,
    value: String,
    onEvent: (BottomBarEvent) -> Unit
) {
    Row(
        modifier =
            Modifier.padding(horizontal = sizes.two, vertical = 4.dp)
                .fillMaxWidth(0.9f), // Occupy most space
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(sizes.one)
    ) {
        TextField(
            value = value,
            onValueChange = { onEvent(BottomBarEvent.OnInputChanged(it)) },
            placeholder = { Text(placeholder) },
            colors =
                TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
            shape = CircleShape,
            modifier = Modifier.weight(1f).clip(CircleShape)
        )
        IconButton(
            onClick = { onEvent(BottomBarEvent.OnSubmitClick) },
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = MaterialTheme.colorScheme.primary
            )
        }
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
