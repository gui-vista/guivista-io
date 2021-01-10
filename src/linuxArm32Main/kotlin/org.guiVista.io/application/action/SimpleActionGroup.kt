package org.guiVista.io.application.action

import gio2.GActionGroup
import gio2.GActionMap
import gio2.GSimpleActionGroup
import gio2.g_simple_action_group_new
import glib2.g_object_unref
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret

public actual class SimpleActionGroup(ptr: CPointer<GSimpleActionGroup>? = null) : ActionGroup, ActionMap {
    public val gSimpleActionGroupPtr: CPointer<GSimpleActionGroup>? = ptr ?: g_simple_action_group_new()
    override val gActionGroupPtr: CPointer<GActionGroup>?
        get() = gSimpleActionGroupPtr?.reinterpret()
    override val gActionMapPtr: CPointer<GActionMap>?
        get() = gSimpleActionGroupPtr?.reinterpret()

    override fun close() {
        g_object_unref(gSimpleActionGroupPtr)
    }
}
