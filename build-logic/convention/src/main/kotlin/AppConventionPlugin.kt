import com.android.build.api.dsl.ApplicationExtension
import gr.questweaver.buildlogic.configureAndroidApp
import gr.questweaver.buildlogic.configureIosApp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class AppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
            val appExtension = extensions.getByType<ApplicationExtension>()
            configureIosApp(kmpExtension)
            configureAndroidApp(kmpExtension)
            configureAndroidApp(appExtension)
        }
}
