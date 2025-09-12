package gr.questweaver.di.domain

import gr.questweaver.core.common.QuestWeaverDispatchers
import gr.questweaver.core.domain.GetUserUseCase
import gr.questweaver.core.domain.UpdateUserUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val userUseCasesModule =
    module {
        single {
            GetUserUseCase(
                userRepository = get(),
                defaultDispatcher = get(named<QuestWeaverDispatchers.Default>()),
            )
        }
        single {
            UpdateUserUseCase(
                userRepository = get(),
                getUserUseCase = get(),
                defaultDispatcher = get(named<QuestWeaverDispatchers.Default>()),
            )
        }
    }
