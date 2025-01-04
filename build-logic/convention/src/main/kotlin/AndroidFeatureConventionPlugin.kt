import gr.questweaver.build_logic.getPlugin
import gr.questweaver.build_logic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.getPlugin("composeCompiler"))
            apply(libs.getPlugin("kotlinSerialization"))
            apply("questweaver.android.library")
        }

        dependencies {
            "implementation"(project(":core:ui"))
            "implementation"(project(":core:designsystem"))
            val koinBom = libs.findLibrary("koin-bom").get()
            "implementation"(platform(koinBom))
            "implementation"(libs.findLibrary("koin-androidx-compose").get())
            "implementation"(libs.findLibrary("koin-core").get())
        }
    }
}