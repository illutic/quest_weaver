plugins {
    alias(libs.plugins.questweaver.app)
    alias(libs.plugins.kotlinAndroid)
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.core.database)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.user.data)
}