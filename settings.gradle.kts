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
include(":feature:onboarding:core")
include(":feature:onboarding:presentation")
include(
    ":feature:home:core",
    ":feature:home:dashboard",
    ":feature:home:recents",
    ":feature:home:resources",
    ":feature:home:create"
)
include(":feature:ai:core", ":feature:ai:presentation")
include(":feature:search:core", ":feature:search:presentation")
include(":feature:settings:core", ":feature:settings:presentation")
include(":feature:user:data", ":feature:user:state")
include(":core:common", ":core:navigation", ":core:components", ":core:ui", ":core:database")
include(":feature:bottombar")
