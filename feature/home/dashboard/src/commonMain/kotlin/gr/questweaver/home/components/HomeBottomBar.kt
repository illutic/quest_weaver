package gr.questweaver.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gr.questweaver.core.ui.sizes
import gr.questweaver.home.HomeRoute
import gr.questweaver.home.HomeStrings
import gr.questweaver.navigation.Route

@Composable
fun HomeBottomBar(
    currentRoute: Route,
    strings: HomeStrings,
    onNavigate: (HomeRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier =
            modifier.navigationBarsPadding().padding(bottom = sizes.four).height(64.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = sizes.two),
            horizontalArrangement = Arrangement.spacedBy(sizes.two),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItem(
                selected = currentRoute == HomeRoute.Home,
                onClick = { onNavigate(HomeRoute.Home) },
                icon = Icons.Default.Home,
                contentDescription = strings.navDashboard
            )
            BottomBarItem(
                selected = currentRoute == HomeRoute.Search,
                onClick = { onNavigate(HomeRoute.Search) },
                icon = Icons.Default.Search,
                contentDescription = strings.navSearch
            )
            BottomBarItem(
                selected = currentRoute == HomeRoute.Settings,
                onClick = { onNavigate(HomeRoute.Settings) },
                icon = Icons.Default.Settings,
                contentDescription = strings.navSettings
            )
        }
    }
}

@Composable
private fun BottomBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint =
                if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
