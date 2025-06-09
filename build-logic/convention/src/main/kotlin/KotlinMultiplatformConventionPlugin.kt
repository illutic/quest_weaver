import com.android.build.api.dsl.LibraryExtension
import gr.questweaver.buildlogic.TARGET_SDK
import gr.questweaver.buildlogic.configureAndroidLibrary
import gr.questweaver.buildlogic.configureKotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
            }

            extensions.configure<LibraryExtension> {
                configureAndroidLibrary(this)

                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                // The resource prefix is derived from the module name,
                // so resources inside ":core:module1" must be prefixed with "core_module1_"
                resourcePrefix = path
                    .split("""\W""".toRegex())
                    .drop(1)
                    .distinct()
                    .joinToString(separator = "_")
                    .lowercase() + "_"

                testOptions {
                    targetSdk = TARGET_SDK
                    animationsDisabled = true
                }
            }

            extensions.configure<KotlinMultiplatformExtension>(::configureKotlinMultiplatform)
        }
}
