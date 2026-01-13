package gr.questweaver.user.domain.usecase

import gr.questweaver.user.domain.User
import gr.questweaver.user.domain.UserRepository
import gr.questweaver.user.domain.exception.InvalidUserNameException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SetUserUseCase(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    @OptIn(ExperimentalUuidApi::class)
    private fun generateUserId(): String = Uuid.random().toString()

    suspend operator fun invoke(name: String): Result<User> =
        withContext(dispatcher) {
            runCatching {
                val name = name.trim().takeIf { it.isNotBlank() } ?: throw InvalidUserNameException()
                val existingUser = userRepository.getUser().getOrNull()
                val userToSet =
                    existingUser?.copy(name = name)
                        ?: User(
                            id = generateUserId(),
                            name = name,
                        )

                userRepository.setUser(userToSet).getOrThrow()
            }
        }
}
