package org.guiVista.io

import gio2.*
import glib2.FALSE
import glib2.GError
import glib2.TRUE
import glib2.g_object_unref
import kotlinx.cinterop.*
import org.guiVista.core.Closable
import org.guiVista.core.Error

public actual class FileEnumerator(fileEnumeratorPtr: CPointer<GFileEnumerator>) : Closable {
    private val arena = Arena()
    public val gFileEnumeratorPtr: CPointer<GFileEnumerator> = fileEnumeratorPtr
    private val closeErrorPtrVar = arena.alloc<CPointerVar<GError>>()
    private val iterateErrorPtrVar = arena.alloc<CPointerVar<GError>>()
    private val nextFileErrorPtrVar = arena.alloc<CPointerVar<GError>>()
    public actual val nextFileError: Error?
        get() {
            val tmp = nextFileErrorPtrVar.value
            return if (tmp != null) Error.fromErrorPointer(tmp) else null
        }
    public actual val closeError: Error?
        get() {
            val tmp = closeErrorPtrVar.value
            return if (tmp != null) Error.fromErrorPointer(tmp) else null
        }
    public actual val iterateError: Error?
        get() {
            val tmp = iterateErrorPtrVar.value
            return if (tmp != null) Error.fromErrorPointer(tmp) else null
        }
    public actual val isClosed: Boolean
        get() = g_file_enumerator_is_closed(gFileEnumeratorPtr) == TRUE
    public actual val container: File
        get() = File.fromFilePtr(g_file_enumerator_get_container(gFileEnumeratorPtr))
    public actual var pendingOperations: Boolean
        get() = g_file_enumerator_has_pending(gFileEnumeratorPtr) == TRUE
        set(value) = g_file_enumerator_set_pending(gFileEnumeratorPtr, if (value) TRUE else FALSE)

    public actual fun fetchChild(info: FileInfo): File =
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

    public actual fun nextFile(): FileInfo? {
        val tmp = g_file_enumerator_next_file(enumerator = gFileEnumeratorPtr, cancellable = null,
            error = nextFileErrorPtrVar.ptr)
        return if (tmp != null) FileInfo(tmp) else null
    }

    public actual fun iterate(outInfo: Array<FileInfo>, outChild: File?): Boolean {
        if (outInfo.isEmpty() && outChild == null) {
            throw IllegalArgumentException("Must have a non null outChild, or outInfo that isn't empty.")
        }
        val rc = g_file_enumerator_iterate(
            direnum = gFileEnumeratorPtr,
            cancellable = null,
            error = iterateErrorPtrVar.ptr,
            out_child = cValuesOf(outChild?.gFilePtr),
            out_info = outInfo.map { it.gFileInfoPtr }.toCValues()
        )
        return rc == TRUE
    }

}
