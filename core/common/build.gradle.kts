plugins {
    alias(libs.plugins.questweaver.android.library)
    alias(libs.plugins.questweaver.kotlin.multiplatform)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            api(libs.slf4j)
        }
        commonMain.dependencies {
            api(libs.kotlinx.coroutines.core)
            api(project.dependencies.platform(libs.koin.bom))
            api(libs.koin.core)
        }
    }
}

android.namespace += ".core.common"
