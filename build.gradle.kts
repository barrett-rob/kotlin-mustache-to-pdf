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
    
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("org.thymeleaf:thymeleaf:3.1.1.RELEASE")
    implementation("org.xhtmlrenderer:flying-saucer-pdf:9.1.22")
    implementation("net.sf.jtidy:jtidy:r938")
}
