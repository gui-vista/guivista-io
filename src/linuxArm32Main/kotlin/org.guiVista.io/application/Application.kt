package org.guiVista.io.application

import gio2.GApplication
import gio2.G_APPLICATION_FLAGS_NONE
import gio2.g_application_new
import kotlinx.cinterop.CPointer

public actual class Application actual constructor(id: String) : ApplicationBase {
    override val gApplicationPtr: CPointer<GApplication>? =
        if (gio2.g_application_id_is_valid(id) == glib2.TRUE) g_application_new(id, G_APPLICATION_FLAGS_NONE)
        else throw IllegalArgumentException("Application ID isn't valid.")
}
