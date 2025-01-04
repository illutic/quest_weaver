package gr.questweaver.build_logic

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun configureKotlinMultiplatform(
    kmpExtension: KotlinMultiplatformExtension
) = kmpExtension.apply {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JVM_TARGET)
                }
            }
        }
    }

    applyDefaultHierarchyTemplate()

    listOf(
        iosArm64(),
        iosX64(),
        iosSimulatorArm64()
    )
}