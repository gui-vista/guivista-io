import java.util.Properties

val gitLabSettings = fetchGitLabSettings()
val projectSettings = fetchProjectSettings()
val bintraySettings = fetchBintraySettings()

group = "org.guivista"
version = if (projectSettings.isDevVer) "${projectSettings.libVer}-dev" else projectSettings.libVer

plugins {
    kotlin("multiplatform") version "1.3.72"
    `maven-publish`
}

repositories {
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://dl.bintray.com/guivista/public")
    }
}

kotlin {
    linuxX64("linuxX64") {
        compilations.getByName("main") {
            dependencies {
                implementation("org.guivista:guivista-core-linuxx64:${projectSettings.libVer}")
            }
            cinterops.create("gio2") {
                includeDirs("/usr/include/glib-2.0/gio")
            }
        }
    }

    linuxArm32Hfp("linuxArm32") {
        compilations.getByName("main") {
            dependencies {
                implementation("org.guivista:guivista-core-linuxarm32:${projectSettings.libVer}")
            }
            cinterops.create("gio2") {
                includeDirs("/mnt/pi_image/usr/include/glib-2.0/gio")
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
                implementation("org.guivista:guivista-core:${projectSettings.libVer}")
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

publishing {
    if (gitLabSettings.publishingEnabled) createGitLabRepository()
    if (bintraySettings.publishingEnabled) createBintrayRepository()
}

tasks.create("createLinuxLibraries") {
    dependsOn("linuxX64MainKlibrary", "linuxArm32MainKlibrary")
}

tasks.getByName("publish") {
    doFirst { println("Project Version: ${project.version}") }
}

data class GitLabSettings(val token: String, val projectId: Int, val publishingEnabled: Boolean)

fun fetchGitLabSettings(): GitLabSettings {
    var token = ""
    var projectId = -1
    val properties = Properties()
    var publishingEnabled = true
    file("gitlab.properties").inputStream().use { inputStream ->
        properties.load(inputStream)
        publishingEnabled = properties.getProperty("publishingEnabled")?.toBoolean() ?: true
        token = properties.getProperty("token") ?: ""
        @Suppress("RemoveSingleExpressionStringTemplate")
        projectId = "${properties.getProperty("projectId")}".toInt()
    }
    return GitLabSettings(token = token, projectId = projectId, publishingEnabled = publishingEnabled)
}

data class ProjectSettings(val libVer: String, val isDevVer: Boolean)

fun fetchProjectSettings(): ProjectSettings {
    var libVer = "SNAPSHOT"
    var isDevVer = true
    val properties = Properties()
    file("project.properties").inputStream().use { inputStream ->
        properties.load(inputStream)
        libVer = properties.getProperty("libVer") ?: "SNAPSHOT"
        @Suppress("RemoveSingleExpressionStringTemplate")
        isDevVer = "${properties.getProperty("isDevVer")}".toBoolean()
    }
    return ProjectSettings(libVer = libVer, isDevVer = isDevVer)
}

val Boolean.intValue: Int
    get() = if (this) 1 else 0

fun PublishingExtension.createBintrayRepository() {
    fun MavenArtifactRepository.applyCredentials() = credentials {
        username = bintraySettings.username
        password = bintraySettings.apiKey
    }

    val baseUrl = "https://api.bintray.com/maven"
    val publish = false.intValue
    val repoUrl = "$baseUrl/${bintraySettings.org}/${bintraySettings.repo}/${project.name}/;publish=$publish"
    // Create repository.
    repositories.maven(repoUrl) {
        name = bintraySettings.repo
        applyCredentials()
    }
}

fun PublishingExtension.createGitLabRepository() {
    repositories.maven {
        val projectId = gitLabSettings.projectId
        url = uri("https://gitlab.com/api/v4/projects/$projectId/packages/maven")
        credentials(HttpHeaderCredentials::class.java) {
            name = "Private-Token"
            value = gitLabSettings.token
        }
        authentication {
            create("header", HttpHeaderAuthentication::class.java)
        }
    }
}

data class BintraySettings(val username: String, val apiKey: String, val org: String, val repo: String,
                           val publishingEnabled: Boolean)

fun fetchBintraySettings(): BintraySettings {
    val filePath = "bintray.properties"
    val properties = Properties()
    var username = ""
    var apiKey = ""
    var org = ""
    var repo = ""
    var publishingEnabled = true
    if (file(filePath).exists()) {
        file(filePath).bufferedReader().use { br ->
            properties.load(br)
            username = properties.getProperty("username") ?: ""
            apiKey = properties.getProperty("apiKey") ?: ""
            org = properties.getProperty("org") ?: ""
            repo = properties.getProperty("repo") ?: ""
            publishingEnabled = properties.getProperty("publishingEnabled")?.toBoolean() ?: true
        }
    }
    return BintraySettings(username = username, apiKey = apiKey, org = org, repo = repo,
        publishingEnabled = publishingEnabled)
}
