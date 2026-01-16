package gr.questweaver.onboarding

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
        val Default = OnboardingStrings()
    }
}
