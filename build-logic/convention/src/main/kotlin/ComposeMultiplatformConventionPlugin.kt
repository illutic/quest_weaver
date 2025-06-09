import gr.questweaver.buildlogic.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
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

            val composeDeps = extensions.getByType(ComposeExtension::class.java).dependencies

            extensions.configure<ComposeCompilerGradlePluginExtension>(::configureCompose)

            ensureKotlinMultiplatformPluginIsApplied(this)
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    androidMain {
                        dependencies {
                            implementation(composeDeps.uiTooling)
                            implementation(composeDeps.preview)
                        }
                    }
                    commonMain {
                        dependencies {
                            implementation(composeDeps.runtime)
                            implementation(composeDeps.foundation)
                            implementation(composeDeps.material3)
                            implementation(composeDeps.materialIconsExtended)
                            implementation(composeDeps.ui)
                            implementation(composeDeps.components.resources)
                        }
                    }
                }
            }
        }
}
