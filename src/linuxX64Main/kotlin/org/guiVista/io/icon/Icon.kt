package org.guiVista.io.icon

import gio2.*
import glib2.TRUE
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString

public actual class Icon private constructor(iconPtr: CPointer<GIcon>?) : IconBase {
    public override val gIconPtr: CPointer<GIcon>? = iconPtr

    public actual companion object {
        /** Creates a new [Icon] instance from a iconPtr. */
        public fun fromIconPtr(iconPtr: CPointer<GIcon>?): Icon = Icon(iconPtr)

        public actual fun fromString(str: String): Icon = Icon(g_icon_new_for_string(str, null))
    }

    actual override fun equals(other: Any?): Boolean =
        if (other is Icon) g_icon_equal(gIconPtr, other.gIconPtr) == TRUE
        else false

    actual override fun hashCode(): Int = g_icon_hash(gIconPtr).toInt()

    actual override fun toString(): String = g_icon_to_string(gIconPtr)?.toKString() ?: ""
}
