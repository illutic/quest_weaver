plugins {
    alias(libs.plugins.questweaver.kotlin.multiplatform)
    alias(libs.plugins.questweaver.compose.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.ui)
            implementation(projects.core.components)
            implementation(projects.core.navigation)
            implementation(projects.feature.ai.core)
            implementation(projects.feature.bottombar)

            implementation(libs.compose.viewmodel)
            implementation(libs.compose.navigation3.ui)
        }
    }
}
