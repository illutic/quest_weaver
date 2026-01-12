package gr.questweaver.onboarding.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import gr.questweaver.core.components.Button
import gr.questweaver.core.components.ButtonType
import gr.questweaver.core.ui.sizes
import gr.questweaver.feature.onboarding.Res
import gr.questweaver.feature.onboarding.ic_logo_android
import gr.questweaver.feature.onboarding.onboarding_welcome_button
import gr.questweaver.feature.onboarding.onboarding_welcome_subtitle
import gr.questweaver.feature.onboarding.onboarding_welcome_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WelcomeScreen(onStartClick: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    WelcomeContent(visible = visible, onStartClick = onStartClick)
}

@Composable
private fun WelcomeContent(
    visible: Boolean,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = sizes.one)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            WelcomeLogo(visible = visible)

            Spacer(modifier = Modifier.height(sizes.zero))

            WelcomeHeader(visible = visible)

            Spacer(modifier = Modifier.weight(1f))

            WelcomeActions(visible = visible, onStartClick = onStartClick)
        }
    }
}

@Composable
private fun WelcomeLogo(visible: Boolean) {
    AnimatedVisibility(visible = visible, enter = logoEnterAnimation()) {
        // Placeholder for d20 icon
        Icon(
            painter = painterResource(Res.drawable.ic_logo_android),
            contentDescription = null,
            modifier = Modifier.size(WelcomeScreenDefaults.ICON_SIZE),
            tint = Color.Unspecified,
        )
    }
}

@Composable
private fun WelcomeHeader(visible: Boolean) {
    AnimatedVisibility(visible = visible, enter = textEnterAnimation()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(Res.string.onboarding_welcome_title),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(sizes.eight))

            Text(
                text = stringResource(Res.string.onboarding_welcome_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun WelcomeActions(visible: Boolean, onStartClick: () -> Unit) {
    AnimatedVisibility(visible = visible, enter = buttonEnterAnimation()) {
        Button(
            onClick = onStartClick,
            buttonType = ButtonType.Primary,
            modifier = Modifier.fillMaxWidth().padding(bottom = sizes.four)
        ) { Text(stringResource(Res.string.onboarding_welcome_button)) }
    }
}

private fun logoEnterAnimation() =
    fadeIn(animationSpec = tween(WelcomeScreenDefaults.ANIMATION_DURATION)) +
            scaleIn(
                initialScale = WelcomeScreenDefaults.INITIAL_SCALE,
                animationSpec = tween(WelcomeScreenDefaults.ANIMATION_DURATION)
            )

private fun textEnterAnimation() =
    fadeIn(
        animationSpec =
            tween(
                WelcomeScreenDefaults.ANIMATION_DURATION,
                delayMillis = WelcomeScreenDefaults.ANIMATION_DELAY_SHORT
            )
    ) +
            expandVertically(
                animationSpec =
                    tween(
                        WelcomeScreenDefaults.ANIMATION_DURATION,
                        delayMillis = WelcomeScreenDefaults.ANIMATION_DELAY_SHORT
                    )
            )

private fun buttonEnterAnimation() =
    fadeIn(
        animationSpec =
            tween(
                WelcomeScreenDefaults.ANIMATION_DURATION,
                delayMillis = WelcomeScreenDefaults.ANIMATION_DELAY_LONG
            )
    ) +
            slideInVertically(
                initialOffsetY = { it },
                animationSpec =
                    tween(
                        WelcomeScreenDefaults.ANIMATION_DURATION,
                        delayMillis = WelcomeScreenDefaults.ANIMATION_DELAY_LONG
                    )
            )

private object WelcomeScreenDefaults {
    val ICON_SIZE = 120.dp
    const val PROGRESS_STEP_1 = 0.33f
    const val ANIMATION_DURATION = 1000
    const val ANIMATION_DELAY_SHORT = 300
    const val ANIMATION_DELAY_LONG = 600
    const val INITIAL_SCALE = 0.5f
}
