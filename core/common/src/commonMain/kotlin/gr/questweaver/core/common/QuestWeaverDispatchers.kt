package gr.questweaver.core.common

sealed interface QuestWeaverDispatchers {
    data object IO : QuestWeaverDispatchers

    data object Default : QuestWeaverDispatchers

    data object Main : QuestWeaverDispatchers
}
