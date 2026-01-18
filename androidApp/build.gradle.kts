plugins {
    alias(libs.plugins.questweaver.app)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.questweaver.compose.multiplatform)
}

dependencies {
    implementation(projects.shared)
    implementation(libs.koin.android)
    implementation(libs.androidx.splashscreen)
    implementation(libs.compose.navigation3.ui)
    implementation(projects.feature.home.dashboard)
    implementation(projects.feature.home.create)
    implementation(projects.feature.home.resources)
    implementation(projects.feature.home.core)
    implementation(libs.compose.material3)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.kotlinx.collections.immutable)
}
