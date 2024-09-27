import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "3.0.0-rc-1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20"
}

group = "uk.kulikov.dnd"
version = "0.0.1"

application {
    mainClass.set("uk.kulikov.dnd.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    compilerOptions {

        jvmTarget = JvmTarget.JVM_1_8
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-default-headers-jvm")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.google.firebase:firebase-admin:9.3.0")

    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

}
