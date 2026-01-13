package gr.questweaver.onboarding.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import gr.questweaver.core.components.Button
import gr.questweaver.core.components.ButtonType
import gr.questweaver.core.ui.sizes
import gr.questweaver.onboarding.OnboardingStrings

@Composable
fun TutorialScreen(strings: OnboardingStrings, onCompleteClick: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    TutorialContent(strings = strings, visible = visible, onCompleteClick = onCompleteClick)
}

@Composable
private fun TutorialContent(
    strings: OnboardingStrings,
    visible: Boolean,
    onCompleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = sizes.one),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        TutorialTitle(strings = strings, visible = visible)

        Spacer(modifier = Modifier.height(sizes.eight))

        TutorialList(strings = strings, visible = visible)

        Spacer(modifier = Modifier.weight(1f))

        TutorialActions(strings = strings, visible = visible, onCompleteClick = onCompleteClick)
    }
}

@Composable
private fun TutorialTitle(strings: OnboardingStrings, visible: Boolean) {
    AnimatedVisibility(visible = visible, enter = titleEnterAnimation()) {
        Text(
            text = strings.tutorialTitle,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun TutorialList(strings: OnboardingStrings, visible: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(sizes.four)) {
        TutorialItem(
            visible = visible,
            delay = TutorialScreenDefaults.ANIMATION_DELAY_ITEM_1,
            icon = Icons.Default.Info,
            text = strings.tutorialItem1
        )

        TutorialItem(
            visible = visible,
            delay = TutorialScreenDefaults.ANIMATION_DELAY_ITEM_2,
            icon = Icons.Default.Info,
            content = {
                Text(
                    text =
                        buildAnnotatedString {
                            append(strings.tutorialPolicyPart1)
                            withStyle(
                                style =
                                    SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        color =
                                            MaterialTheme.colorScheme
                                                .onSurfaceVariant
                                    )
                            ) { append(strings.tutorialPrivacyPolicy) }
                            append(strings.tutorialPolicyPart2)
                            withStyle(
                                style =
                                    SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        color =
                                            MaterialTheme.colorScheme
                                                .onSurfaceVariant
                                    )
                            ) { append(strings.tutorialTermsOfService) }
                        },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )

        TutorialItem(
            visible = visible,
            delay = TutorialScreenDefaults.ANIMATION_DELAY_ITEM_3,
            icon = Icons.Default.Star,
            text = strings.tutorialItem3
        )

        TutorialItem(
            visible = visible,
            delay = TutorialScreenDefaults.ANIMATION_DELAY_ITEM_4,
            icon = Icons.Default.Star,
            text = strings.tutorialItem4
        )
    }
}

@Composable
private fun TutorialActions(
    strings: OnboardingStrings,
    visible: Boolean,
    onCompleteClick: () -> Unit
) {
    AnimatedVisibility(visible = visible, enter = buttonEnterAnimation()) {
        Button(
            onClick = onCompleteClick,
            buttonType = ButtonType.Primary,
            modifier = Modifier.fillMaxWidth().padding(bottom = sizes.four)
        ) { Text(strings.tutorialButton) }
    }
}

@Composable
private fun TutorialItem(
    visible: Boolean,
    delay: Int,
    icon: ImageVector,
    text: String? = null,
    content: @Composable (() -> Unit)? = null // null default is fine
) {
    AnimatedVisibility(visible = visible, enter = itemEnterAnimation(delay)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Box(
                modifier =
                    Modifier.size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(sizes.four))
            if (content != null) {
                content()
            } else {
                Text(
                    text = text ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun titleEnterAnimation() =
    fadeIn(animationSpec = tween(TutorialScreenDefaults.ANIMATION_DURATION)) +
            expandVertically(animationSpec = tween(TutorialScreenDefaults.ANIMATION_DURATION))

private fun itemEnterAnimation(delay: Int) =
    fadeIn(
        animationSpec =
            tween(TutorialScreenDefaults.ANIMATION_DURATION, delayMillis = delay)
    ) +
            slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec =
                    tween(
                        TutorialScreenDefaults.ANIMATION_DURATION,
                        delayMillis = delay
                    )
            )

private fun buttonEnterAnimation() =
    fadeIn(
        animationSpec =
            tween(
                TutorialScreenDefaults.ANIMATION_DURATION,
                delayMillis = TutorialScreenDefaults.ANIMATION_DELAY_BUTTON
            )
    ) +
            slideInVertically(
                initialOffsetY = { it },
                animationSpec =
                    tween(
                        TutorialScreenDefaults.ANIMATION_DURATION,
                        delayMillis = TutorialScreenDefaults.ANIMATION_DELAY_BUTTON
                    )
            )

private object TutorialScreenDefaults {
    const val ANIMATION_DURATION = 800
    const val ANIMATION_DELAY_ITEM_1 = 100
    const val ANIMATION_DELAY_ITEM_2 = 250
    const val ANIMATION_DELAY_ITEM_3 = 400
    const val ANIMATION_DELAY_ITEM_4 = 550
    const val ANIMATION_DELAY_BUTTON = 700
}
