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
            api(projects.feature.home.core)
            implementation(projects.feature.home.recents)
            implementation(projects.feature.home.resources)
            implementation(projects.feature.home.create)

            implementation(libs.compose.viewmodel)
            implementation(libs.compose.lifecycle.viewmodel)
            implementation(libs.compose.navigation3.ui)
        }
    }
}
