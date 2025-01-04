import com.android.build.api.dsl.LibraryExtension
import gr.questweaver.build_logic.TARGET_SDK
import gr.questweaver.build_logic.configureAndroidLibrary
import gr.questweaver.build_logic.getPlugin
import gr.questweaver.build_logic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.getPlugin("androidLibrary"))
        }

        extensions.configure<LibraryExtension> {
            configureAndroidLibrary(this)

            defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            // The resource prefix is derived from the module name,
            // so resources inside ":core:module1" must be prefixed with "core_module1_"
            resourcePrefix = path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_").lowercase() + "_"

            testOptions {
                targetSdk = TARGET_SDK
                animationsDisabled = true
            }
        }

        dependencies {
            "implementation"(libs.findLibrary("androidx-core-ktx").get())
            "implementation"(libs.findLibrary("androidx-navigation").get())
            "implementation"(libs.findLibrary("kotlinx-coroutines-core").get())
            "implementation"(libs.findLibrary("kotlinx-coroutines-android").get())
            "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
        }
    }
}