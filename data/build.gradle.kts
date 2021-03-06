plugins {
    id("com.android.library")
    id("kotlin-android")
}

apply(from = "$rootDir/scripts/base_module.gradle")

dependencies {

    implementation(project(":api"))

    implementation(Libs.kotlin)

    implementation(Libs.rxJava)
    implementation(Libs.rxKotlin)

    implementation(Libs.koinCore)
    implementation(Libs.kOptional)
}
