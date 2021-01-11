package org.guiVista.io.application.menu

import gio2.GMenuModel
import glib2.g_object_unref
import kotlinx.cinterop.CPointer

public actual class MenuModel(ptr: CPointer<GMenuModel>?) : MenuModelBase {
    override val gMenuModelPtr: CPointer<GMenuModel>? = ptr

    override fun close() {
        g_object_unref(gMenuModelPtr)
    }
}
