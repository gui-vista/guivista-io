package org.guiVista.io

/** Allows a resource to be closed. */
interface Closable {
    /** Closes the resource. May also close other child resources. */
    fun close()
}
