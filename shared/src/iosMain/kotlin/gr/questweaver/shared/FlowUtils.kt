package gr.questweaver.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

inline fun <T : Any> Flow<T>.subscribe(
    scope: CoroutineScope,
    crossinline onError: (Throwable) -> Unit = {},
    crossinline onNext: (T) -> Unit,
) {
    scope.launch {
        try {
            collect {
                onNext(it)
            }
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            onError(t)
        }
    }
}
