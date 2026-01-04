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
    private fun ensureKotlinMultiplatformPluginIsApplied(project: Project) =
        project.extensions.findByType(KotlinMultiplatformExtension::class.java) ?: error(
            "Kotlin Multiplatform plugin is not applied\n" +
                    "\nMake sure the order of the plugins is correct:\n\n" +
                    "plugins {\n" +
                    "\talias(libs.plugins.kotlin.multiplatform)\n" +
                    "\talias(libs.plugins.compose.multiplatform)\n" +
                    "}\n\n",
        )

    override fun apply(target: Project) =
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.getByType(ComposeExtension::class.java).dependencies

            ensureKotlinMultiplatformPluginIsApplied(this)
            extensions.configure<ComposeCompilerGradlePluginExtension>(::configureCompose)
            extensions.configure<ComposeExtension> {
                extensions.configure<ResourcesExtension> {
                    publicResClass = true
                    packageOfResClass = getNamespace()
                    generateResClass = always
                }
            }
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    androidMain {
                        dependencies {
                            implementation(libs.getLibrary("compose-ui-tooling").get())
                        }
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
}
