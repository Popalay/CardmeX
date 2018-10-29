import Versions.compileSdkVersion
import Versions.minSdkVersion
import Versions.targetSdkVersion
import Versions.versionCode
import Versions.versionName
import com.android.build.gradle.AppExtension
import groovy.util.Eval.x

plugins {
    id("com.android.library")
    id("kotlin-android")
}

apply(from = "$rootDir/scripts/base_module.gradle")

android {
    defaultConfig {
        consumerProguardFiles("proguard-rules.pro")
        buildConfigField("String", "GOOGLE_CLIENT_ID", ApiKeys.googleClientId)
    }
}

dependencies {
    api(project(":api"))
    api(Libs.kotlin)
    api(Libs.materialComponents)
    api(Libs.constraintLayout)
    api(Libs.rxJava)
    api(Libs.rxAndroid)
    api(Libs.rxKotlin)
    api(Libs.koinAndroid)
    api(Libs.koinViewModel)
    api(Libs.kOptional)
    api(Libs.ktx)
    api(Libs.picasso)

    implementation(project(":cache"))
    implementation(project(":remote"))
    implementation(project(":data"))
    implementation(project(":authenticator"))
    implementation(project(":pushnotification"))

    implementation(Libs.lifecycle)
    implementation(Libs.navigationFragment)
    implementation(Libs.navigationUi)
    implementation(Libs.crashlytics)
    implementation(Libs.stetho)
}