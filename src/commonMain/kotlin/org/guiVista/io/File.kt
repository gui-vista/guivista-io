package org.guiVista.io

/** File and Directory Handling. */
expect class File {
    /**
     * Gets the base name (the last component of the path) for a given [File]. If called for the top level of a system
     * (such as the filesystem root, or a uri like sftp://host/ it will return a single directory separator (and on
     * Windows possibly a drive letter). The base name is a byte string (**not** UTF-8). It has no defined encoding, or
     * rules other than it may not contain zero bytes. If you want to use file names in a user interface you **should**
     * use the display name that you can get by requesting the `G_FILE_ATTRIBUTE_STANDARD_DISPLAY_NAME` attribute, with
     * `queryInfo`.
     *
     * No blocking I/O is done with the property. This property will return the [file's][File] base name, or *""* (an
     * empty String) if the [File] instance is invalid.
     */
    val baseName: String

    /**
     * Gets the local pathname for [File] instance if one exists. If it isn't empty then this is guaranteed to be an
     * absolute, canonical path. It might contain symlinks. No blocking I/O is done with the property.
     *
     * This property will return the [file's][File] path, or *""* (an empty String) if no such path exists.
     */
    val path: String

    /** Gets the URI for the file. No blocking I/O is done with the property. */
    val uri: String

    /**
     * Gets the parent directory for the file. If the file represents the root directory of the file system, then
     * *null* will be returned. This function doesn't do any blocking I/O.
     *
     * Returns a [File] instance to the parent of the given [File], or *null* if there is no parent.
     */
    val parent: File?

    /**
     * Checks if file has a parent, and optionally if it is parent. If parent is *null* then this function returns
     * *true*, if file has any parent at all. If parent is isn't null then *true* is only returned, if file is an
     * immediate child of parent.
     */
    fun hasParent(parent: File): Boolean

    /**
     * Gets a child of file with [base name][baseName] equal to [name]. Note that the file with that specific name might not
     * exist, but you can still have a [File] that points to it. You can use this for instance to create that file.
     * This function doesn't do any blocking I/O.
     * @param name The child's base name.
     * @return A [File] to a child specified by [name].
     */
    fun fetchChild(name: String): File

    /**
     * Checks whether file has the prefix specified by prefix. In other words if the names of initial elements of
     * file's pathname match prefix. **Only** full pathname elements are matched so a path like /foo isn't considered a
     * prefix of /foobar, only of /foo/bar. A [File] isn't a prefix of itself. If you want to check for equality use
     * [equals].
     *
     * This function doesn't do any I/O, as it works purely on names. As such it can sometimes return *false* even if
     * file is inside a prefix (from a filesystem point of view). Because the prefix of file is an alias of prefix.
     * @param prefix Input [file][File].
     * @return If the files's parent, grandparent, etc is prefix then *true*, otherwise *false*.
     */
    fun hasPrefix(prefix: File): Boolean

    /**
     * Gets the parse name of the file. A parse name is a UTF-8 string that describes the file. This is generally used
     * to show the [File] as a nice full path name kind of String in a user interface, like in a location entry. For
     * local files with names that can safely be converted to UTF-8 the pathname is used, otherwise the IRI is used (a
     * form of URI that allows UTF-8 characters unescaped).
     *
     * This function doesn't do any blocking I/O.
     * @return A String containing the [file's][File] parse name.
     */
    fun fetchParseName(): String

    /**
     * Gets the path for [descendant] relative to parent (this [File] instance). This function doesn't do any blocking
     * I/O.
     * @param descendant Input [file][File].
     * @return The relative path from [descendant] to parent, or *""* (an empty String) if [descendant] doesn't have a
     * parent as prefix.
     */
    fun fetchRelativePath(descendant: File): String

    /**
     * Resolves a relative path for [File] instance to an absolute path. This function doesn't do any blocking I/O.
     * @param relativePath Input [file][File].
     * @return A [File] instance to the resolved path, or *null* if [relativePath] is *null*, or if file is invalid.
     */
    fun resolveRelativePath(relativePath: String): File?

    /**
     * Creates a hash value for the [File] instance. This function doesn't do any blocking I/O.
     * @return If [File] instance isn't valid then return *0*, otherwise an Int that can be used as hash value for the
     * [File] instance. This function is intended for easily hashing a [File] to add to a GHashTable, or similar data
     * structure.
     */
    fun hash(): UInt

    companion object {
        /**
         * Constructs a [File] for a given path. This operation never fails, but the returned object might not support
         * any I/O operation **if** path is malformed.
         * @param path A string containing a relative, or absolute path. The string **must** be encoded in the glib
         * filename encoding.
         * @return A new [File] for the given path.
         */
        fun fromPath(path: String): File

        /**
         * Constructs a [File] for a given URI. This operation never fails, but the returned object might not support
         * any I/O operation **if** uri is malformed, or **if** the uri type isn't supported.
         * @param uri A UTF-8 encoded String containing a URI.
         * @return A new [File] for the given uri.
         */
        fun fromUri(uri: String): File

        /**
         * Constructs a [File] with the given [parseName] (i.e. something given by `g_file_get_parse_name()`). This
         * operation never fails, but the returned object might not support any I/O operation **if** [parseName] cannot
         * be parsed.
         * @param parseName A file name, or path to be parsed.
         * @return A new [File] instance.
         */
        fun parseName(parseName: String): File
    }
}