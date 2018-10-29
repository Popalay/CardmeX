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
        classpath("com.google.gms:google-services:4.0.1")
        classpath("io.fabric.tools:gradle:1.26.1")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.20.0")
        classpath("org.jacoco:org.jacoco.core:0.8.2")
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
    id("com.github.ben-manes.versions") version ("0.20.0")
}

tasks {
    @Suppress("UNUSED_VARIABLE") val clean by creating(Delete::class) {
        delete(rootProject.buildDir)
    }
}