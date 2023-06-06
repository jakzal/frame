
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
        @Suppress("UNUSED_VARIABLE")
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.compose.ui:ui-test-junit4:${extra["compose.version"]}")
                implementation("org.junit.vintage:junit-vintage-engine:5.6.3")
            }
        }
    }
}

tasks {
    @Suppress("UNUSED_VARIABLE")
    val jvmTest by getting(Test::class) {
        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = true
        }
    }
}

compose.desktop {
    application {
        mainClass = "pl.zalas.frame.app.MainKt"
    }
}
