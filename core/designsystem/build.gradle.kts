plugins {
    alias(libs.plugins.questweaver.android.library)
    alias(libs.plugins.questweaver.kotlin.multiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            api(compose.components.resources)
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = auto
}

android.namespace += ".core.designsystem"
