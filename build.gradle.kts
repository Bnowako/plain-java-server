plugins {
    kotlin("jvm") version "1.7.0" apply false
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    repositories {
        mavenCentral()
    }
}