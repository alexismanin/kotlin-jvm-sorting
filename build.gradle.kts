import kotlinx.benchmark.gradle.*
import org.jetbrains.kotlin.allopen.gradle.*
import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.5.31"
    id("org.jetbrains.kotlinx.benchmark") version "0.3.1"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

configure<AllOpenExtension> {
    annotation("org.openjdk.jmh.annotations.State")
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.3.1")
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
    }
}

// TODO: remove once Kotlin support java 17
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

benchmark {
    targets {
        register("main") {
            this as JvmBenchmarkTarget
        }
    }

    // TODO: search why this is needed
    tasks.withType<org.gradle.jvm.tasks.Jar> { duplicatesStrategy = DuplicatesStrategy.INCLUDE }
}

