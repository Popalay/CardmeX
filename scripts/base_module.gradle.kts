import Versions.compileSdkVersion
import Versions.minSdkVersion
import Versions.targetSdkVersion
import Versions.versionCode
import Versions.versionName
import com.android.build.gradle.AppExtension

android {
    compileSdkVersion(Versions.compileSdkVersion)
    defaultConfig {
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)
        versionName = Versions.versionName
        versionCode = Versions.versionCode

        consumerProguardFiles("proguard-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}