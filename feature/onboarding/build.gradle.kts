plugins {
    alias(libs.plugins.questweaver.kotlin.multiplatform)
    alias(libs.plugins.questweaver.compose.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(projects.core.common)
            implementation(projects.core.navigation)
            implementation(projects.core.components)
            implementation(projects.feature.user.domain)
        }
    }
}
