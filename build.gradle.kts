group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("multiplatform") version "1.3.61"
    `maven-publish`
}

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
}

kotlin {
    linuxX64("linuxX64") {
        compilations.getByName("main") {
            cinterops.create("gio2") {
                includeDirs("/usr/include/glib-2.0/gio")
            }
            dependencies {
                val guiVistaCoreVer = "1.0-SNAPSHOT"
                implementation("org.guivista:guivista-core-linuxx64:$guiVistaCoreVer")
            }
        }
    }

    sourceSets {
        @Suppress("UNUSED_VARIABLE") val linuxX64Main by getting {
            languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
        }
    }
}
