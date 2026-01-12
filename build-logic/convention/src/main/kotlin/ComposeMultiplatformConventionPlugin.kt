import gr.questweaver.buildlogic.configureCompose
import gr.questweaver.buildlogic.getLibrary
import gr.questweaver.buildlogic.getNamespace
import gr.questweaver.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<ComposeCompilerGradlePluginExtension>(::configureCompose)
            extensions.configure<ComposeExtension> {
                extensions.configure<ResourcesExtension> {
                    publicResClass = true
                    packageOfResClass = getNamespace()
                    generateResClass = always
                }
            }

            val kmpExtension = extensions.findByType(KotlinMultiplatformExtension::class.java)
            if (kmpExtension != null) {
                configureKotlinMultiplatformCompose(kmpExtension)
            } else {
                configureAndroidCompose()
            }
        }

    private fun Project.configureKotlinMultiplatformCompose(
        extension: KotlinMultiplatformExtension
    ) {
        extension.apply {
            sourceSets.apply {
                androidMain {
                    dependencies { implementation(libs.getLibrary("compose-ui-tooling").get()) }
                }
                commonMain {
                    dependencies {
                        implementation(libs.getLibrary("compose-ui").get())
                        implementation(libs.getLibrary("compose-ui-preview").get())
                        implementation(libs.getLibrary("compose-runtime").get())
                        implementation(libs.getLibrary("compose-foundation").get())
                        implementation(libs.getLibrary("compose-material3").get())
                        implementation(libs.getLibrary("compose-components-resources").get())
                        implementation(libs.getLibrary("compose-material3-adaptive").get())
                        implementation(libs.getLibrary("compose-viewmodel").get())
                        implementation(libs.getLibrary("compose-lifecycle-viewmodel").get())
                        implementation(libs.getLibrary("compose-viewmodel-nav3").get())
                    }
                }
            }
        }
    }

    private fun Project.configureAndroidCompose() {
        // Determine if it's an Android project (Application or Library)
        val isAndroid = extensions.findByName("android") != null
        if (isAndroid) {
            dependencies.apply {
                val implementation = "implementation"
                val debugImplementation = "debugImplementation"

                add(implementation, libs.getLibrary("compose-ui").get())
                add(implementation, libs.getLibrary("compose-ui-preview").get())
                add(implementation, libs.getLibrary("compose-runtime").get())
                add(implementation, libs.getLibrary("compose-foundation").get())
                add(implementation, libs.getLibrary("compose-material3").get())
                add(implementation, libs.getLibrary("compose-components-resources").get())
                add(implementation, libs.getLibrary("compose-material3-adaptive").get())
                add(implementation, libs.getLibrary("compose-viewmodel").get())
                add(implementation, libs.getLibrary("compose-lifecycle-viewmodel").get())
                add(implementation, libs.getLibrary("compose-viewmodel-nav3").get())

                add(debugImplementation, libs.getLibrary("compose-ui-tooling").get())
            }
        }
    }
}
