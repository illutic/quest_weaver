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
        }
    }
}


android.namespace += ".core.designsystem"