package gr.questweaver.network

import kotlinx.serialization.json.Json

internal val JsonConfig =
    Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
        useArrayPolymorphism = false
    }
