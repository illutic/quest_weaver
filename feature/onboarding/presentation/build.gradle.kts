plugins {
    alias(libs.plugins.questweaver.kotlin.multiplatform)
    alias(libs.plugins.questweaver.compose.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.feature.onboarding.core)

            implementation(projects.core.common)
            implementation(projects.core.ui)
            implementation(projects.core.components)
            implementation(projects.core.navigation)

            // ViewModel dependency
            implementation(libs.compose.viewmodel)
            implementation(libs.compose.lifecycle.viewmodel)
            implementation(libs.compose.navigation3.ui)
        }
    }
}
