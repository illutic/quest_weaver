plugins {
    alias(libs.plugins.questweaver.kotlin.multiplatform)
    alias(libs.plugins.questweaver.compose.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.navigation)
            implementation(libs.kotlinx.collections.immutable)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "gr.questweaver.home.core"
    generateResClass = always
}
