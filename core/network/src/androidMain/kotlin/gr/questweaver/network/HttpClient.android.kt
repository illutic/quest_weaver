package gr.questweaver.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual fun getHttpClient() =
    HttpClient(OkHttp) {
        commonHttpClientConfig()
    }
