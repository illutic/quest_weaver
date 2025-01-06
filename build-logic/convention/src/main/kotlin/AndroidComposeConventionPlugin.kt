import com.android.build.api.dsl.LibraryExtension
import gr.questweaver.buildlogic.configureAndroidCompose
import gr.questweaver.buildlogic.getPlugin
import gr.questweaver.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            with(pluginManager) {
                apply("questweaver.android.library")
                apply(libs.getPlugin("composeCompiler"))
            }

            configureAndroidCompose(extensions.getByType<LibraryExtension>())
        }
}
