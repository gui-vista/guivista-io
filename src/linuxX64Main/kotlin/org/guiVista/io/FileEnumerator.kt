package org.guiVista.io

import gio2.*
import glib2.GError
import glib2.TRUE
import glib2.g_object_unref
import kotlinx.cinterop.*
import org.guiVista.core.Closable
import org.guiVista.core.Error

class FileEnumerator(fileEnumeratorPtr: CPointer<GFileEnumerator>) : Closable {
    private val arena = Arena()
    val gFileEnumeratorPtr: CPointer<GFileEnumerator>? = fileEnumeratorPtr
    private val closeErrorPtrVar = arena.alloc<CPointerVar<GError>>()
    val closeError: Error?
        get() {
            val tmp = closeErrorPtrVar.value
            return if (tmp != null) Error.fromErrorPtr(tmp) else null
        }
    val isClosed: Boolean
        get() = g_file_enumerator_is_closed(gFileEnumeratorPtr) == TRUE
    /** The [File] container which is being enumerated. */
    val container: File
        get() = File.fromFilePtr(g_file_enumerator_get_container(gFileEnumeratorPtr))

    /**
     * Fetches a new [File], which refers to the file named by info in the source directory of enumerator. This
     * function is primarily intended to be used inside loops with [nextFile].
     * @param info A [FileInfo] fetched from [nextFile], or the async equivalents.
     * @return A [File] for the [FileInfo] passed it.
     */
    fun fetchChild(info: FileInfo): File =
        File.fromFilePtr(g_file_enumerator_get_child(gFileEnumeratorPtr, info.gFileInfoPtr))

    /**
     * Releases all resources used by this enumerator making the enumerator return *G_IO_ERROR_CLOSED* on all calls.
     * This will be automatically called when the last reference is dropped, but you might want to call this function
     * to make sure resources are released as early as possible.
     */
    override fun close() {
        g_file_enumerator_close(enumerator = gFileEnumeratorPtr, cancellable = null, error = closeErrorPtrVar.ptr)
        g_object_unref(gFileEnumeratorPtr)
        arena.clear()
    }

    /**
     * Returns information for the next file in the enumerated object. Will block until the information is available.
     * The [FileInfo] returned from this function will contain attributes that match the attribute string that was
     * passed when the [FileEnumerator] was created. See the documentation of GFileEnumerator for information about the
     * order of returned files. On error returns *null*, and sets error to the error. If the enumerator is at the end,
     * *null* will be returned, and error will be unset.
     * @return A [FileInfo] instance, or *null* on error, or end of enumerator.
     */
    fun nextFile(): FileInfo? {
        val tmp = g_file_enumerator_next_file(enumerator = gFileEnumeratorPtr, cancellable = null, error = null)
        return if (tmp != null) FileInfo(tmp) else null
    }
}
