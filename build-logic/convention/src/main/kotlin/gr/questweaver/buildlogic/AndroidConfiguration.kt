package gr.questweaver.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.utils.property

internal val JAVA_VERSION = JavaVersion.VERSION_21
internal val JVM_TARGET = JvmTarget.JVM_21
internal const val COMPILE_SDK = 35
internal const val MIN_SDK = 24
internal const val TARGET_SDK = 35
internal const val APP_NAME_PLACEHOLDER = "appName"

internal fun Project.configureAndroidApp(applicationExtension: ApplicationExtension) {
    applicationExtension.apply {
        configureAndroidLibrary(this)

        namespace += ".android"

        defaultConfig {
            applicationId = "gr.questweaver.android"
            targetSdk = TARGET_SDK
            versionCode = property("gr.questweaver.version.code").toString().toInt()
            versionName = property("gr.questweaver.version.name").toString()
            manifestPlaceholders[APP_NAME_PLACEHOLDER] = property("gr.questweaver.app.name").toString()
        }

        buildTypes {
            debug {
                manifestPlaceholders[APP_NAME_PLACEHOLDER] = property("gr.questweaver.app.name").toString() + " Debug"
            }
        }

        dependencies {
            "implementation"(libs.getLibrary("androidx-core-ktx").get())
        }
    }
}

internal fun configureAndroidLibrary(commonExtension: CommonExtension<*, *, *, *, *, *>) =
    with(commonExtension) {
        namespace = "gr.questweaver"
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

@Suppress("UnstableApiUsage")
internal fun Project.configureAndroidCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            "implementation"(platform(bom))
            "androidTestImplementation"(platform(bom))
            "implementation"(libs.findLibrary("coil").get())
            "implementation"(libs.findLibrary("coil-compose").get())
            "implementation"(libs.findLibrary("androidx-ui-tooling-preview").get())
            "implementation"(libs.findLibrary("androidx-ui").get())
            "implementation"(libs.findLibrary("androidx-compose-animation").get())
            "implementation"(libs.findLibrary("androidx-ui-graphics").get())
            "implementation"(libs.findLibrary("androidx-material3").get())
            "implementation"(libs.findLibrary("androidx-activity-compose").get())
            "implementation"(libs.findLibrary("androidx-navigation").get())
            "implementation"(libs.findLibrary("androidx-splashscreen").get())
            "implementation"(libs.findLibrary("androidx-material3-adaptive-navigation-suite-android").get())
            "implementation"(libs.findLibrary("androidx-adaptive-android").get())
            "debugImplementation"(libs.findLibrary("androidx-ui-test-manifest").get())
            "debugImplementation"(libs.findLibrary("androidx-ui-tooling").get())
        }
    }

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }

        fun Provider<*>.relativeToRootProject(dir: String) =
            map {
                isolated.rootProject.projectDirectory
                    .dir("build")
                    .dir(projectDir.toRelativeString(rootDir))
            }.map { it.dir(dir) }

        project.providers
            .gradleProperty("enableComposeCompilerMetrics")
            .onlyIfTrue()
            .relativeToRootProject("compose-metrics")
            .let(metricsDestination::set)

        project.providers
            .gradleProperty("enableComposeCompilerReports")
            .onlyIfTrue()
            .relativeToRootProject("compose-reports")
            .let(reportsDestination::set)

        stabilityConfigurationFiles
            .add(isolated.rootProject.projectDirectory.file("compose_compiler_config.conf"))
    }
}
