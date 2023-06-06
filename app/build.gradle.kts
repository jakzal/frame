
plugins {
    // We do not include any of the convention plugins from build-logic here
    // as the multiplatform plugin already does some similar setup.
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}


repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(19)
        withJava()
    }
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}
