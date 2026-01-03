import Shared

extension Kotlinx_coroutines_coreFlow {
    func subscribe<A: AnyObject>(
        scope: Kotlinx_coroutines_coreCoroutineScope,
        onError: @escaping (KotlinThrowable) -> Void,
        onNext: @escaping (A?) -> Void
    ) {
        FlowUtilsKt.subscribe(
            self,
            scope: scope,
            onError: onError,
            onNext: { value in onNext(value as? A) }
        )
    }
}
