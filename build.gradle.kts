plugins {
    kotlin("jvm") version "2.4.0"
    application

    id("com.gradleup.shadow") version "9.0.0"
}

group = "net.waclap"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.apache.commons:commons-jexl3:3.7.0")
    implementation("com.github.Waclap:CraftyLexerr:v1.1.1")
}

kotlin {
    jvmToolchain(26)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}