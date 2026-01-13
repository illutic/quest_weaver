package gr.questweaver.onboarding.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import gr.questweaver.core.components.Button
import gr.questweaver.core.components.ButtonType
import gr.questweaver.core.components.Message
import gr.questweaver.core.components.TextField
import gr.questweaver.core.ui.sizes
import gr.questweaver.onboarding.OnboardingStrings

@Composable
fun RegistrationScreen(
    strings: OnboardingStrings,
    name: String,
    onNameChange: (String) -> Unit,
    onRandomNameClick: () -> Unit,
    onRegisterClick: (String) -> Unit,
    error: String? = null,
    onErrorDismiss: () -> Unit = {},
) {
    var visible by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) { visible = true }

    LaunchedEffect(error) {
        if (error != null) {
            snackbarHostState.showSnackbar(error)
            onErrorDismiss()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                RegistrationContent(
                    strings = strings,
                    name = name,
                    onNameChange = onNameChange,
                    onRegisterClick = onRegisterClick,
                    onRandomNameClick = onRandomNameClick,
                    visible = visible,
                )
            }
        },
    )
}

@Composable
private fun RegistrationContent(
    strings: OnboardingStrings,
    name: String,
    onNameChange: (String) -> Unit,
    onRegisterClick: (String) -> Unit,
    onRandomNameClick: () -> Unit,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = sizes.one),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        RegistrationTitle(strings = strings, visible = visible)

        Spacer(modifier = Modifier.height(sizes.zero))

        RegistrationForm(
            strings = strings,
            visible = visible,
            name = name,
            onNameChange = onNameChange,
        )

        Spacer(modifier = Modifier.weight(1f))

        RegistrationActions(
            strings = strings,
            visible = visible,
            name = name,
            onRegisterClick = onRegisterClick,
            onRandomNameClick = onRandomNameClick,
        )
    }
}

@Composable
private fun RegistrationTitle(
    strings: OnboardingStrings,
    visible: Boolean,
) {
    AnimatedVisibility(visible = visible, enter = titleEnterAnimation()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = strings.registrationTitle,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(sizes.eight))

            Text(
                text = strings.registrationSubtitle,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun RegistrationForm(
    strings: OnboardingStrings,
    visible: Boolean,
    name: String,
    onNameChange: (String) -> Unit,
) {
    AnimatedVisibility(visible = visible, enter = inputEnterAnimation()) {
        Column {
            TextField(
                value = name,
                onValueChange = onNameChange,
                placeholder = { Text(strings.registrationInputPlaceholder) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null)
                },
            )

            Spacer(modifier = Modifier.height(sizes.four))

            Message(
                text = strings.registrationInfoText,
                icon = rememberVectorPainter(Icons.Outlined.Info),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun RegistrationActions(
    strings: OnboardingStrings,
    visible: Boolean,
    name: String,
    onRegisterClick: (String) -> Unit,
    onRandomNameClick: () -> Unit,
) {
    AnimatedVisibility(visible = visible, enter = buttonEnterAnimation()) {
        Column {
            Button(
                onClick = { onRegisterClick(name) },
                buttonType = ButtonType.Primary,
                enabled = name.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            ) { Text(strings.registrationCreateButton) }

            Spacer(modifier = Modifier.height(sizes.four))

            Button(
                onClick = onRandomNameClick,
                buttonType = ButtonType.Outlined,
                modifier = Modifier.fillMaxWidth().padding(bottom = sizes.four),
            ) { Text(strings.registrationRandomButton) }
        }
    }
}

private fun titleEnterAnimation() =
    fadeIn(
        animationSpec =
            tween(
                RegistrationScreenDefaults.ANIMATION_DURATION,
                delayMillis = RegistrationScreenDefaults.ANIMATION_DELAY_SHORT,
            ),
    ) +
        expandVertically(
            animationSpec =
                tween(
                    RegistrationScreenDefaults.ANIMATION_DURATION,
                    delayMillis =
                        RegistrationScreenDefaults.ANIMATION_DELAY_SHORT,
                ),
        )

private fun inputEnterAnimation() =
    fadeIn(
        animationSpec =
            tween(
                RegistrationScreenDefaults.ANIMATION_DURATION,
                delayMillis = RegistrationScreenDefaults.ANIMATION_DELAY_MEDIUM,
            ),
    ) +
        slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec =
                tween(
                    RegistrationScreenDefaults.ANIMATION_DURATION,
                    delayMillis =
                        RegistrationScreenDefaults.ANIMATION_DELAY_MEDIUM,
                ),
        )

private fun buttonEnterAnimation() =
    fadeIn(
        animationSpec =
            tween(
                RegistrationScreenDefaults.ANIMATION_DURATION,
                delayMillis = RegistrationScreenDefaults.ANIMATION_DELAY_LONG,
            ),
    ) +
        slideInVertically(
            initialOffsetY = { it },
            animationSpec =
                tween(
                    RegistrationScreenDefaults.ANIMATION_DURATION,
                    delayMillis =
                        RegistrationScreenDefaults.ANIMATION_DELAY_LONG,
                ),
        )

private object RegistrationScreenDefaults {
    const val ANIMATION_DURATION = 800
    const val ANIMATION_DELAY_SHORT = 100
    const val ANIMATION_DELAY_MEDIUM = 300
    const val ANIMATION_DELAY_LONG = 500
}
