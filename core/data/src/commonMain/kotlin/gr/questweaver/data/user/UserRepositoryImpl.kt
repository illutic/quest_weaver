package gr.questweaver.data.user

import ScopeExecutorImpl
import gr.questweaver.common.coroutines.provideIoDispatcher
import gr.questweaver.domain.repository.UserRepository
import gr.questweaver.local.user.UserLocalDataSource
import gr.questweaver.local.user.provideUserLocalDataSource
import gr.questweaver.local.user.toDomain
import gr.questweaver.local.user.toDto
import gr.questweaver.model.User
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.dsl.module

val userModule =
    module {
        provideUserLocalDataSource()
        single<UserRepository> {
            UserRepositoryImpl(
                userLocalDataSource = get(),
                dispatcher = provideIoDispatcher(),
            )
        }
    }

private class UserRepositoryImpl(
    private val userLocalDataSource: UserLocalDataSource,
    private val dispatcher: CoroutineDispatcher,
) : UserRepository,
    ScopeExecutor by ScopeExecutorImpl(dispatcher) {
    override suspend fun getUser(): Result<User> =
        tryExecuteInScope {
            userLocalDataSource.getUser()?.toDomain() ?: throw NoUserError()
        }

    override suspend fun saveUser(user: User): Result<User> =
        tryExecuteInScope {
            userLocalDataSource.saveUser(user.toDto()).toDomain()
        }

    override suspend fun removeUser(id: String): Result<Unit> =
        tryExecuteInScope {
            userLocalDataSource.clearUser()
        }
}
