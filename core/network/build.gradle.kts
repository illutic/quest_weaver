plugins {
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.questweaver.android.library)
    alias(libs.plugins.questweaver.kotlin.multiplatform)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.android)
        }
        commonMain.dependencies {
            implementation(projects.core.model)
            implementation(projects.core.common)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.contentNegotiation)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}


android.namespace += ".core.network"

