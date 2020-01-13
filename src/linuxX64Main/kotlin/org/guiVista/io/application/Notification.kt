package org.guiVista.io.application

import gio2.*
import glib2.g_object_unref
import kotlinx.cinterop.CPointer
import org.guiVista.core.ObjectBase

/** User Notifications (pop up messages). */
class Notification(title: String) : ObjectBase {
    val gNotificationPtr: CPointer<GNotification>? = g_notification_new(title)

    /**
     * Sets the title of [Notification] to the [new title][newTitle].
     * @param newTitle The new title to use.
     */
    fun changeTitle(newTitle: String) {
        g_notification_set_title(gNotificationPtr, newTitle)
    }

    /**
     * Sets the body of [Notification] to the [new body][newBody].
     * @param newBody The new body to use.
     */
    fun changeBody(newBody: String) {
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

    /**
     * Sets the default action of [Notification] to [detailedAction]. This action is activated when the [Notification]
     * is clicked on. The action in [detailedAction] **must** be an application-wide action (it **must** start with
     * "app."). If [detailedAction] contains a target the given action will be activated with that target as its
     * parameter. See `g_action_parse_detailed_name()` for a description of the format for [detailedAction].
     *
     * When no default action is set the [application][ApplicationBase] that the notification was sent on is activated.
     * @param detailedAction A detailed action name.
     */
    fun changeDefaultAction(detailedAction: String) {
        g_notification_set_default_action(gNotificationPtr, detailedAction)
    }

    /**
     * Adds a button to [Notification] that activates the action in [detailedAction] when clicked. That action **must**
     * be an application-wide action (starting with "app."). If [detailedAction] contains a target the action will be
     * activated with that target as its parameter. See `g_action_parse_detailed_name()` for a description of the
     * format for [detailedAction].
     * @param label Label of the button.
     * @param detailedAction A detailed action name.
     */
    fun addButton(label: String, detailedAction: String) {
        g_notification_add_button(notification = gNotificationPtr, label = label, detailed_action = detailedAction)
    }

    override fun close() {
        g_object_unref(gNotificationPtr)
    }
}
