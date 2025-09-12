package gr.questweaver.core.domain

import gr.questweaver.core.data.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateUserUseCase(
    val userRepository: UserRepository,
    val getUserUseCase: GetUserUseCase,
    val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(
        userId: String,
        newName: String,
    ): Result<Unit> =
        withContext(defaultDispatcher) {
            getUserUseCase(userId).fold(
                onSuccess = { user ->
                    userRepository.save(user.copy(name = newName))
                },
                onFailure = { error ->
                    Result.failure(error)
                },
            )
        }
}
