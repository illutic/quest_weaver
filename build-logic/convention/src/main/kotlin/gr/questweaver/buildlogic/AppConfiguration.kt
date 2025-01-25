package gr.questweaver.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureAndroidApp(applicationExtension: ApplicationExtension) {
    applicationExtension.apply {
        configureAndroidLibrary(this)

        defaultConfig {
            applicationId = "gr.questweaver.app.android"
            targetSdk = TARGET_SDK
            versionCode = property("gr.questweaver.version.code").toString().toInt()
            versionName = property("gr.questweaver.version.name").toString()
            manifestPlaceholders[APP_NAME_PLACEHOLDER] = property("gr.questweaver.app.name").toString()
        }

        buildTypes {
            debug {
                manifestPlaceholders[APP_NAME_PLACEHOLDER] =
                    property("gr.questweaver.app.name").toString() + " Debug"
            }
        }

        dependencies {
            "implementation"(libs.getLibrary("androidx-core-ktx").get())
        }
    }
}

internal fun Project.configureAndroidApp(kmpExtension: KotlinMultiplatformExtension) =
    kmpExtension.apply {
        androidTarget {
            compilations.all {
                compileTaskProvider.configure {
                    compilerOptions {
                        jvmTarget.set(JVM_TARGET)
                    }
                }
            }
        }
    }

internal fun Project.configureIosApp(kmpExtension: KotlinMultiplatformExtension) =
    kmpExtension.apply {
        listOf(
            iosArm64(),
            iosSimulatorArm64(),
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "ComposeApp"
                isStatic = true
                optimized = true
            }
        }
    }
