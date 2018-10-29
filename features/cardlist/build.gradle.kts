plugins {
    id("com.android.library")
    id("kotlin-android")
}

apply(from = "$rootDir/scripts/base_module.gradle")

dependencies {
    implementation(project(":core"))
    implementation(project(":features:cardactions"))

    implementation(Libs.rxBinding)

    implementation(Libs.navigationFragment)
    implementation(Libs.navigationUi)

    implementation(Libs.koinAndroidScope)
}