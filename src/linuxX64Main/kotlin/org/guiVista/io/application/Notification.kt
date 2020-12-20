package org.guiVista.io.application

import gio2.*
import glib2.g_object_unref
import kotlinx.cinterop.CPointer
import org.guiVista.core.ObjectBase
import org.guiVista.io.Icon

/** User Notifications (pop up messages). */
public actual class Notification actual constructor(title: String) : ObjectBase {
    public val gNotificationPtr: CPointer<GNotification>? = g_notification_new(title)

    public actual fun changeIcon(newIcon: Icon) {
        g_notification_set_icon(gNotificationPtr, newIcon.gIconPtr)
    }

    public actual fun changeTitle(newTitle: String) {
        g_notification_set_title(gNotificationPtr, newTitle)
    }

    public actual fun changeBody(newBody: String) {
        g_notification_set_body(gNotificationPtr, newBody)
    }

    /**
     * Sets the priority of [Notification] to the [new priority][newPriority]. See GNotificationPriority for possible
     * values.
     * @param newPriority The new priority to use.
     */
    public fun changeIcon(newPriority: GNotificationPriority) {
        g_notification_set_priority(gNotificationPtr, newPriority)
    }

    public actual fun changeDefaultAction(detailedAction: String) {
        g_notification_set_default_action(gNotificationPtr, detailedAction)
    }

    public actual fun addButton(label: String, detailedAction: String) {
        g_notification_add_button(notification = gNotificationPtr, label = label, detailed_action = detailedAction)
    }

    override fun close() {
        g_object_unref(gNotificationPtr)
    }
}
