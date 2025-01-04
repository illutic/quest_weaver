package gr.questweaver.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineDispatcher

internal class HttpDataSourceImpl(
    private val httpClient: HttpClient,
    private val ioDispatcher: CoroutineDispatcher,
) : HttpDataSource {
    private suspend inline fun <reified T> HttpResponse.transform(): Result<T> =
        when {
            status.isSuccess() -> body<T>().let { Result.success(it) }
            else -> Result.failure(toHttpException())
        }
}
