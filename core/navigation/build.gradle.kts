plugins {
    alias(libs.plugins.questweaver.kotlin.multiplatform)
    alias(libs.plugins.questweaver.compose.multiplatform)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            api(libs.androidx.navigation.runtime)
            api(libs.androidx.navigation.ui)
            api(libs.androidx.material3.adaptive.navigation3)
        }
    }
}