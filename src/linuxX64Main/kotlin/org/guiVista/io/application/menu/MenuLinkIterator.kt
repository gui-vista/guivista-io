package org.guiVista.io.application.menu

import gio2.GMenuLinkIter
import gio2.g_menu_link_iter_get_name
import gio2.g_menu_link_iter_get_value
import gio2.g_menu_link_iter_next
import glib2.TRUE
import glib2.g_object_unref
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString
import org.guiVista.core.ObjectBase

public actual class MenuLinkIterator(ptr: CPointer<GMenuLinkIter>?) : ObjectBase {
    public val gMenuLinkIterPtr: CPointer<GMenuLinkIter>? = ptr

    public actual val name: String
        get() = g_menu_link_iter_get_name(gMenuLinkIterPtr)?.toKString() ?: ""

    public actual val value: MenuModelBase
        get() = MenuModel(g_menu_link_iter_get_value(gMenuLinkIterPtr))

    override fun close() {
        g_object_unref(gMenuLinkIterPtr)
    }

    public actual fun next(): Boolean = g_menu_link_iter_next(gMenuLinkIterPtr) == TRUE
}
