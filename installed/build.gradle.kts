import Versions.compileSdkVersion
import Versions.minSdkVersion
import Versions.targetSdkVersion
import Versions.versionCode
import Versions.versionName
import com.android.build.gradle.AppExtension
import groovy.util.Eval.x

plugins {
    id("com.android.application")
    id("io.fabric")
    id("com.google.gms.google-services")
}

android {
    compileSdkVersion(Versions.compileSdkVersion)
    defaultConfig {
        applicationId = "com.popalay.cardme"
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)
        versionName = Versions.versionName
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file(SignIn.debugStoreFile)
        }

        create("release") {
            storeFile = file(SignIn.releaseStoreFile)
            keyAlias = SignIn.releaseKeyAlias
            keyPassword = SignIn.releaseKeyPassword
            storePassword = SignIn.storePassword
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            extra["enableCrashlytics"] = false
        }

        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    flavorDimensions("type", "env", "experience")
    productFlavors {

        register("dev") {
            setDimension("env")
            versionNameSuffix = "-dev"
            extra["enableCrashlytics"] = false
            resConfigs("en", "xxhdpi")
        }

        register("prod") {
            setDimension("env")
        }

        create("x") {
            setDimension("type")
            applicationIdSuffix = ".x"
            resValue("string", "app_name", "CardmeX")

            signingConfigs {

                getByName("debug") {
                    storeFile = file(SignIn.debugStoreFile)
                }

                getByName("release") {
                    storeFile = file(SignIn.releaseStoreFile)
                    keyAlias = SignIn.releaseXKeyAlias
                    keyPassword = SignIn.releaseXKeyPassword
                    storePassword = SignIn.storePassword
                }
            }
        }

        register("full") {
            setDimension("type")
            resValue("string", "app_name", "Cardme")
        }

        register("instant") {
            setDimension("experience")
            versionCode = Versions.versionCode
        }

        register("installed") {
            setDimension("experience")
            versionCode = Versions.versionCode + 1
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":features:main"))
    implementation(project(":features:cardlist"))
    implementation(project(":features:addcard"))
    implementation(project(":features:usercard"))
    implementation(project(":features:cardactions"))
    implementation(project(":features:carddetails"))

    implementation(Libs.navigationUi)
}