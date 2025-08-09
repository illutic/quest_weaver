package gr.questweaver.buildlogic

import org.apache.log4j.helpers.LogLog.warn
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.getLibrary(libraryName: String) = findLibrary(libraryName).get()

fun getEnvOrWarn(name: String): String? =
    System.getenv(name) ?: run {
        warn("Environment variable '$name' is not set.")
        null
    }
