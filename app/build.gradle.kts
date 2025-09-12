plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.questweaver.compose.multiplatform)
    alias(libs.plugins.questweaver.app)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(projects.core.common)
            implementation(projects.core.data)
            implementation(projects.core.domain)
            implementation(projects.core.model)
            implementation(projects.feature.onboarding)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
