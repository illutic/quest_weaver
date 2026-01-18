plugins {
    alias(libs.plugins.questweaver.kotlin.multiplatform)
    alias(libs.plugins.questweaver.compose.multiplatform)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "Shared"
            isStatic = true
            optimized = !debuggable

            export(projects.core.ui)
            export(projects.core.common)
            export(projects.core.database)
            export(projects.core.navigation)
            export(projects.feature.onboarding.core)
            export(projects.feature.onboarding.presentation)
            export(projects.feature.home.dashboard)
            export(projects.feature.home.core)
            export(projects.feature.user.data)
            export(projects.feature.user.state)
            export(projects.feature.ai.core)
            export(projects.feature.ai.presentation)
            export(projects.feature.search.core)
            export(projects.feature.search.presentation)
            export(projects.feature.bottombar)
            export(libs.androidx.lifecycle.viewmodel)
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.ui)
            api(projects.core.common)
            api(projects.core.database)
            api(projects.core.navigation)
            api(projects.feature.onboarding.core)
            api(projects.feature.onboarding.presentation)
            api(projects.feature.home.dashboard)
            api(projects.feature.home.core)
            api(projects.feature.user.data)
            api(projects.feature.user.state)
            api(projects.feature.ai.core)
            api(projects.feature.ai.presentation)
            api(projects.feature.search.core)
            api(projects.feature.search.presentation)
            api(projects.feature.bottombar)
        }
        iosMain.dependencies {
            api(libs.androidx.lifecycle.viewmodel)
        }
    }
}
