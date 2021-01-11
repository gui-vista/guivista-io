package org.guiVista.io.application.menu

import org.guiVista.core.ObjectBase

public expect class MenuLinkIterator : ObjectBase {
    /**
     * Gets the name of the link at the current iterator position. The iterator is not advanced.
     */
    public val name: String

    /**
     * Gets the linked [MenuModelBase] at the current iterator position. The iterator is not advanced.
     */
    public val value: MenuModelBase

    /**
     * Attempts to advance the iterator to the next (possibly first) link. A value of *true* is returned on success, or
     * *false* if there are no more links. You **MUST** call this function when you first acquire the iterator to
     * advance it to the first link (and determine if the first link exists at all).
     * @return A value of *true* on success, or *false* when there are no more links.
     */
    public fun next(): Boolean
}
