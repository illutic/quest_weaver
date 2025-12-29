package gr.questweaver.user.data

import gr.questweaver.core.common.QuestWeaverDispatchers
import gr.questweaver.user.data.local.LocalUserDataSource
import gr.questweaver.user.domain.usecase.GetUserUseCase
import gr.questweaver.user.domain.usecase.IsUserRegisteredUseCase
import gr.questweaver.user.domain.usecase.SetUserUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val userDataModule = module {
    single {
        UserRepositoryImpl(userDataSource = LocalUserDataSource(dao = get()))
    }
    includes(userUseCasesModule)
}

private val userUseCasesModule = module {
    single {
        GetUserUseCase(
            userRepository = get(),
            dispatcher = get(named<QuestWeaverDispatchers.Default>())
        )
    }
    single {
        IsUserRegisteredUseCase(
            getUserUseCase = get(),
            dispatcher = get(named<QuestWeaverDispatchers.Default>())
        )
    }
    single {
        SetUserUseCase(
            userRepository = get(),
            dispatcher = get(named<QuestWeaverDispatchers.Default>())
        )
    }
}