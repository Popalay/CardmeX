buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "https://maven.fabric.io/public")
        maven(url = "http://dl.bintray.com/kotlin/kotlin-eap")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.gradlePlugin}")
        classpath(kotlin("gradle-plugin", Versions.kotlinLanguage))
        classpath("android.arch.navigation:navigation-safe-args-gradle-plugin:${Versions.acNavigation}")
        classpath("com.google.gms:google-services:${Versions.googleServicesPlugin}")
        classpath("io.fabric.tools:gradle:${Versions.fabricPlugin}")
        classpath("org.jacoco:org.jacoco.core:${Versions.jacocoPlugin}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
        maven(url = "http://dl.bintray.com/kotlin/kotlin-eap")
    }
}

plugins {
    id("com.github.ben-manes.versions") version (Versions.versionsUpdatePlugin)
}

tasks {
    @Suppress("UNUSED_VARIABLE") val clean by creating(Delete::class) {
        delete(rootProject.buildDir)
    }
}