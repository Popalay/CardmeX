import Versions.compileSdkVersion
import Versions.minSdkVersion
import Versions.targetSdkVersion
import Versions.versionCode
import Versions.versionName
import com.android.build.gradle.AppExtension
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCallArgument.DefaultArgument.arguments

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

apply(from = "$rootDir/scripts/base_module.gradle")

android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":api"))

    implementation(Libs.kotlin)

    implementation(Libs.rxJava)
    implementation(Libs.rxKotlin)

    implementation(Libs.koinCore)
    implementation(Libs.kOptional)

    implementation(Libs.roomRuntime)
    implementation(Libs.roomRxJava)
    kapt(Libs.roomCompiler)
}