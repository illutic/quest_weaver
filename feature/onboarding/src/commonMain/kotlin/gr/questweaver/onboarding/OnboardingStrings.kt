package gr.questweaver.onboarding

import gr.questweaver.feature.onboarding.Res
import gr.questweaver.feature.onboarding.onboarding_registration_create_button
import gr.questweaver.feature.onboarding.onboarding_registration_error_unknown
import gr.questweaver.feature.onboarding.onboarding_registration_info_text
import gr.questweaver.feature.onboarding.onboarding_registration_input_placeholder
import gr.questweaver.feature.onboarding.onboarding_registration_random_button
import gr.questweaver.feature.onboarding.onboarding_registration_subtitle
import gr.questweaver.feature.onboarding.onboarding_registration_title
import gr.questweaver.feature.onboarding.onboarding_tutorial_button
import gr.questweaver.feature.onboarding.onboarding_tutorial_item_1
import gr.questweaver.feature.onboarding.onboarding_tutorial_item_3
import gr.questweaver.feature.onboarding.onboarding_tutorial_item_4
import gr.questweaver.feature.onboarding.onboarding_tutorial_policy_part_1
import gr.questweaver.feature.onboarding.onboarding_tutorial_policy_part_2
import gr.questweaver.feature.onboarding.onboarding_tutorial_privacy_policy
import gr.questweaver.feature.onboarding.onboarding_tutorial_terms_of_service
import gr.questweaver.feature.onboarding.onboarding_tutorial_title
import gr.questweaver.feature.onboarding.onboarding_welcome_button
import gr.questweaver.feature.onboarding.onboarding_welcome_subtitle
import gr.questweaver.feature.onboarding.onboarding_welcome_title
import org.jetbrains.compose.resources.getString

data class OnboardingStrings(
    val welcomeTitle: String = "",
    val welcomeSubtitle: String = "",
    val welcomeButton: String = "",
    val registrationTitle: String = "",
    val registrationSubtitle: String = "",
    val registrationInputPlaceholder: String = "",
    val registrationInfoText: String = "",
    val registrationCreateButton: String = "",
    val registrationRandomButton: String = "",
    val tutorialTitle: String = "",
    val tutorialItem1: String = "",
    val tutorialItem3: String = "",
    val tutorialItem4: String = "",
    val tutorialButton: String = "",
    // Policy strings can be handled specially or added here if simple
    val tutorialPolicyPart1: String = "",
    val tutorialPrivacyPolicy: String = "",
    val tutorialPolicyPart2: String = "",
    val tutorialTermsOfService: String = "",
    val errorUnknown: String = "",
) {
    companion object {
        val Empty = OnboardingStrings()

        suspend fun load(): OnboardingStrings =
            OnboardingStrings(
                welcomeTitle = getString(Res.string.onboarding_welcome_title),
                welcomeSubtitle = getString(Res.string.onboarding_welcome_subtitle),
                welcomeButton = getString(Res.string.onboarding_welcome_button),
                registrationTitle = getString(Res.string.onboarding_registration_title),
                registrationSubtitle =
                    getString(Res.string.onboarding_registration_subtitle),
                registrationInputPlaceholder =
                    getString(Res.string.onboarding_registration_input_placeholder),
                registrationInfoText =
                    getString(Res.string.onboarding_registration_info_text),
                registrationCreateButton =
                    getString(Res.string.onboarding_registration_create_button),
                registrationRandomButton =
                    getString(Res.string.onboarding_registration_random_button),
                tutorialTitle = getString(Res.string.onboarding_tutorial_title),
                tutorialItem1 = getString(Res.string.onboarding_tutorial_item_1),
                tutorialItem3 = getString(Res.string.onboarding_tutorial_item_3),
                tutorialItem4 = getString(Res.string.onboarding_tutorial_item_4),
                tutorialButton = getString(Res.string.onboarding_tutorial_button),
                tutorialPolicyPart1 =
                    getString(Res.string.onboarding_tutorial_policy_part_1),
                tutorialPrivacyPolicy =
                    getString(Res.string.onboarding_tutorial_privacy_policy),
                tutorialPolicyPart2 =
                    getString(Res.string.onboarding_tutorial_policy_part_2),
                tutorialTermsOfService =
                    getString(Res.string.onboarding_tutorial_terms_of_service),
                errorUnknown = getString(Res.string.onboarding_registration_error_unknown),
            )
    }
}
