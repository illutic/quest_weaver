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
import gr.questweaver.onboarding.OnboardingDrawables
import gr.questweaver.onboarding.OnboardingStrings
import org.jetbrains.compose.resources.painterResource

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WelcomeScreen(
    strings: OnboardingStrings,
    drawables: OnboardingDrawables,
    onStartClick: () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    WelcomeContent(
        strings = strings,
        drawables = drawables,
        visible = visible,
        onStartClick = onStartClick,
    )
}

@Composable
private fun WelcomeContent(
    strings: OnboardingStrings,
    drawables: OnboardingDrawables,
    visible: Boolean,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = sizes.one),
        ) {
            Spacer(modifier = Modifier.weight(1f))

            WelcomeLogo(drawables = drawables, visible = visible)

            Spacer(modifier = Modifier.height(sizes.zero))

            WelcomeHeader(strings = strings, visible = visible)

            Spacer(modifier = Modifier.weight(1f))

            WelcomeActions(
                strings = strings,
                visible = visible,
                onStartClick = onStartClick,
            )
        }
    }
}

@Composable
private fun WelcomeLogo(
    drawables: OnboardingDrawables,
    visible: Boolean,
) {
    AnimatedVisibility(visible = visible, enter = logoEnterAnimation()) {
        Icon(
            painter = painterResource(drawables.logo),
            contentDescription = drawables.logoName,
            tint = Color.Unspecified,
            modifier = Modifier.size(WelcomeScreenDefaults.ICON_SIZE),
        )
    }
}

@Composable
private fun WelcomeHeader(
    strings: OnboardingStrings,
    visible: Boolean,
) {
    AnimatedVisibility(visible = visible, enter = textEnterAnimation()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = strings.welcomeTitle,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(sizes.eight))

            Text(
                text = strings.welcomeSubtitle,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun WelcomeActions(
    strings: OnboardingStrings,
    visible: Boolean,
    onStartClick: () -> Unit,
) {
    AnimatedVisibility(visible = visible, enter = buttonEnterAnimation()) {
        Column {
            Button(
                onClick = onStartClick,
                buttonType = ButtonType.Primary,
                modifier = Modifier.fillMaxWidth().padding(bottom = sizes.two),
            ) { Text(strings.welcomeButton) }
        }
    }
}

private fun logoEnterAnimation() =
    fadeIn(animationSpec = tween(WelcomeScreenDefaults.ANIMATION_DURATION)) +
            scaleIn(
                initialScale = WelcomeScreenDefaults.INITIAL_SCALE,
                animationSpec = tween(WelcomeScreenDefaults.ANIMATION_DURATION),
            )

private fun textEnterAnimation() =
    fadeIn(
        animationSpec =
            tween(
                WelcomeScreenDefaults.ANIMATION_DURATION,
                delayMillis = WelcomeScreenDefaults.ANIMATION_DELAY_SHORT,
            ),
    ) +
            expandVertically(
                animationSpec =
                    tween(
                        WelcomeScreenDefaults.ANIMATION_DURATION,
                        delayMillis = WelcomeScreenDefaults.ANIMATION_DELAY_SHORT,
                    ),
            )

private fun buttonEnterAnimation() =
    fadeIn(
        animationSpec =
            tween(
                WelcomeScreenDefaults.ANIMATION_DURATION,
                delayMillis = WelcomeScreenDefaults.ANIMATION_DELAY_LONG,
            ),
    ) +
            slideInVertically(
                initialOffsetY = { it },
                animationSpec =
                    tween(
                        WelcomeScreenDefaults.ANIMATION_DURATION,
                        delayMillis = WelcomeScreenDefaults.ANIMATION_DELAY_LONG,
                    ),
            )

private object WelcomeScreenDefaults {
    val ICON_SIZE = 120.dp
    const val PROGRESS_STEP_1 = 0.33f
    const val ANIMATION_DURATION = 1000
    const val ANIMATION_DELAY_SHORT = 300
    const val ANIMATION_DELAY_LONG = 600
    const val INITIAL_SCALE = 0.5f
}
