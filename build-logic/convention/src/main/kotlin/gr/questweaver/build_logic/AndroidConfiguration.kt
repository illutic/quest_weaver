package gr.questweaver.build_logic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

internal val JAVA_VERSION = JavaVersion.VERSION_17
internal val JVM_TARGET = JvmTarget.JVM_17
internal const val COMPILE_SDK = 35
internal const val MIN_SDK = 24
internal const val TARGET_SDK = 24

internal fun Project.configureAndroidApp(
    applicationExtension: ApplicationExtension,
) {
    applicationExtension.apply {
        configureAndroidLibrary(this)

        namespace += ".android"

        defaultConfig {
            applicationId = "gr.questweaver.android"
            targetSdk = TARGET_SDK
            versionCode = 1
            versionName = "1.0"
        }

        dependencies {
            "implementation"(libs.getLibrary("androidx-core-ktx").get())
        }
    }
}

internal fun configureAndroidLibrary(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) = with (commonExtension) {
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

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            "implementation"(platform(bom))
            "androidTestImplementation"(platform(bom))
            "implementation"(libs.findLibrary("androidx-ui-tooling-preview").get())
            "debugImplementation"(libs.findLibrary("androidx-ui-tooling").get())
        }
    }

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }
        fun Provider<*>.relativeToRootProject(dir: String) = map {
            isolated.rootProject.projectDirectory
                .dir("build")
                .dir(projectDir.toRelativeString(rootDir))
        }.map { it.dir(dir) }

        project.providers.gradleProperty("enableComposeCompilerMetrics").onlyIfTrue()
            .relativeToRootProject("compose-metrics")
            .let(metricsDestination::set)

        project.providers.gradleProperty("enableComposeCompilerReports").onlyIfTrue()
            .relativeToRootProject("compose-reports")
            .let(reportsDestination::set)

        stabilityConfigurationFiles
            .add(isolated.rootProject.projectDirectory.file("compose_compiler_config.conf"))
    }
}