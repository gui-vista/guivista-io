package org.guiVista.io

/** Represents an icon. */
expect class Icon : IconBase {
    companion object {
        /**
         * Generate a [Icon] instance from [str]. This function can fail if [str] isn't valid. If your application, or
         * library provides one or more [Icon] implementations then you need to ensure that each `GType` is registered
         * with the type system prior to calling [fromString].
         * @param str A String obtained from [toString].
         * @return A instance of [Icon].
         * @see toString
         */
        fun fromString(str: String): Icon
    }

    /**
     * Generates a textual representation of the icon that can be used for serialization such as when passing icon to a
     * different process, or saving it to persistent storage. Use `g_icon_new_for_string()` to get icon back from the
     * returned string. The encoding of the returned string is proprietary to GIcon except in the following two cases:
     * - If icon is a GFileIcon the returned string is a native path (such as * /path/to/my icon.png*) without escaping
     * if the `GFile` for icon is a native file. If the file is not native then the returned string is the result of
     * `g_file_get_uri()` (such as *sftp://path/to/my%20icon.png*).
     * @return A textual representation of the icon.
     */
    override fun toString(): String

    /** Checks if two icons are equal. */
    override fun equals(other: Any?): Boolean

    /** The icon's hash. Suitable for use in a `GHashTable`, or similar data structure. */
    override fun hashCode(): Int
}
