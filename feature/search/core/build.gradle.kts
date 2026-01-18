plugins {
    alias(libs.plugins.questweaver.kotlin.multiplatform)
    alias(libs.plugins.questweaver.compose.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.navigation)
            implementation(projects.core.ui)
            implementation(projects.feature.bottombar)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.collections.immutable)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "gr.questweaver.search.core"
    generateResClass = always
}
