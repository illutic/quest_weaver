package gr.questweaver.user.data

import gr.questweaver.core.common.QuestWeaverDispatchers
import gr.questweaver.user.data.local.LocalUserDataSource
import gr.questweaver.user.domain.UserRepository
import gr.questweaver.user.domain.usecase.GetUserUseCase
import gr.questweaver.user.domain.usecase.IsUserRegisteredUseCase
import gr.questweaver.user.domain.usecase.SetUserUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val userDataModule = module {
    single<UserRepository> {
        UserRepositoryImpl(userDataSource = LocalUserDataSource(dao = get()))
    }
}

val userUseCasesModule = module {
    factory {
        GetUserUseCase(
            userRepository = get(),
            dispatcher = get(named(QuestWeaverDispatchers.Default))
        )
    }
    factory {
        IsUserRegisteredUseCase(
            getUserUseCase = get(),
            dispatcher = get(named(QuestWeaverDispatchers.Default))
        )
    }
    factory {
        SetUserUseCase(
            userRepository = get(),
            dispatcher = get(named(QuestWeaverDispatchers.Default))
        )
    }
}