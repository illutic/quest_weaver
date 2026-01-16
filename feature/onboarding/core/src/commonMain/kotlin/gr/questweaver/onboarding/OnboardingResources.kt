package gr.questweaver.onboarding

import org.jetbrains.compose.resources.getString

suspend fun loadOnboardingStrings(): OnboardingStrings =
    OnboardingStrings(
        welcomeTitle = getString(Res.string.onboarding_welcome_title),
        welcomeSubtitle = getString(Res.string.onboarding_welcome_subtitle),
        welcomeButton = getString(Res.string.onboarding_welcome_button),
        registrationTitle = getString(Res.string.onboarding_registration_title),
        registrationSubtitle = getString(Res.string.onboarding_registration_subtitle),
        registrationInputPlaceholder =
            getString(Res.string.onboarding_registration_input_placeholder),
        registrationInfoText = getString(Res.string.onboarding_registration_info_text),
        registrationCreateButton =
            getString(Res.string.onboarding_registration_create_button),
        registrationRandomButton =
            getString(Res.string.onboarding_registration_random_button),
        tutorialTitle = getString(Res.string.onboarding_tutorial_title),
        tutorialItem1 = getString(Res.string.onboarding_tutorial_item_1),
        tutorialItem3 = getString(Res.string.onboarding_tutorial_item_3),
        tutorialItem4 = getString(Res.string.onboarding_tutorial_item_4),
        tutorialButton = getString(Res.string.onboarding_tutorial_button),
        tutorialPolicyPart1 = getString(Res.string.onboarding_tutorial_policy_part_1),
        tutorialPrivacyPolicy = getString(Res.string.onboarding_tutorial_privacy_policy),
        tutorialPolicyPart2 = getString(Res.string.onboarding_tutorial_policy_part_2),
        tutorialTermsOfService = getString(Res.string.onboarding_tutorial_terms_of_service),
        errorUnknown = getString(Res.string.onboarding_registration_error_unknown),
    )
