plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.questweaver.compose.multiplatform)
    alias(libs.plugins.questweaver.app)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
        }
    }
}
