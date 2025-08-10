plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.questweaver.compose.multiplatform)
    alias(libs.plugins.questweaver.app)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
        }
    }
}

base {
    archivesName =
        if (android.defaultConfig.versionName != null) "questweaver-v${android.defaultConfig.versionName}" else "questweaver"
}
