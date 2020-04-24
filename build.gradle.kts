group = "org.guivista"
version = "0.1-SNAPSHOT"

plugins {
    kotlin("multiplatform") version "1.3.72"
    `maven-publish`
}

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
}

kotlin {
    val guiVistaCoreVer = "0.1-SNAPSHOT"
    linuxX64("linuxX64") {
        compilations.getByName("main") {
            cinterops.create("gio2_x64") {
                includeDirs("/usr/include/glib-2.0/gio")
            }
            dependencies {
                implementation("org.guivista:guivista-core-linuxx64:$guiVistaCoreVer")
            }
        }
    }

    linuxArm32Hfp("linuxArm32") {
        compilations.getByName("main") {
            cinterops.create("gio2_arm32") {
                includeDirs("/mnt/pi_image/usr/include/glib-2.0/gio")
            }
            dependencies {
                implementation("org.guivista:guivista-core-linuxarm32:$guiVistaCoreVer")
            }
        }
    }

    sourceSets {
        val unsignedTypes = "kotlin.ExperimentalUnsignedTypes"

        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            languageSettings.useExperimentalAnnotation(unsignedTypes)
            dependencies {
                val kotlinVer = "1.3.72"
                implementation(kotlin("stdlib-common", kotlinVer))
                implementation("org.guivista:guivista-core:$guiVistaCoreVer")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val linuxX64Main by getting {
            languageSettings.useExperimentalAnnotation(unsignedTypes)
        }

        @Suppress("UNUSED_VARIABLE")
        val linuxArm32Main by getting {
            languageSettings.useExperimentalAnnotation(unsignedTypes)
        }
    }
}

tasks.create("createLinuxLibraries") {
    dependsOn("linuxX64MainKlibrary", "linuxArm32MainKlibrary")
}
