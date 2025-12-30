enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "QuestWeaver"
include(":androidApp")
include(":shared")
include(":feature:onboarding")
include(":feature:user:data", ":feature:user:domain")
include(":core:common", ":core:navigation", ":core:ui", ":core:database")
