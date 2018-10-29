@file:Suppress("unused")

object Versions {
    const val versionName = "0.1.5"
    val versionCode = (System.getenv("BITRISE_BUILD_NUMBER") ?: "40").toInt()

    const val minSdkVersion = 23
    const val targetSdkVersion = 28
    const val compileSdkVersion = 28

    const val gradlePlugin = "3.3.0-beta01"
    const val kotlinLanguage = "1.3.0"

    const val constraintLayout = "2.0.0-alpha2"
    const val ktx = "1.0.0"
    const val materialComponents = "1.0.0"

    const val rxJava = "2.2.3"
    const val rxAndroid = "2.1.0"
    const val rxKotlin = "2.3.0"

    const val rxBinding = "2.2.0"

    const val acLifecycle = "2.0.0"
    const val acRoom = "2.0.0"
    const val acNavigation = "1.0.0-alpha06"

    const val firebaseCore = "16.0.4"
    const val firebaseAuth = "16.0.5"
    const val firebaseStorage = "16.0.4"
    const val firebaseFireStore = "17.1.2"
    const val firebaseMessaging = "17.3.4"
    const val firebaseDynamicLinks = "16.1.2"

    const val playServicesAuth = "16.0.1"
    const val playServicesInstant = "16.0.1"

    const val koinDi = "1.0.1"
    const val hoard = "1.0.0"

    const val kOptional = "1.6.0"
    const val timber = "4.6.0"
    const val picasso = "2.71828"
    const val crashlytics = "2.9.5"
    const val stetho = "1.5.0"

    const val kotlinTest = "3.1.10"
}

object Libs {

    // Google
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    const val materialComponents = "com.google.android.material:material:${Versions.materialComponents}"

    // Firebase
    const val firebaseAuth = "com.google.firebase:firebase-auth:${Versions.firebaseAuth}"
    const val firebaseCommon = "com.google.firebase:firebase-common:${Versions.firebaseCore}"
    const val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebaseCore}"
    const val firebaseFireStore = "com.google.firebase:firebase-firestore:${Versions.firebaseFireStore}"
    const val firebaseStorage = "com.google.firebase:firebase-storage:${Versions.firebaseStorage}"
    const val firebaseMessaging = "com.google.firebase:firebase-messaging:${Versions.firebaseMessaging}"
    const val firebaseDynamicLinks = "com.google.firebase:firebase-dynamic-links:${Versions.firebaseDynamicLinks}"

    const val playServicesAuth = "com.google.android.gms:play-services-auth:${Versions.playServicesAuth}"
    const val playServicesInstant = "com.google.android.gms:play-services-instantapps:${Versions.playServicesInstant}"

    // Kotlin
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinLanguage}"

    // RxJava
    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    const val rxBinding = "com.jakewharton.rxbinding2:rxbinding-kotlin:${Versions.rxBinding}"

    // Room
    const val roomRuntime = "androidx.room:room-runtime:${Versions.acRoom}"
    const val roomRxJava = "androidx.room:room-rxjava2:${Versions.acRoom}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.acRoom}"

    // Navigation
    const val navigationFragment = "android.arch.navigation:navigation-fragment-ktx:${Versions.acNavigation}"
    const val navigationUi = "android.arch.navigation:navigation-ui:${Versions.acNavigation}"

    // DI
    const val koinCore = "org.koin:koin-core:${Versions.koinDi}"
    const val koinAndroid = "org.koin:koin-android:${Versions.koinDi}"
    const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koinDi}"
    const val koinAndroidScope = "org.koin:koin-androidx-scope:${Versions.koinDi}"
    const val koinTest = "org.koin:koin-test:${Versions.koinDi}"

    // Lifecycle
    const val lifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.acLifecycle}"

    // Utils
    const val kOptional = "com.gojuno.koptional:koptional:${Versions.kOptional}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val picasso = "com.squareup.picasso:picasso:${Versions.picasso}"
    const val hoard = "com.github.popalay:hoard:${Versions.hoard}"

    // Development
    const val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}"
    const val stetho = "com.facebook.stetho:stetho:${Versions.stetho}"

    // Test
    const val kotlinTestCore = "io.kotlintest:kotlintest-core:${Versions.kotlinTest}"
    const val kotlinTestAssertions = "io.kotlintest:kotlintest-assertions:${Versions.kotlinTest}"
    const val kotlinTestRunner = "io.kotlintest:kotlintest-runner-junit5:${Versions.kotlinTest}"
}

object ApiKeys {

    val googleClientId = Secret.googleClientId
}

object SignIn {
    const val debugStoreFile = "../keys/debug.keystore"
    const val releaseStoreFile = "../keys/release.keystore"
    val storePassword = System.getenv("BITRISEIO_ANDROID_KEYSTORE_PASSWORD") ?: Secret.storePassword

    val releaseXKeyAlias = System.getenv("BITRISEIO_ANDROID_KEYSTORE_ALIAS") ?: Secret.releaseXKeyAlias
    val releaseXKeyPassword = System.getenv("BITRISEIO_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD") ?: Secret.releaseXKeyPassword

    const val releaseKeyAlias = Secret.releaseKeyAlias
    const val releaseKeyPassword = Secret.releaseKeyPassword
}