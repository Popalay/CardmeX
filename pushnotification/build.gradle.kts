plugins {
    id("com.android.library")
    id("kotlin-android")
}

apply(from = "$rootDir/scripts/base_module.gradle")

dependencies {

    implementation(project(":api"))

    implementation(Libs.kotlin)
    implementation(Libs.picasso)
    implementation(Libs.rxJava)
    implementation(Libs.ktx)

    implementation(Libs.firebaseMessaging)
    implementation(Libs.firebaseFireStore)
    implementation(Libs.firebaseAuth)

    implementation(Libs.koinCore)
    implementation(Libs.koinAndroid)
}
