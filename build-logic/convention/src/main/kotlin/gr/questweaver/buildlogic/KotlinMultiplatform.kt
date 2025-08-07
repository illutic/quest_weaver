package gr.questweaver.buildlogic

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureKotlinMultiplatform(kmpExtension: KotlinMultiplatformExtension) =
    kmpExtension.apply {
        configureMultiplatformAndroidLibrary(this)
        configureIosLibrary(this)

        applyDefaultHierarchyTemplate()

        sourceSets.apply {
            commonMain.dependencies {
                val koinBom = libs.getLibrary("koin-bom").get()
                implementation(libs.getLibrary("kotlinx-coroutines-core"))
                implementation(project.dependencies.platform(koinBom))
                implementation(libs.findLibrary("koin-core").get())
                implementation(libs.findLibrary("napier").get())
            }
            androidMain.dependencies {
                implementation(libs.findLibrary("androidx-core-ktx").get())
                implementation(libs.findLibrary("androidx-navigation").get())
                implementation(libs.findLibrary("kotlinx-coroutines-core").get())
                implementation(libs.findLibrary("kotlinx-coroutines-android").get())
                implementation(libs.findLibrary("koin-android").get())
            }
        }
    }

internal fun Project.configureCompose(extension: ComposeCompilerGradlePluginExtension) {
    extension.apply {
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
