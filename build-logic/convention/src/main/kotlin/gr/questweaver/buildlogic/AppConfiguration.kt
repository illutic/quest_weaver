package gr.questweaver.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidApp(applicationExtension: ApplicationExtension) {
    applicationExtension.apply {
        buildFeatures {
            compose = true
        }

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }

        signingConfigs {
            create("release") {
                storeFile = file("$rootDir/keystore/questweaver")
                storePassword = getEnvOrWarn("KEYSTORE_PASSWORD").orEmpty()
                keyAlias = getEnvOrWarn("KEY_ALIAS").orEmpty()
                keyPassword = getEnvOrWarn("KEY_PASSWORD").orEmpty()
            }
        }

        defaultConfig {
            manifestPlaceholders[APP_NAME_PLACEHOLDER] = "QuestWeaver"
            applicationId = "gr.questweaver.app.android"
            namespace = "gr.questweaver.app.android"
            targetSdk {
                version = release(TARGET_SDK)
            }
            minSdk {
                version = release(MIN_SDK)
            }
            compileSdk {
                version = release(COMPILE_SDK)
            }
            versionCode = getEnvOrWarn("VERSION_CODE")?.toIntOrNull() ?: 1
            versionName = getEnvOrWarn("VERSION_NAME")
        }

        buildTypes {
            debug {
                manifestPlaceholders[APP_NAME_PLACEHOLDER] = "QuestWeaver Debug"
                isMinifyEnabled = false
                isDebuggable = true
                applicationIdSuffix = ".debug"
            }

            create("staging") {
                manifestPlaceholders[APP_NAME_PLACEHOLDER] = "QuestWeaver Staging"
                isDebuggable = false
                isMinifyEnabled = true
                isShrinkResources = true
                ndk.debugSymbolLevel = "FULL"
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                )
                applicationIdSuffix = ".staging"
                signingConfig = signingConfigs.getByName("debug")
            }

            release {
                manifestPlaceholders[APP_NAME_PLACEHOLDER] = "QuestWeaver"
                isDebuggable = false
                isMinifyEnabled = true
                isShrinkResources = true
                ndk.debugSymbolLevel = "FULL"
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                )
                signingConfig = signingConfigs.getByName("release")
            }
        }

        dependencies {
            val koinPlatform = libs.getLibrary("koin-bom").get()
            "implementation"(libs.getLibrary("androidx-core-ktx").get())
            "implementation"(libs.getLibrary("androidx-activity-compose").get())
            "implementation"(project.dependencies.platform(koinPlatform))
            "implementation"(libs.getLibrary("koin-android").get())
        }
    }
}
