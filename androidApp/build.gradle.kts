plugins {
    alias(libs.plugins.questweaver.app)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.questweaver.compose.multiplatform)
}

dependencies {
    implementation(projects.shared)
    implementation(libs.koin.android)
    implementation(libs.androidx.splashscreen)
}