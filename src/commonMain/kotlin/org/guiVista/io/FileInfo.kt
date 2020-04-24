package org.guiVista.io

import org.guiVista.core.Closable

/** File information and attributes. */
expect class FileInfo : Closable {
    /** When *true* the file is hidden. */
    var isHidden: Boolean

    /** When *true* the file is symbolic link */
    var isSymlink: Boolean

    /** Name of the file. */
    var name: String

    /** Display name for the file. */
    var displayName: String

    /** The file's content type */
    var contentType: String

    /** Edit name for the file. */
    var editName: String
}