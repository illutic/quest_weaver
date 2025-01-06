import com.android.build.api.dsl.ApplicationExtension
import gr.questweaver.buildlogic.configureAndroidApp
import gr.questweaver.buildlogic.configureAndroidCompose
import gr.questweaver.buildlogic.getPlugin
import gr.questweaver.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            with(pluginManager) {
                apply(libs.getPlugin("kotlinAndroid"))
                apply(libs.getPlugin("androidApplication"))
                apply(libs.getPlugin("composeCompiler"))
            }

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidApp(extension)
            configureAndroidCompose(extension)
        }
}
