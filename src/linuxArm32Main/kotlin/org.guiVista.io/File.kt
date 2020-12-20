package org.guiVista.io

import gio2.*
import glib2.TRUE
import glib2.g_object_unref
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.cValuesOf
import kotlinx.cinterop.toKString
import org.guiVista.core.Closable
import org.guiVista.core.Error

@Suppress("EqualsOrHashCode")
public actual class File private constructor(filePtr: CPointer<GFile>?) : Closable {
    public val gFilePtr: CPointer<GFile>? = filePtr
    public actual val baseName: String
        get() = g_file_get_basename(gFilePtr)?.toKString() ?: ""
    public actual val path: String
        get() = g_file_get_path(gFilePtr)?.toKString() ?: ""
    public actual val uri: String
        get() = g_file_get_uri(gFilePtr)?.toKString() ?: ""
    public actual val parent: File?
        get() {
            val tmp = g_file_get_parent(gFilePtr)
            return if (tmp != null) File(tmp) else null
        }
    public actual val peekPath: String
        get() = g_file_peek_path(gFilePtr)?.toKString() ?: ""
    public actual val isNative: Boolean
        get() = g_file_is_native(gFilePtr) == TRUE
    public actual val uriScheme: String
        get() = g_file_get_uri_scheme(gFilePtr)?.toKString() ?: ""

    public actual fun hasParent(parent: File): Boolean = g_file_has_parent(gFilePtr, parent.gFilePtr) == TRUE

    public actual fun fetchChild(name: String): File = File(g_file_get_child(gFilePtr, name))

    public actual fun hasPrefix(prefix: File): Boolean = g_file_has_prefix(gFilePtr, prefix.gFilePtr) == TRUE

    public actual fun fetchParseName(): String = g_file_get_parse_name(gFilePtr)?.toKString() ?: ""

    public actual fun fetchRelativePath(descendant: File): String =
        g_file_get_relative_path(gFilePtr, descendant.gFilePtr)?.toKString() ?: ""

    public actual fun resolveRelativePath(relativePath: String): File? {
        val tmp = g_file_resolve_relative_path(gFilePtr, relativePath)
        return if (tmp != null) File(tmp) else null
    }

    public actual fun hasUriScheme(uriScheme: String): Boolean = g_file_has_uri_scheme(gFilePtr, uriScheme) == TRUE

    public actual companion object {
        /** Creates a new [File] instance from a filePtr. */
        public fun fromFilePtr(filePtr: CPointer<GFile>?): File = File(filePtr)

        public actual fun fromPath(path: String): File = File(g_file_new_for_path(path))

        public actual fun fromUri(uri: String): File = File(g_file_new_for_uri(uri))

        public actual fun parseName(parseName: String): File = File(g_file_parse_name(parseName))

        public actual fun fromCommandLine(arg: String, cwd: String): File =
            if (cwd.isEmpty()) File(g_file_new_for_commandline_arg(arg))
            else File(g_file_new_for_commandline_arg_and_cwd(arg, cwd))
    }

    /**
     * Utility function to inspect the GFileType of a [File]. This function does blocking I/O. The primary use case of
     * this function is to check if a [File] is a regular file, directory, or symbolic link.
     * @param flags A set of GFileQueryInfoFlags.
     * @return The GFileType of the [File], and *G_FILE_TYPE_UNKNOWN* if the file doesn't exist.
     */
    public fun queryFileType(flags: GFileQueryInfoFlags): GFileType =
        g_file_query_file_type(file = gFilePtr, cancellable = null, flags = flags)

    /**
     * Gets the requested information about specified file. The result is a [FileInfo] object that contains key-value
     * attributes (such as the type or size of the file). The [attributes] value is a String that specifies the file
     * attributes that should be gathered. It is not an error if it's not possible to read a particular requested
     * attribute from a file - it just won't be set. Attributes should be a comma-separated list of attributes, or
     * attribute wildcards. The wildcard "*" means **all** attributes, and a wildcard like "standard::*" means all
     * attributes in the standard namespace. An example attribute query would be "standard::*,owner::user". The
     * standard attributes are available as defines like `G_FILE_ATTRIBUTE_STANDARD_NAME`.
     *
     * If cancellable isn't *null* then the operation can be cancelled by triggering the cancellable object from
     * another thread. If the operation was cancelled the error `G_IO_ERROR_CANCELLED` will be returned. For symlinks
     * normally the information about the target of the symlink is returned, rather than information about the symlink
     * itself. However if you pass `G_FILE_QUERY_INFO_NOFOLLOW_SYMLINKS` in [flags] the information about the symlink
     * itself will be returned. Also for symlinks that point to non-existing files the information about the symlink
     * itself will be returned.
     *
     * If the file doesn't exist the `G_IO_ERROR_NOT_FOUND` error will be returned. Other errors are possible too, and
     * depend on what kind of filesystem the file is on.
     * @param attributes An attribute query String.
     * @param flags A set of GFileQueryInfoFlags.
     * @param error The [Error] instance to use for storing error information.
     * @return A [FileInfo] instance, or *null* if a error has occurred.
     */
    public fun queryInfo(attributes: String, flags: GFileQueryInfoFlags, error: Error): FileInfo? {
        val fileInfoPtr = g_file_query_info(
            file = gFilePtr,
            attributes = attributes,
            flags = flags,
            error = cValuesOf(error.gErrorPtr),
            cancellable = null
        )
        return if (fileInfoPtr != null) FileInfo(fileInfoPtr) else null
    }

    actual override fun hashCode(): Int = g_file_hash(gFilePtr).toInt()

    actual override fun equals(other: Any?): Boolean =
        if (other is File) g_file_equal(gFilePtr, other.gFilePtr) == TRUE else false

    override fun close() {
        g_object_unref(gFilePtr)
    }

    /**
     * Gets the requested information about the files in a directory. The result is a [FileEnumerator] object that will
     * give out GFileInfo objects for all the files in the directory. The [attributes] value is a string that specifies
     * the file attributes that should be gathered. It is not an error if it's not possible to read a particular
     * requested attribute from a file - it just won't be set. Attributes should be a comma-separated list of
     * attributes, or attribute wildcards. The wildcard "*" means all attributes, and a wildcard like "standard::*"
     * means all attributes in the standard namespace. An example attribute query be "standard::*,owner::user". The
     * standard attributes are available as defines, like *G_FILE_ATTRIBUTE_STANDARD_NAME*.
     *
     * If cancellable isn't *null* then the operation can be cancelled by triggering the cancellable object from
     * another thread. If the operation was cancelled the error *G_IO_ERROR_CANCELLED* will be returned. If the file
     * doesn't exist the *G_IO_ERROR_NOT_FOUND* error will be returned. If the file isn't a directory the
     * *G_IO_ERROR_NOT_DIRECTORY* error will be returned. Other errors are possible too.
     * @param attributes An attribute query string.
     * @param flags A set of GFileQueryInfoFlags.
     * @return A [FileEnumerator] if successful, *null* on error.
     */
    public fun enumerateChildren(attributes: String, flags: GAppInfoCreateFlags): FileEnumerator? {
        val tmp = g_file_enumerate_children(
            file = gFilePtr,
            attributes = attributes,
            flags = flags,
            cancellable = null,
            error = null
        )
        return if (tmp != null) FileEnumerator(tmp) else null
    }
}
