package gr.questweaver.onboarding

import gr.questweaver.navigation.Route

sealed interface OnboardingRoute : Route {
    object Welcome : OnboardingRoute {
        override val path: String = "onboarding/welcome"
        override val id: String = "onboarding_welcome"
    }

    object Registration : OnboardingRoute {
        override val path: String = "onboarding/registration"
        override val id: String = "onboarding_registration"
    }

    object Tutorial : OnboardingRoute {
        override val path: String = "onboarding/tutorial"
        override val id: String = "onboarding_tutorial"
    }

    object Graph : OnboardingRoute {
        override val path: String = "onboarding/graph"
        override val id: String = "onboarding_graph"
    }
}
