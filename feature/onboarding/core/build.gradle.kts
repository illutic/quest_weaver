plugins {
    alias(libs.plugins.questweaver.kotlin.multiplatform)
    alias(libs.plugins.questweaver.compose.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.navigation)

            // Logic Dependencies
            implementation(projects.core.common)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.collections.immutable)

            // Domain Dependencies
            implementation(projects.feature.user.state) // Renamed to core? Check renaming
            implementation(projects.feature.home.core)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "gr.questweaver.onboarding"
    generateResClass = always
}
