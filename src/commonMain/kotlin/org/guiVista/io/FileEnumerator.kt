package org.guiVista.io

import org.guiVista.core.Closable
import org.guiVista.core.Error

expect class FileEnumerator : Closable {
    val closeError: Error?
    val iterateError: Error?
    val nextFileError: Error?
    val isClosed: Boolean

    /** The [File] container which is being enumerated. */
    val container: File

    /** Indicates if there are pending operations for the enumerator. */
    var pendingOperations: Boolean

    /**
     * Fetches a new [File] which refers to the file named by info in the source directory of enumerator. This
     * function is primarily intended to be used inside loops with [nextFile].
     * @param info A [FileInfo] fetched from [nextFile], or the async equivalents.
     * @return A [File] for the [FileInfo] passed it.
     */
    fun fetchChild(info: FileInfo): File

    /**
     * Returns information for the next file in the enumerated object. Will block until the information is available.
     * The [FileInfo] returned from this function will contain attributes that match the attribute string that was
     * passed when the [FileEnumerator] was created. See the documentation of GFileEnumerator for information about the
     * order of returned files. On error returns *null*, and sets error to the error. If the enumerator is at the end,
     * *null* will be returned, and error will be unset.
     * @return A [FileInfo] instance, or *null* on error, or end of enumerator.
     */
    fun nextFile(): FileInfo?

    /**
     * This is a version of [nextFile] that's easier to use correctly from Kotlin programs. With [nextFile] the return
     * value signifies *"end of iteration or error"*, which requires allocation of a temporary [Error]. In contrast
     * with this function a *false* return from g_file_enumerator_iterate() **ALWAYS** means "error". End of iteration
     * is signaled by [outInfo] or [outChild] being *null*.
     *
     * Another crucial difference is that the references for [outInfo] and [outChild] are owned by this enumerator
     * (they are cached as hidden properties). You **MUST NOT** unreference them in your own code. This makes memory
     * management significantly easier for Kotlin code in combination with loops. Finally this function optionally
     * allows retrieving a [File] as well.
     *
     * You **MUST** specify at least one of [outInfo] or [outChild].
     */
    fun iterate(outInfo: Array<FileInfo>, outChild: File?): Boolean
}