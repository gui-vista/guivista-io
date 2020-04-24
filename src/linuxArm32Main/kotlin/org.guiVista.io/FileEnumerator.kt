package org.guiVista.io

import gio2.*
import glib2.GError
import glib2.TRUE
import glib2.g_object_unref
import kotlinx.cinterop.*
import org.guiVista.core.Closable
import org.guiVista.core.Error

actual class FileEnumerator(fileEnumeratorPtr: CPointer<GFileEnumerator>) : Closable {
    private val arena = Arena()
    val gFileEnumeratorPtr: CPointer<GFileEnumerator>? = fileEnumeratorPtr
    private val closeErrorPtrVar = arena.alloc<CPointerVar<GError>>()
    actual val closeError: Error?
        get() {
            val tmp = closeErrorPtrVar.value
            return if (tmp != null) Error.fromErrorPtr(tmp) else null
        }
    actual val isClosed: Boolean
        get() = g_file_enumerator_is_closed(gFileEnumeratorPtr) == TRUE
    actual val container: File
        get() = File.fromFilePtr(g_file_enumerator_get_container(gFileEnumeratorPtr))

    actual fun fetchChild(info: FileInfo): File =
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

    actual fun nextFile(): FileInfo? {
        val tmp = g_file_enumerator_next_file(enumerator = gFileEnumeratorPtr, cancellable = null, error = null)
        return if (tmp != null) FileInfo(tmp) else null
    }
}
