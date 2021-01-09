package org.guiVista.io.application.action

import gio2.GAction
import gio2.GPropertyAction
import gio2.g_property_action_new
import glib2.g_object_unref
import glib2.gpointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret
import org.guiVista.core.ObjectBase
import org.guiVista.core.disconnectGSignal

public actual class PropertyAction private constructor(ptr: CPointer<GPropertyAction>?) : Action, ObjectBase {
    public val gPropertyActionPtr: CPointer<GPropertyAction>? = ptr

    override val gActionPtr: CPointer<GAction>?
        get() = gPropertyActionPtr?.reinterpret()

    public companion object {
        public fun fromPointer(ptr: CPointer<GPropertyAction>?): PropertyAction = PropertyAction(ptr)

        /**
         * Creates an action corresponding to the value of [propName] on [obj]. The property **MUST** be existent, and
         * readable and writable (and not construct-only). This function takes a reference on [obj], and doesn't
         * release it until the action is destroyed.
         * @param name The name of action to create.
         * @param obj The object that has the property to wrap.
         * @param propName The name of the property.
         * @return A new [PropertyAction].
         */
        public fun create(name: String, obj: gpointer, propName: String): PropertyAction =
            PropertyAction(g_property_action_new(name = name, `object` = obj, property_name = propName))
    }

    override fun disconnectSignal(handlerId: ULong) {
        super.disconnectSignal(handlerId)
        disconnectGSignal(gPropertyActionPtr, handlerId.toUInt())
    }

    override fun close() {
        g_object_unref(gPropertyActionPtr)
    }
}
