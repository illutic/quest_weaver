plugins {
    alias(libs.plugins.questweaver.kotlin.multiplatform)
    alias(libs.plugins.questweaver.compose.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.ai.core)
            implementation(projects.feature.ai.presentation)
            implementation(projects.feature.home.core)
            implementation(projects.feature.home.dashboard)
            implementation(projects.feature.home.search.core)
            implementation(projects.feature.home.search.presentation)
            implementation(projects.feature.settings.core)
            implementation(projects.feature.settings.presentation)
            implementation(projects.core.navigation)
            implementation(projects.core.ui)
            implementation(projects.core.components)

            implementation(libs.compose.viewmodel)
            implementation(libs.compose.navigation3.ui)
            implementation(libs.compose.material.icons.extended)
        }
    }
}