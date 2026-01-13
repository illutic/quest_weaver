plugins {
    alias(libs.plugins.questweaver.kotlin.multiplatform)
    alias(libs.plugins.questweaver.compose.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.compose.navigation3.ui)
            api(libs.compose.material3.adaptive)
        }
    }
}
