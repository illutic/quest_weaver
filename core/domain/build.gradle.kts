plugins {
    alias(libs.plugins.questweaver.android.library)
    alias(libs.plugins.questweaver.kotlin.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.model)
        }
    }
}

android.namespace += ".core.domain"
