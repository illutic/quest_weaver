package gr.questweaver.user.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class IsUserRegisteredUseCase(
    private val getUserUseCase: GetUserUseCase,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Result<Boolean> =
        withContext(dispatcher) {
            getUserUseCase().fold(
                onSuccess = { user ->
                    Result.success(user.id.isNotEmpty())
                },
                onFailure = { error ->
                    Result.failure(error)
                },
            )
        }
}
