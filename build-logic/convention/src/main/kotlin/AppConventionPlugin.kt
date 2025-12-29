import com.android.build.api.dsl.ApplicationExtension
import gr.questweaver.buildlogic.configureAndroidApp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            val appExtension = extensions.getByType<ApplicationExtension>()
            configureAndroidApp(appExtension)
        }
}
