package gr.questweaver.ai

import gr.questweaver.navigation.Route

sealed interface AiRoute : Route {
    data object AiAssistant : AiRoute {
        override val id: String = "ai_assistant"
        override val path: String = "ai_assistant"
    }
}
