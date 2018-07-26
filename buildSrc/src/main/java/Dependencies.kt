@file:Suppress("unused")

object Versions {
    const val versionName = "0.0.1"

    const val minSdkVersion = 23
    const val targetSdkVersion = 28
    const val compileSdkVersion = 28

    const val gradlePlugin = "3.2.0-beta04"
    const val kotlinLanguage = "1.2.51"

    const val supportLibrary = "28.0.0-alpha3"
    const val constraintLayout = "1.1.2"
    const val ktx = "0.3"
    const val materialComponents = "1.0.0-beta01"

    const val rxJava = "2.1.17"
    const val rxAndroid = "2.0.2"
    const val rxKotlin = "2.2.0"

    const val rxBinding = "2.1.1"

    const val acLifecycle = "1.1.1"
    const val acRoom = "1.1.1"
    const val acNavigation = "1.0.0-alpha04"

    const val firebaseCore = "16.0.0"
    const val firebaseAuth = "16.0.2"
    const val playServicesAuth = "15.0.1"

    const val koinDi = "1.0.0-beta-3"

    const val kOptional = "1.5.0"
    const val timber = "4.6.0"
    const val picasso = "2.71828"
    const val androidProguards = "2.0.1"
    const val crashlytics = "2.9.3"
}

object Libs {

    // Google
    const val supportLib = "com.android.support:support-v4:${Versions.supportLibrary}"
    const val recyclerView = "com.android.support:recyclerview-v7:${Versions.supportLibrary}"
    const val cardView = "com.android.support:cardview-v7:${Versions.supportLibrary}"
    const val supportDesign = "com.android.support:design:${Versions.supportLibrary}"
    const val constraintLayout = "com.android.support.constraint:constraint-layout:${Versions.constraintLayout}"
    const val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    const val materialComponents = "com.google.android.material:material:${Versions.materialComponents}"

    // Firebase
    const val firebaseAuth = "com.google.firebase:firebase-auth:${Versions.firebaseAuth}"
    const val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebaseCore}"
    const val playServicesAuth = "com.google.android.gms:play-services-auth:${Versions.playServicesAuth}"

    // Kotlin
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinLanguage}"

    // RxJava
    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    const val rxBinding = "com.jakewharton.rxbinding2:rxbinding-kotlin:${Versions.rxBinding}"
    const val rxBindingDesign = "com.jakewharton.rxbinding2:rxbinding-design-kotlin:${Versions.rxBinding}"

    // Room
    const val roomRuntime = "android.arch.persistence.room:runtime:${Versions.acRoom}"
    const val roomRxJava = "android.arch.persistence.room:rxjava2:${Versions.acRoom}"
    const val roomCompiler = "android.arch.persistence.room:compiler:${Versions.acRoom}"

    // Navigation
    const val navigationFragment = "android.arch.navigation:navigation-fragment-ktx:${Versions.acNavigation}"
    const val navigationUi = "android.arch.navigation:navigation-ui:${Versions.acNavigation}"

    // DI
    const val koinAndroid = "org.koin:koin-android:${Versions.koinDi}"
    const val koinViewModel = "org.koin:koin-android-viewmodel:${Versions.koinDi}"
    const val koinAndroidScope = "org.koin:koin-android-scope:${Versions.koinDi}"

    // Lyfecycle
    const val lifecycle = "android.arch.lifecycle:extensions:${Versions.acLifecycle}"

    const val kOptional = "com.gojuno.koptional:koptional:${Versions.kOptional}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val picasso = "com.squareup.picasso:picasso:${Versions.picasso}"

    // Development
    const val androidProguards = "com.github.yongjhih.android-proguards:android-proguards:${Versions.androidProguards}"
    const val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}"
}

object ApiKeys {

    val googleClientId = System.getenv("BITRISEIO_GOOGLE_CLIENT_ID") ?: Secret.googleClientId
}

object SignIn {
    const val debugStoreFile = "../keys/debug.keystore"
    const val releaseStoreFile = "../keys/release.keystore"
    val storePassword  = System.getenv("BITRISEIO_ANDROID_KEYSTORE_PASSWORD") ?: Secret.storePassword

    val releaseXKeyAlias = System.getenv("BITRISEIO_ANDROID_KEYSTORE_ALIAS") ?: Secret.releaseXKeyAlias
    val releaseXKeyPassword = System.getenv("BITRISEIO_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD") ?: Secret.releaseXKeyPassword

    const val releaseKeyAlias = Secret.releaseKeyAlias
    const val releaseKeyPassword = Secret.releaseKeyPassword
}