package gr.questweaver.onboarding

import org.jetbrains.compose.resources.DrawableResource

data class OnboardingDrawables(
    val logo: DrawableResource,
    val logoName: String,
) {
    companion object {
        val Default by lazy {
            OnboardingDrawables(logo = onboardingLogo, logoName = onboardingLogoName)
        }

        fun load(): OnboardingDrawables =
            OnboardingDrawables(logo = onboardingLogo, logoName = onboardingLogoName)
    }
}

internal expect val onboardingLogo: DrawableResource
internal expect val onboardingLogoName: String
