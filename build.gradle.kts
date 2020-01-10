group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("multiplatform") version "1.3.61"
}

kotlin {
    linuxX64("linuxX64")
}
