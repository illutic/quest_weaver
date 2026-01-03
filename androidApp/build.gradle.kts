plugins {
    alias(libs.plugins.questweaver.app)
    alias(libs.plugins.kotlinAndroid)
}

dependencies {
    implementation(projects.shared)
    implementation(libs.koin.android)
    implementation(libs.androidx.splashscreen)
}