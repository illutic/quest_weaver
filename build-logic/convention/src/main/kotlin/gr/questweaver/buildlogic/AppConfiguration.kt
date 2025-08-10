package gr.questweaver.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureAndroidApp(applicationExtension: ApplicationExtension) {
    applicationExtension.apply {
        configureAndroidLibrary(this)

        signingConfigs {
            create("release") {
                storeFile = file("$rootDir/keystore/questweaver")
                storePassword = getEnvOrWarn("KEYSTORE_PASSWORD").orEmpty()
                keyAlias = getEnvOrWarn("KEY_ALIAS").orEmpty()
                keyPassword = getEnvOrWarn("KEY_PASSWORD").orEmpty()
            }
        }

        defaultConfig {
            applicationId = "gr.questweaver.app.android"
            targetSdk = TARGET_SDK
            versionCode = getEnvOrWarn("VERSION_CODE")?.toIntOrNull() ?: 1
            versionName = getEnvOrWarn("VERSION_NAME") ?: "1.0.0"
            manifestPlaceholders[APP_NAME_PLACEHOLDER] = "QuestWeaver"
            setProperty("archivesBaseName", "questweaver_v$versionName")
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
                setProperty("archivesBaseName", "questweaver_v${defaultConfig.versionName}")
            }
        }

        dependencies {
            "implementation"(libs.getLibrary("androidx-core-ktx").get())
            "implementation"(libs.getLibrary("androidx-activity-compose").get())
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
                baseName = "QuestWeaver"
                isStatic = true
                optimized = true
            }
        }
    }
