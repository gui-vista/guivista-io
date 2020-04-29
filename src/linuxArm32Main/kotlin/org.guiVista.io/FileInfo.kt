package org.guiVista.io

import gio2.*
import glib2.FALSE
import glib2.TRUE
import glib2.g_object_unref
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString
import org.guiVista.core.Closable

actual class FileInfo(fileInfoPtr: CPointer<GFileInfo>? = null) : Closable {
    val gFileInfoPtr: CPointer<GFileInfo>? = fileInfoPtr ?: g_file_info_new()
    actual var isHidden: Boolean
        get() = g_file_info_get_is_hidden(gFileInfoPtr) == TRUE
        set(value) = g_file_info_set_is_hidden(gFileInfoPtr, if (value) TRUE else FALSE)

    actual var isSymlink: Boolean
        get() = g_file_info_get_is_symlink(gFileInfoPtr) == TRUE
        set(value) = g_file_info_set_is_symlink(gFileInfoPtr, if (value) TRUE else FALSE)
    actual var name: String
        get() = g_file_info_get_name(gFileInfoPtr)?.toKString() ?: ""
        set(value) = g_file_info_set_name(gFileInfoPtr, value)
    actual var displayName: String
        get() = g_file_info_get_display_name(gFileInfoPtr)?.toKString() ?: ""
        set(value) = g_file_info_set_display_name(gFileInfoPtr, value)
    actual var editName: String
        get() = g_file_info_get_edit_name(gFileInfoPtr)?.toKString() ?: ""
        set(value) = g_file_info_set_edit_name(gFileInfoPtr, value)
    actual var contentType: String
        get() = g_file_info_get_content_type(gFileInfoPtr)?.toKString() ?: ""
        set(value) = g_file_info_set_content_type(gFileInfoPtr, value)

    /** The file size (in bytes). */
    var size: goffset
        set(value) = g_file_info_set_size(gFileInfoPtr, value)
        get() = g_file_info_get_size(gFileInfoPtr)

    override fun close() {
        g_object_unref(gFileInfoPtr)
    }
}