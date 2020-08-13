package org.guiVista.io.application

import org.guiVista.core.ObjectBase
import org.guiVista.io.Icon

/** User Notifications (pop up messages). */
expect class Notification(title: String) : ObjectBase {
    /**
     * Sets the icon of [Notification] to the [new icon][newIcon].
     * @param newIcon The new icon to use.
     */
    fun changeIcon(newIcon: Icon)

    /**
     * Sets the title of [Notification] to the [new title][newTitle].
     * @param newTitle The new title to use.
     */
    fun changeTitle(newTitle: String)

    /**
     * Sets the body of [Notification] to the [new body][newBody].
     * @param newBody The new body to use.
     */
    fun changeBody(newBody: String)

    /**
     * Sets the default action of [Notification] to [detailedAction]. This action is activated when the [Notification]
     * is clicked on. The action in [detailedAction] **must** be an application-wide action (it **must** start with
     * "app."). If [detailedAction] contains a target the given action will be activated with that target as its
     * parameter. See `g_action_parse_detailed_name()` for a description of the format for [detailedAction].
     *
     * When no default action is set the [application][ApplicationBase] that the notification was sent on is activated.
     * @param detailedAction A detailed action name.
     */
    fun changeDefaultAction(detailedAction: String)

    /**
     * Adds a button to [Notification] that activates the action in [detailedAction] when clicked. That action **must**
     * be an application-wide action (starting with "app."). If [detailedAction] contains a target the action will be
     * activated with that target as its parameter. See `g_action_parse_detailed_name()` for a description of the
     * format for [detailedAction].
     * @param label Label of the button.
     * @param detailedAction A detailed action name.
     */
    fun addButton(label: String, detailedAction: String)
}