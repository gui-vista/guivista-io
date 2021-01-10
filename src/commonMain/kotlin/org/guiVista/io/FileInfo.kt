package org.guiVista.io

import org.guiVista.core.Closable
import org.guiVista.io.icon.Icon

/** File information and attributes. */
public expect class FileInfo : Closable {
    /** When *true* the file is hidden. */
    public var isHidden: Boolean

    /** When *true* the file is symbolic link */
    public var isSymlink: Boolean

    /** Name of the file. */
    public var name: String

    /** Display name for the file. */
    public var displayName: String

    /** The file's content type */
    public var contentType: String

    /** Edit name for the file. */
    public var editName: String

    /** When *true* the file is a backup file. */
    public val isBackup: Boolean

    /** The icon for the file. */
    public var icon: Icon

    /** The symbolic icon for the file */
    public var symbolicIcon: Icon

    /** File size in bytes. */
    public var size: Long

    /** The symbolic link target. */
    public var symlinkTarget: String

    /** The entity tag. Refer to `G_FILE_ATTRIBUTE_ETAG_VALUE`. Based on the `g_file_info_get_etag` function. */
    public val eTag: String

    /** The value of the sort order attribute. Refer to `G_FILE_ATTRIBUTE_STANDARD_SORT_ORDER`. */
    public var sortOrder: Int

    /**
     * Checks if [FileInfo] has an attribute that matches [attr].
     * @param attr File attribute key.
     * @return A value of *true* if [FileInfo] has an attribute matching [attr], *false* otherwise.
     */
    public fun hasAttribute(attr: String): Boolean

    /**
     * Checks if [FileInfo] has an attribute that matches [namespace].
     * @param namespace File attribute namespace.
     * @return A value of *true* if [FileInfo] has an attribute matching [namespace], *false* otherwise.
     */
    public fun hasNamespace(namespace: String): Boolean

    /**
     * Lists the [FileInfo] attributes.
     * @param namespace The file attribute key's namespace, or *""* to list **ALL** attributes.
     * @return An array of strings of all of the possible attribute types for the [namespace], or an empty array on
     * error.
     */
    public fun listAttributes(namespace: String): Array<String>

    /**
     * Removes all cases of [attr] from [FileInfo] if it exists.
     * @param attr A file attribute key.
     */
    public fun removeAttribute(attr: String)

    /**
     * Gets the value of [attr] formatted as a String. This escapes things as needed to make the string valid UTF-8.
     * Based on the `g_file_info_get_attribute_as_string` function.
     * @param attr A file attribute key.
     * @return A UTF-8 string associated with the given [attribute][attr], or *""* if the [attribute][attr] wasn’t set.
     */
    public fun fetchAttributeAsString(attr: String): String

    /**
     * Gets the value of a String attribute. Based on the `g_file_info_get_attribute_string` function.
     * @param attr A file attribute key.
     * @return The contents of the attribute value as a UTF-8 String, or *""* otherwise.
     */
    public fun fetchStringAttribute(attr: String): String

    /**
     * Gets the value of a StringV attribute. Based on the `g_file_info_get_attribute_stringv` function.
     * @param attr A file attribute key.
     * @return the contents of the attribute value as a StringV (an array), or a empty array. All returned strings are
     * UTF-8.
     */
    public fun fetchStringVAttribute(attr: String): Array<String>

    /**
     * Gets the value of a Byte String attribute. Based on the `g_file_info_get_attribute_byte_string` function.
     * @param attr A file attribute key.
     * @return The contents of the attribute value as a Byte String, or *""* otherwise.
     */
    public fun fetchByteStringAttribute(attr: String): String

    /**
     * Gets the value of a Boolean attribute. Based on the `g_file_info_get_attribute_boolean` function.
     * @param attr A file attribute key.
     * @return The Boolean value contained within the attribute. If [attr] doesn't have a value then *false* is
     * returned.
     */
    public fun fetchBooleanAttribute(attr: String): Boolean

    /**
     * Gets an unsigned 32-bit Integer contained within the [attribute][attr]. Based on the
     * `g_file_info_get_attribute_uint32` function.
     * @param attr A file attribute key.
     * @return An unsigned 32-bit Integer from the attribute. If the attribute does not contain an unsigned 32-bit
     * Integer, or is invalid then *0* will be returned.
     */
    public fun fetchUInt32Attribute(attr: String): UInt

    /**
     * Gets a signed 32-bit Integer contained within the attribute. Based on the `g_file_info_get_attribute_int32`
     * function.
     * @param attr A file attribute key.
     * @return A signed 32-bit Integer from the attribute. If the attribute doesn't contain a signed 32-bit Integer, or
     * is invalid then *0* will be returned.
     */
    public fun fetchInt32Attribute(attr: String): Int

    /**
     * Gets an unsigned 64-bit Integer contained within the [attribute][attr]. Based on the
     * `g_file_info_get_attribute_uint64` function.
     * @param attr A file attribute key.
     * @return An unsigned 64-bit Integer from the attribute. If the attribute does not contain an unsigned 64-bit
     * Integer, or is invalid then *0* will be returned.
     */
    public fun fetchUInt64Attribute(attr: String): ULong

    /**
     * Gets a signed 64-bit Integer contained within the attribute. Based on the `g_file_info_get_attribute_int64`
     * function.
     * @param attr A file attribute key.
     * @return A signed 64-bit Integer from the attribute. If the attribute doesn't contain a signed 64-bit Integer, or
     * is invalid then *0* will be returned.
     */
    public fun fetchInt64Attribute(attr: String): Long

    /**
     * Sets the String [attribute][attr] to contain the given [value], if possible. Based on the
     * `g_file_info_set_attribute_string` function.
     * @param attr A file attribute key.
     * @param value A UTF-8 String.
     */
    public fun changeStringAttribute(attr: String, value: String)

    /**
     * Sets the String V [attribute][attr] to contain the given [value], if possible. Based on the
     * `g_file_info_set_attribute_stringv` function.
     * @param attr A file attribute key.
     * @param value An array of UTF-8 strings.
     */
    public fun changeStringVAttribute(attr: String, value: Array<String>)

    /**
     * Sets the Byte String [attribute][attr] to contain the given [value], if possible. Based on the
     * `g_file_info_set_attribute_byte_string` function.
     * @param attr A file attribute key.
     * @param value A Byte String.
     */
    public fun changeByteStringAttribute(attr: String, value: String)

    /**
     * Sets the Boolean [attribute][attr] to contain the given [value], if possible. Based on the
     * `g_file_info_set_attribute_boolean` function.
     * @param attr A file attribute key.
     * @param value A Boolean value.
     */
    public fun changeBooleanAttribute(attr: String, value: Boolean)

    /**
     * Sets the UInt32 [attribute][attr] to contain the given [value], if possible. Based on the
     * `g_file_info_set_attribute_uint32` function.
     * @param attr A file attribute key.
     * @param value An unsigned 32-bit integer.
     */
    public fun changeUInt32Attribute(attr: String, value: UInt)

    /**
     * Sets the Int32 [attribute][attr] to contain the given [value], if possible. Based on the
     * `g_file_info_set_attribute_int32` function.
     * @param attr A file attribute key.
     * @param value A signed 32-bit integer.
     */
    public fun changeInt32Attribute(attr: String, value: Int)

    /**
     * Sets the UInt64 [attribute][attr] to contain the given [value], if possible. Based on the
     * `g_file_info_set_attribute_uint64` function.
     * @param attr A file attribute key.
     * @param value An unsigned 64-bit integer.
     */
    public fun changeUInt64Attribute(attr: String, value: ULong)

    /**
     * Sets the UInt64 [attribute][attr] to contain the given [value], if possible. Based on the
     * `g_file_info_set_attribute_int64` function.
     * @param attr A file attribute key.
     * @param value An unsigned 64-bit integer.
     */
    public fun changeInt64Attribute(attr: String, value: Long)

    /** Clears the status information from [FileInfo]. */
    public fun clearStatus()
}