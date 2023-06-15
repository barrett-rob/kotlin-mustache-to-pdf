plugins {
    kotlin("jvm") version "1.7.22"
    application
}

application {
    mainClass.set("org.example.GeneratePdfKt")
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation("com.github.spullara.mustache.java:compiler:0.9.10")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("org.xhtmlrenderer:flying-saucer-pdf:9.1.22")
}
