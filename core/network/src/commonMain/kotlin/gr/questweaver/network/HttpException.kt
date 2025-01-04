package gr.questweaver.network

inline fun <reified T> Result<T>.getHttpException(): HttpException? =
    when (val exception = exceptionOrNull()) {
        is HttpException -> exception
        else -> null
    }

data class HttpException(
    val code: Int? = null,
    val errorBody: String? = null,
) : Throwable(errorBody)
