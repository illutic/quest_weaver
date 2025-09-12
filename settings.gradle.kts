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
include(":app")
include(":feature:onboarding")
include(":core:common")
include(":core:data")
include(":core:domain")
include(":core:model")
include(":core:navigation")
include(":core:ui")
