package gr.questweaver.core.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Button(
    onClick: () -> Unit,
    buttonType: ButtonType,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val buttonShapes = ButtonDefaults.shapes(
        shape = MaterialTheme.shapes.large, pressedShape = MaterialTheme.shapes.largeIncreased
    )
    val colors = when (buttonType) {
        ButtonType.Primary -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )

        ButtonType.Secondary -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )

        ButtonType.Outlined -> ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    }

    when (buttonType) {
        ButtonType.Outlined -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier,
                shapes = buttonShapes,
                colors = colors,
                enabled = enabled,
                content = content
            )
        }

        else -> {
            Button(
                onClick = onClick,
                modifier = modifier,
                shapes = buttonShapes,
                colors = colors,
                enabled = enabled,
                content = content
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IconButton(
    onClick: () -> Unit,
    buttonType: ButtonType,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val buttonShapes = IconButtonDefaults.shapes(
        shape = MaterialTheme.shapes.extraLarge,
        pressedShape = MaterialTheme.shapes.extraLargeIncreased
    )
    val colors = when (buttonType) {
        ButtonType.Primary -> IconButtonDefaults.iconButtonColors(
            containerColor = Color.Unspecified,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = Color.Unspecified,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )

        ButtonType.Secondary -> IconButtonDefaults.iconButtonColors(
            containerColor = Color.Unspecified,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContainerColor = Color.Unspecified,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )

        ButtonType.Outlined -> IconButtonDefaults.iconButtonColors(
            containerColor = Color.Unspecified,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = Color.Unspecified,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    }

    when (buttonType) {
        ButtonType.Outlined -> {
            OutlinedIconButton(
                onClick = onClick,
                modifier = modifier,
                shapes = buttonShapes,
                colors = colors,
                enabled = enabled,
                content = content
            )
        }

        else -> {
            IconButton(
                onClick = onClick,
                modifier = modifier,
                shapes = buttonShapes,
                colors = colors,
                enabled = enabled,
                content = content
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun TextButton(
    onClick: () -> Unit,
    buttonType: ButtonType,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val buttonShapes = ButtonDefaults.shapes(
        shape = MaterialTheme.shapes.large, pressedShape = MaterialTheme.shapes.largeIncreased
    )
    val colors = when (buttonType) {
        ButtonType.Primary -> ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
        )

        ButtonType.Secondary -> ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.38f)
        )

        ButtonType.Outlined -> ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    }

    TextButton(
        onClick = onClick,
        modifier = modifier,
        shapes = buttonShapes,
        colors = colors,
        border = if (buttonType == ButtonType.Outlined) ButtonDefaults.outlinedButtonBorder(enabled) else null,
        enabled = enabled,
        content = content
    )
}