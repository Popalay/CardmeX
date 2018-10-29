import Versions.compileSdkVersion
import Versions.minSdkVersion
import Versions.targetSdkVersion
import Versions.versionCode
import Versions.versionName

plugins {
    id("com.android.library")
    id("kotlin-android")
}

apply(from = "$rootDir/scripts/base_module.gradle")

android {
    defaultConfig {
        buildConfigField("String", "GOOGLE_CLIENT_ID", ApiKeys.googleClientId)
    }
}

dependencies {
    implementation(project(":api"))

    implementation(Libs.kotlin)

    implementation(Libs.rxJava)

    implementation(Libs.firebaseAuth)
    implementation(Libs.playServicesAuth)

    implementation(Libs.kOptional)

    implementation(Libs.koinCore)
    implementation(Libs.koinAndroid)
}
