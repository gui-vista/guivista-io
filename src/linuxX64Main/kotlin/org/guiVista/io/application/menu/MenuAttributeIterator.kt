package org.guiVista.io.application.menu

import gio2.GMenuAttributeIter
import gio2.g_menu_attribute_iter_get_name
import gio2.g_menu_attribute_iter_get_value
import gio2.g_menu_attribute_iter_next
import glib2.TRUE
import glib2.g_object_unref
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString
import org.guiVista.core.ObjectBase
import org.guiVista.core.dataType.Variant

public actual class MenuAttributeIterator(ptr: CPointer<GMenuAttributeIter>?) : ObjectBase {
    public val gMenuAttributeIterPtr: CPointer<GMenuAttributeIter>? = ptr

    public actual val name: String
        get() = g_menu_attribute_iter_get_name(gMenuAttributeIterPtr)?.toKString() ?: ""

    public actual val value: Variant
        get() = Variant.fromPointer(g_menu_attribute_iter_get_value(gMenuAttributeIterPtr))

    override fun close() {
        g_object_unref(gMenuAttributeIterPtr)
    }

    public actual fun next(): Boolean = g_menu_attribute_iter_next(gMenuAttributeIterPtr) == TRUE
}
