import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val korge_version: String by project
val kotlin_version: String by project
val logback_version: String by project

//buildscript {
//    val korgePluginVersion: String by project
//
//    repositories {
//        mavenLocal()
//        mavenCentral()
//        google()
//        maven { url = uri("https://plugins.gradle.org/m2/") }
//    }
//    dependencies {
//        classpath("com.soywiz.korlibs.korge.plugins:korge-gradle-plugin:$korgePluginVersion")
//    }
//}
//
//apply<KorgeGradlePlugin>()

plugins {
    application
    kotlin("jvm") version "1.7.0"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
//    mavenLocal()
    mavenCentral()
//    google()
//    maven { url = uri("https://plugins.gradle.org/m2/") }
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-websockets:$ktor_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
//    implementation("io.ktor:ktor-server-netty:$ktor_version")
//    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")


    implementation("com.github.sarxos:webcam-capture:0.3.12")
    implementation("com.soywiz.korlibs.korio:korio:$korge_version")
    implementation("com.soywiz.korlibs.korge2:korge:$korge_version")
    implementation(kotlin("stdlib-jdk8"))
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}