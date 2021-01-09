package org.guiVista.io.application.action

import gio2.GActionMap
import gio2.g_action_map_add_action
import gio2.g_action_map_lookup_action
import gio2.g_action_map_remove_action
import kotlinx.cinterop.CPointer

public actual interface ActionMap {
    public val gActionMapPtr: CPointer<GActionMap>?

    /**
     * Looks up the action with the [name][actionName] in [ActionMap].
     * @param actionName The name of the action.
     * @return An instance of [Action] if it exists, otherwise *null*.
     */
    public fun lookupAction(actionName: String) {
        g_action_map_lookup_action(gActionMapPtr, actionName)
    }

    /**
     * Adds an action to the [ActionMap]. If the action map already contains an action with the same name as action
     * then the old action is dropped from the action map. The action map takes its own reference on [action].
     * @param action The [Action] to add.
     */
    public fun addAction(action: Action) {
        g_action_map_add_action(gActionMapPtr, action.gActionPtr)
    }

    /**
     * Removes the named action from the action map. If no action of this name is in the map then nothing happens.
     * @param actionName The name of the action to remove.
     */
    public fun removeAction(actionName: String) {
        g_action_map_remove_action(gActionMapPtr, actionName)
    }
}
