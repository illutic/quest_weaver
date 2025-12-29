package gr.questweaver.buildlogic

import com.android.build.api.dsl.androidLibrary
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal val JAVA_VERSION = JavaVersion.VERSION_21
internal val JVM_TARGET = JvmTarget.JVM_21
internal const val COMPILE_SDK = 35
internal const val MIN_SDK = 24
internal const val TARGET_SDK = 35
internal const val APP_NAME_PLACEHOLDER = "appName"

internal fun Project.configureMultiplatformAndroidLibrary(kmpExtension: KotlinMultiplatformExtension) =
    kmpExtension.apply {
        androidLibrary {
            namespace = moduleName
            
            compileSdk {
                version = release(COMPILE_SDK)
            }

            minSdk {
                version = release(MIN_SDK)
            }

            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }

            withDeviceTest {
                targetSdk {
                    version = release(TARGET_SDK)
                }
                instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                execution = "ANDROIDX_TEST_ORCHESTRATOR"
                animationsDisabled = true
            }

            compilerOptions {
                jvmTarget.set(JVM_TARGET)
            }
        }
    }

internal fun Project.configureIosLibrary(kmpExtension: KotlinMultiplatformExtension) =
    kmpExtension.apply {
        listOf(
            iosArm64(),
            iosSimulatorArm64(),
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "QuestWeaver"
                isStatic = true
                optimized = !debuggable
                binaryOption("bundleId", moduleName)
            }
        }
    }

private val Project.moduleName: String
    get() {
        val moduleName = path
            .split(":")
            .drop(1)
            .joinToString(".")
        return if (moduleName.isNotEmpty()) "gr.questweaver.app.$moduleName" else "gr.questweaver.app"
    }