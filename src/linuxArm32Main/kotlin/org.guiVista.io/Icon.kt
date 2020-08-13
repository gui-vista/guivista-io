package org.guiVista.io

import gio2.GIcon
import gio2.g_icon_hash
import gio2.g_icon_new_for_string
import gio2.g_icon_to_string
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString

actual class Icon private constructor() : IconBase {
    private var _gIconPtr: CPointer<GIcon>? = null
    val gIconPtr: CPointer<GIcon>?
        get() = _gIconPtr

    actual companion object {
        actual fun newForString(str: String): Icon = Icon().apply { g_icon_new_for_string(str, null) }
    }

    override val hash: UInt
        get() = g_icon_hash(_gIconPtr)

    actual override fun toString(): String = g_icon_to_string(_gIconPtr)?.toKString() ?: ""
}
