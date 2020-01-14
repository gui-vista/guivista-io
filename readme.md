# GUI Vista IO (guivista-io)

A Kotlin Native library that provides Input/Output functionality. This library uses the 
[GIO](https://developer.gnome.org/gio/stable/) library. **Warning** - This library depends on Kotlin Native which is 
currently in beta, and doesn't provide any backwards compatibility guarantees!


## Publish Library

Currently GUI Vista IO isn't available in Maven Central, JCenter, or any other remote Maven repository. Do the 
following to publish the library:

1. Clone this repository
2. Change working directory to where the repository has been cloned to
3. Publish the library locally via Gradle, eg `./gradlew publishLinuxX64PublicationToMavenLocal`


## Setup Gradle Build File

In order to use the library with Gradle do the following:

1. Open/create a Kotlin Native project which targets **linuxX64**
2. Open the project's **build.gradle.kts** file
3. Insert `mavenLocal()` into the **repositories** block
4. Add the library dependency: `implementation("org.guivista:guivista-io:0.1-SNAPSHOT")`

The build file should look similar to the following:
```kotlin
// ...
repositories {
    mavenLocal()
}

kotlin {
    // ...
    linuxX64() {
        // ...
        compilations.getByName("main") {
            dependencies {
                val guiVistaVer = "0.1-SNAPSHOT"
                implementation("org.guivista:guivista-io-linuxx64:$guiVistaVer")
            }
        }
    }
}
```


## Extracting File Information

In order to obtain information about a file you will need to be using an existing 
[File](src/linuxX64Main/kotlin/org/guiVista/io/File.kt) instance. With this instance the `queryInfo` function is used to 
get file information via a [FileInfo](src/linuxX64Main/kotlin/org/guiVista/io/FileInfo.kt) instance, provided the file 
already exists.


## Enumerating Files

Files can be enumerated via the `enumerateChildren` function on a 
[File](src/linuxX64Main/kotlin/org/guiVista/io/File.kt) instance. The 
[FileEnumerator](src/linuxX64Main/kotlin/org/guiVista/io/FileEnumerator.kt) class is responsible for the actual 
enumeration taking place. On a FileEnumerator instance the `nextFile` function is used to go over each file until there 
are no more files to enumerate. Below is a example:

```kotlin
// ...
fun fetchImageFiles(dir: File): Array<String> {
    val tmp = mutableListOf<String>()
    val fileType = dir.queryFileType(G_FILE_QUERY_INFO_NONE)
    if (fileType == G_FILE_TYPE_DIRECTORY) {
        val enumerator = dir.enumerateChildren(G_FILE_ATTRIBUTE_STANDARD_NAME, 0u)
        var fileInfo = enumerator?.nextFile()
        while (fileInfo != null) {
            tmp += fileInfo.name
            fileInfo.close()
            fileInfo = enumerator?.nextFile()
        }
        enumerator?.close()
    }
    return tmp.filter { it.endsWith(PNG_EXT) || it.endsWith(TIF_EXT) || it.endsWith(JPEG_EXT) }.toTypedArray()
}
// ...
```
