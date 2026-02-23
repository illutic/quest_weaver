package gr.questweaver.core.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem<T>(
    val route: T,
    val label: String,
    val icon: ImageVector,
)

@Composable
fun <T> AppBottomBar(
    navItems: List<NavItem<out T>>,
    selectedNavIndex: Int,
    onNavigate: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    ShortNavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        navItems.forEachIndexed { index, item ->
            ShortNavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selectedNavIndex == index,
                onClick = { onNavigate(item.route) }
            )
        }
    }
}