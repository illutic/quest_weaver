import gr.questweaver.build_logic.configureKotlinMultiplatform
import gr.questweaver.build_logic.getLibrary
import gr.questweaver.build_logic.getPlugin
import gr.questweaver.build_logic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KMPConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            with(pluginManager) {
                apply(libs.getPlugin("kotlinMultiplatform"))
            }

            extensions.configure<KotlinMultiplatformExtension> {
                configureKotlinMultiplatform(this)

                sourceSets.apply {
                    commonMain.dependencies {
                        val koinBom = libs.getLibrary("koin-bom").get()
                        api(libs.getLibrary("kotlinx-coroutines-core"))
                        api(project.dependencies.platform(koinBom))
                        api(libs.findLibrary("koin-core").get())
                    }
                    androidMain.dependencies {
                        api(libs.findLibrary("koin-android").get())
                    }
                }
            }
        }
}
