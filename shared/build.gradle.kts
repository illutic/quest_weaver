plugins {
    alias(libs.plugins.questweaver.kotlin.multiplatform)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "Shared"
            isStatic = true
            optimized = !debuggable

            export(projects.core.ui)
            export(projects.core.common)
            export(projects.core.database)
            export(projects.feature.onboarding)
            export(projects.feature.user.data)
            export(projects.feature.user.domain)
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.ui)
            api(projects.core.common)
            api(projects.core.database)
            api(projects.feature.onboarding)
            api(projects.feature.user.data)
            api(projects.feature.user.domain)
        }
    }
}

