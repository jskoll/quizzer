/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.3.3/userguide/building_java_projects.html
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    java

}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    maven("https://jitpack.io")
}

kotlin {
    sourceSets {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.4")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
                implementation ("com.google.code.gson:gson:2.8.5")
                implementation("com.github.ajalt.mordant:mordant:2.0.0-beta4")
                implementation("com.github.kotlin-inquirer:kotlin-inquirer:0.1.0")
                implementation("io.github.microutils:kotlin-logging-jvm:2.1.20")
                implementation(group="ch.qos.logback", name="logback-classic", version="1.2.6")
            }
    }
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:30.1.1-jre")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    // Define the main class for the application.
    mainClass.set("quizzer.AppKt")
}
