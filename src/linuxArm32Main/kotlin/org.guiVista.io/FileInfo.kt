package org.guiVista.io

import gio2.*
import glib2.FALSE
import glib2.TRUE
import glib2.g_free
import glib2.g_object_unref
import kotlinx.cinterop.*
import org.guiVista.core.Closable
import org.guiVista.io.icon.Icon

public actual class FileInfo(fileInfoPtr: CPointer<GFileInfo>? = null) : Closable {
    public val gFileInfoPtr: CPointer<GFileInfo>? = fileInfoPtr ?: g_file_info_new()
    public actual var isHidden: Boolean
        get() = g_file_info_get_is_hidden(gFileInfoPtr) == TRUE
        set(value) = g_file_info_set_is_hidden(gFileInfoPtr, if (value) TRUE else FALSE)
    public actual var isSymlink: Boolean
        get() = g_file_info_get_is_symlink(gFileInfoPtr) == TRUE
        set(value) = g_file_info_set_is_symlink(gFileInfoPtr, if (value) TRUE else FALSE)
    public actual var name: String
        get() = g_file_info_get_name(gFileInfoPtr)?.toKString() ?: ""
        set(value) = g_file_info_set_name(gFileInfoPtr, value)
    public actual var displayName: String
        get() = g_file_info_get_display_name(gFileInfoPtr)?.toKString() ?: ""
        set(value) = g_file_info_set_display_name(gFileInfoPtr, value)
    public actual var editName: String
        get() = g_file_info_get_edit_name(gFileInfoPtr)?.toKString() ?: ""
        set(value) = g_file_info_set_edit_name(gFileInfoPtr, value)
    public actual var contentType: String
        get() = g_file_info_get_content_type(gFileInfoPtr)?.toKString() ?: ""
        set(value) = g_file_info_set_content_type(gFileInfoPtr, value)
    public actual var size: Long
        set(value) = g_file_info_set_size(gFileInfoPtr, value)
        get() = g_file_info_get_size(gFileInfoPtr)
    public actual val isBackup: Boolean
        get() = g_file_info_get_is_backup(gFileInfoPtr) == TRUE
    public actual var icon: Icon
        get() = Icon.fromIconPtr(g_file_info_get_icon(gFileInfoPtr))
        set(value) = g_file_info_set_icon(gFileInfoPtr, value.gIconPtr)
    public actual var symbolicIcon: Icon
        get() = Icon.fromIconPtr(g_file_info_get_icon(gFileInfoPtr))
        set(value) = g_file_info_set_icon(gFileInfoPtr, value.gIconPtr)
    public actual var symlinkTarget: String
        get() = g_file_info_get_symlink_target(gFileInfoPtr)?.toKString() ?: ""
        set(value) = g_file_info_set_symlink_target(gFileInfoPtr, value)
    public actual val eTag: String
        get() = g_file_info_get_etag(gFileInfoPtr)?.toKString() ?: ""
    public actual var sortOrder: Int
        get() = g_file_info_get_sort_order(gFileInfoPtr)
        set(value) = g_file_info_set_sort_order(gFileInfoPtr, value)

    override fun close() {
        g_object_unref(gFileInfoPtr)
    }

    public actual fun hasAttribute(attr: String): Boolean = g_file_info_has_attribute(gFileInfoPtr, attr) == TRUE

    public actual fun hasNamespace(namespace: String): Boolean = g_file_info_has_namespace(gFileInfoPtr, namespace) == TRUE

    public actual fun listAttributes(namespace: String): Array<String> {
        val tmpList = mutableListOf<String>()
        var pos = 0
        var item: CPointer<ByteVarOf<Byte>>?
        val attrTmp = g_file_info_list_attributes(gFileInfoPtr, namespace)
        do {
            item = attrTmp?.get(pos)
            if (item != null) tmpList += item.toKString()
            pos++
        } while (item != null)
        return tmpList.toTypedArray()
    }

    public actual fun removeAttribute(attr: String) {
        g_file_info_remove_attribute(gFileInfoPtr, attr)
    }

    public actual fun fetchAttributeAsString(attr: String): String {
        val tmp = g_file_info_get_attribute_as_string(gFileInfoPtr, attr)
        val result = tmp?.toKString() ?: ""
        g_free(tmp)
        return result
    }

    public actual fun fetchStringAttribute(attr: String): String =
        g_file_info_get_attribute_string(gFileInfoPtr, attr)?.toKString() ?: ""

    public actual fun fetchStringVAttribute(attr: String): Array<String> {
        val tmpList = mutableListOf<String>()
        var pos = 0
        var item: CPointer<ByteVarOf<Byte>>?
        val tmpValue = g_file_info_get_attribute_stringv(gFileInfoPtr, attr)
        do {
            item = tmpValue?.get(pos)
            if (item != null) tmpList += item.toKString()
            pos++
        } while (item != null)
        return tmpList.toTypedArray()
    }

    public actual fun fetchByteStringAttribute(attr: String): String =
        g_file_info_get_attribute_byte_string(gFileInfoPtr, attr)?.toKString() ?: ""

    public actual fun fetchBooleanAttribute(attr: String): Boolean =
        g_file_info_get_attribute_boolean(gFileInfoPtr, attr) == TRUE

    public actual fun fetchUInt32Attribute(attr: String): UInt = g_file_info_get_attribute_uint32(gFileInfoPtr, attr)

    public actual fun fetchInt32Attribute(attr: String): Int = g_file_info_get_attribute_int32(gFileInfoPtr, attr)

    public actual fun fetchUInt64Attribute(attr: String): ULong = g_file_info_get_attribute_uint64(gFileInfoPtr, attr)

    public actual fun fetchInt64Attribute(attr: String): Long = g_file_info_get_attribute_int64(gFileInfoPtr, attr)

    public actual fun changeStringAttribute(attr: String, value: String) {
        g_file_info_set_attribute_string(info = gFileInfoPtr, attribute = attr, attr_value = value)
    }

    public actual fun changeStringVAttribute(attr: String, value: Array<String>): Unit = memScoped {
        val tmp = value.toCStringArray(this)
        g_file_info_set_attribute_stringv(info = gFileInfoPtr, attribute = attr, attr_value = tmp)
    }

    public actual fun changeByteStringAttribute(attr: String, value: String) {
        g_file_info_set_attribute_byte_string(info = gFileInfoPtr, attribute = attr, attr_value = value)
    }

    public actual fun changeBooleanAttribute(attr: String, value: Boolean) {
        g_file_info_set_attribute_boolean(
            info = gFileInfoPtr,
            attribute = attr,
            attr_value = if (value) TRUE else FALSE
        )
    }

    public actual fun changeUInt32Attribute(attr: String, value: UInt) {
        g_file_info_set_attribute_uint32(info = gFileInfoPtr, attribute = attr, attr_value = value)
    }

    public actual fun changeInt32Attribute(attr: String, value: Int) {
        g_file_info_set_attribute_int32(info = gFileInfoPtr, attribute = attr, attr_value = value)
    }

    public actual fun changeUInt64Attribute(attr: String, value: ULong) {
        g_file_info_set_attribute_uint64(info = gFileInfoPtr, attribute = attr, attr_value = value)
    }

    public actual fun changeInt64Attribute(attr: String, value: Long) {
        g_file_info_set_attribute_int64(info = gFileInfoPtr, attribute = attr, attr_value = value)
    }

    public actual fun clearStatus() {
        g_file_info_clear_status(gFileInfoPtr)
    }
}
