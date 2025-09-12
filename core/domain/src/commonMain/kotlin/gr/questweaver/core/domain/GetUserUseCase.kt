package gr.questweaver.core.domain

import gr.questweaver.core.data.UserRepository
import gr.questweaver.core.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetUserUseCase(
    val userRepository: UserRepository,
    val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(userId: String): Result<User> = withContext(defaultDispatcher) { userRepository.get(userId) }
}
