plugins {
    id("com.android.library")
    id("kotlin-android")
}

apply(from = "$rootDir/scripts/base_module.gradle")

dependencies {
    implementation(project(":core"))

    implementation(Libs.rxBinding)

    implementation(Libs.koinAndroidScope)
    implementation(Libs.navigationFragment)
}
