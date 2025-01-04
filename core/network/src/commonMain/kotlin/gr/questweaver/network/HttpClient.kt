package gr.questweaver.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders

internal expect fun getHttpClient(): HttpClient

internal suspend fun HttpResponse.toHttpException() = HttpException(errorBody = bodyAsText(), code = status.value)

internal fun HttpClientConfig<*>.commonHttpClientConfig() {
    install(ContentNegotiation) {
        JsonConfig
    }

    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.ALL
        sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }
}
