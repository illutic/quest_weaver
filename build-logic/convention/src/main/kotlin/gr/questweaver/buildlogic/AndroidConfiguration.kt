package gr.questweaver.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

internal val JAVA_VERSION = JavaVersion.VERSION_21
internal val JVM_TARGET = JvmTarget.JVM_21
internal const val COMPILE_SDK = 35
internal const val MIN_SDK = 24
internal const val TARGET_SDK = 35
internal const val APP_NAME_PLACEHOLDER = "appName"

internal fun Project.configureAndroidLibrary(commonExtension: CommonExtension<*, *, *, *, *, *>) =
    with(commonExtension) {
        val moduleName = path.split(":").drop(2).joinToString(".")
        namespace = if (moduleName.isNotEmpty()) "gr.questweaver.app.$moduleName" else "gr.questweaver.app"
        compileSdk = COMPILE_SDK

        defaultConfig {
            minSdk = MIN_SDK
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }

        buildTypes {
            getByName("debug") {
                isMinifyEnabled = false
            }
            getByName("release") {
                isMinifyEnabled = true
            }
        }

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }
