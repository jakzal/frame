plugins {
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(19)
}

tasks.test {
    useJUnitPlatform {
    }
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}
