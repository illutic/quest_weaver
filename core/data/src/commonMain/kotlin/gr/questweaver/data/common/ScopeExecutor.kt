import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

interface ScopeExecutor {
    suspend fun <T> tryExecuteInScope(block: suspend () -> T): Result<T>
}

class ScopeExecutorImpl(
    private val dispatcher: CoroutineDispatcher,
) : ScopeExecutor {
    override suspend fun <T> tryExecuteInScope(block: suspend () -> T): Result<T> =
        withContext(dispatcher) {
            try {
                Result.success(block())
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Result.failure(e)
            }
        }
}
