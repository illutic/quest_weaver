plugins {
    alias(libs.plugins.questweaver.android.app)
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.model)
    implementation(projects.core.common)
}
