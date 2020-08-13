package org.guiVista.io

import gio2.*
import glib2.TRUE
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString

actual class Icon private constructor(iconPtr: CPointer<GIcon>?) : IconBase {
    val gIconPtr: CPointer<GIcon>? = iconPtr

    actual companion object {
        /** Creates a new [Icon] instance from a iconPtr. */
        fun fromIconPtr(iconPtr: CPointer<GIcon>?): Icon = Icon(iconPtr)

        actual fun fromString(str: String): Icon = Icon(g_icon_new_for_string(str, null))
    }

    actual override fun toString(): String = g_icon_to_string(gIconPtr)?.toKString() ?: ""

    actual override fun equals(other: Any?): Boolean =
        if (other is Icon) g_icon_equal(gIconPtr, other.gIconPtr) == TRUE
        else false

    actual override fun hashCode(): Int = g_icon_hash(gIconPtr).toInt()
}
