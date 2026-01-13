package gr.questweaver.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.getLibrary(libraryName: String) = findLibrary(libraryName).get()

fun getEnvOrWarn(name: String): String? =
    System.getenv(name) ?: run {
        println("WARN: Environment variable '$name' is not set.")
        null
    }

fun Project.getNamespace(): String {
    val moduleName =
        path
            .split(":")
            .drop(1)
            .joinToString(".")

    return if (moduleName.isNotEmpty()) {
        "gr.questweaver.$moduleName"
    } else {
        "gr.questweaver.app"
    }
}
