plugins {
    id("com.android.library")
    id("kotlin-android")
    id("androidx.navigation.safeargs")
}

apply(from = "$rootDir/scripts/base_module.gradle")

dependencies {
    implementation(project(":core"))

    implementation(Libs.rxBinding)

    implementation(Libs.navigationFragment)
    implementation(Libs.navigationUi)

    implementation(Libs.koinAndroidScope)
    implementation(Libs.playServicesInstant)
}