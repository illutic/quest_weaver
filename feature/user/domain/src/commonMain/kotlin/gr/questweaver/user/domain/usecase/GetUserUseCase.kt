package gr.questweaver.user.domain.usecase

import gr.questweaver.user.domain.User
import gr.questweaver.user.domain.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetUserUseCase(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Result<User> =
        withContext(dispatcher) {
            userRepository.getUser()
        }
}