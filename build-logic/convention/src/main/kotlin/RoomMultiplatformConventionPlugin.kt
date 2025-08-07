import androidx.room.gradle.RoomExtension
import gr.questweaver.buildlogic.getLibrary
import gr.questweaver.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class RoomMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("androidx.room")
            }

            dependencies {
                val roomCompiler = libs.getLibrary("androidx-room-compiler").get()
                add("kspAndroid", roomCompiler)
                add("kspIosSimulatorArm64", roomCompiler)
                add("kspIosArm64", roomCompiler)
            }

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(libs.getLibrary("androidx-room-runtime").get())
                        implementation(libs.getLibrary("androidx-sqlite-bundled").get())
                    }
                }

                listOf(
                    iosArm64(),
                    iosSimulatorArm64(),
                ).forEach { iosTarget ->
                    iosTarget.binaries.forEach {
                        it.linkerOpts.add("-lsqlite3")
                    }
                }
            }

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }
        }
    }
}