package org.guiVista.io.application

import gio2.*
import glib2.g_object_unref
import kotlinx.cinterop.CPointer
import org.guiVista.core.ObjectBase

/** User Notifications (pop up messages). */
actual class Notification actual constructor(title: String) : ObjectBase {
    val gNotificationPtr: CPointer<GNotification>? = g_notification_new(title)

    actual fun changeTitle(newTitle: String) {
        g_notification_set_title(gNotificationPtr, newTitle)
    }

    actual fun changeBody(newBody: String) {
        g_notification_set_body(gNotificationPtr, newBody)
    }

    /**
     * Sets the priority of [Notification] to the [new priority][newPriority]. See GNotificationPriority for possible
     * values.
     * @param newPriority The new priority to use.
     */
    fun changeIcon(newPriority: GNotificationPriority) {
        g_notification_set_priority(gNotificationPtr, newPriority)
    }

    actual fun changeDefaultAction(detailedAction: String) {
        g_notification_set_default_action(gNotificationPtr, detailedAction)
    }

    actual fun addButton(label: String, detailedAction: String) {
        g_notification_add_button(notification = gNotificationPtr, label = label, detailed_action = detailedAction)
    }

    override fun close() {
        g_object_unref(gNotificationPtr)
    }
}
