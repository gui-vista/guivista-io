# GUI Vista IO (guivista-io)

A Kotlin Native library that provides Input/Output functionality. This library uses the 
[GIO](https://developer.gnome.org/gio/stable/) library. **Warning** - This library depends on Kotlin Native which is 
currently in beta, and doesn't provide any backwards compatibility guarantees! Currently, GUI Vista IO isn't 
available in Maven Central or JCenter, but is available in a remote GitLab Maven repository.


## Setup Gradle Build File

In order to use the library with Gradle (version 5.4 or higher) do the following:

1. Open/create a Kotlin Native project which targets **linuxX64** or **linuxArm32Hfp**
2. Open the project's **build.gradle.kts** file
3. Insert the following into the **repositories** block:
```kotlin
maven {
    val projectId = "16243425"
    url = uri("https://gitlab.com/api/v4/projects/$projectId/packages/maven")
}
```
4. Add the library dependency: `implementation("org.guivista:guivista-io-$target:$guiVistaVer")`

The build file should look similar to the following:
```kotlin
// ...
repositories {
    maven {
        val projectId = "16243425"
        url = uri("https://gitlab.com/api/v4/projects/$projectId/packages/maven")
    }
}

kotlin {
    // ...
    // Replace with linuxArm32Hfp if that target is used in the project.
    linuxX64 {
        // ...
        compilations.getByName("main") {
            dependencies {
                // For a ARM v7 based SBC use the linuxarm32 target.
                val target = "linuxx64"
                val guiVistaVer = "0.1"
                implementation("org.guivista:guivista-io-$target:$guiVistaVer")
            }
        }
    }
}
```

## Extracting File Information

In order to obtain information about a file you will need to be using an existing 
[File](src/commonMain/kotlin/org/guiVista/io/File.kt) instance. With this instance the `queryInfo` function is used to 
get file information via a [FileInfo](src/commonMain/kotlin/org/guiVista/io/FileInfo.kt) instance, provided the file 
already exists.


## Enumerating Files

Files can be enumerated via the `enumerateChildren` function on a 
[File](src/commonMain/kotlin/org/guiVista/io/File.kt) instance. The 
[FileEnumerator](src/commonMain/kotlin/org/guiVista/io/FileEnumerator.kt) class is responsible for the actual 
enumeration taking place. On a FileEnumerator instance the `nextFile` function is used to go over each file until 
there are no more files to enumerate. Below is an example:

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
