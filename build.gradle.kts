// import org.jetbrains.kotlin.gradle.internal.encodePluginOptions

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.20-Beta2"
    id("org.jetbrains.intellij") version "1.16.0"
    // kotlin("jvm") version "1.9.10"
}

group = "com.sqy"
version = "1.0-SNAPSHOT"

kotlin{
    jvmToolchain(11)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.microutils","kotlin-logging-jvm","2.0.11")
    //implementation("net.java.dev.jna","jna","5.13.0")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2021.3.3")
    type.set("IC") // Target IDE Platform
    plugins.set(listOf("com.intellij.java","org.jetbrains.kotlin"))

}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
        options.encoding = "utf-8"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    listBundledPlugins {

    }

    patchPluginXml {
        sinceBuild.set("213")
        untilBuild.set("232.*")
    }

//    signPlugin {
//        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
//        privateKey.set(System.getenv("PRIVATE_KEY"))
//        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
//    }
//
//    publishPlugin {
//        token.set(System.getenv("PUBLISH_TOKEN"))
//    }
}

