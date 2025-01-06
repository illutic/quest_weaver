package gr.questweaver.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.getLibrary(libraryName: String) = findLibrary(libraryName).get()

fun VersionCatalog.getPlugin(pluginName: String): String = findPlugin(pluginName).get().get().pluginId
