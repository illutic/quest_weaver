plugins {
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.questweaver.kotlin.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.model)
            implementation(projects.core.common)
            implementation(libs.kotlinx.serialization.protobuf)
        }

        androidMain.dependencies {
            implementation(libs.androidx.datastore)
        }
    }
}
