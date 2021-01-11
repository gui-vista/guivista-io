package org.guiVista.io.application.menu

import org.guiVista.core.ObjectBase
import org.guiVista.core.dataType.Variant

public expect class MenuAttributeIterator : ObjectBase {
    /**
     * Gets the name of the attribute at the current iterator position. The iterator is not advanced.
     */
    public val name: String

    /**
     * Gets the value of the attribute at the current iterator position. The iterator is not advanced.
     */
    public val value: Variant

    /**
     * Attempts to advance the iterator to the next (possibly first) attribute. A value of *true* is returned on
     * success, or *false* if there are no more attributes. You **MUST** call this function when you first acquire the
     * iterator to advance it to the first attribute (and determine if the first attribute exists at all).
     * @return A value of *true* on success, or *false* when there are no more attributes.
     */
    public fun next(): Boolean
}
