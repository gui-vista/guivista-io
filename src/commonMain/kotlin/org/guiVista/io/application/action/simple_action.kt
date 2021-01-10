package org.guiVista.io.application.action

import org.guiVista.core.ObjectBase
import org.guiVista.core.dataType.Variant

/** A simple [Action] implementation. */
public expect class SimpleAction : Action, ObjectBase {
    /**
     * Sets the action as enabled or not. An action **MUST** be enabled in order to be activated, or in order to have
     * its state changed from outside callers. This should only be called by the implementor of the action. Users of
     * the action should not attempt to modify its enabled flag.
     * @param enabled Whether the action is enabled.
     */
    public fun changedEnabled(enabled: Boolean)

    /**
     * Sets the state hint for the action.
     * @param stateHint A [Variant] representing that state hint.
     */
    public fun changeStateHint(stateHint: Variant)

    /**
     * Sets the state of the action. This directly updates the **state** property to the given value. This should only
     * be called by the implementor of the action. Users of the action should not attempt to directly modify the
     * **state** property. Instead they should call `g_action_change_state` to request the change.
     *
     * If the [value] is floating then it is consumed.
     * @param value The new [Variant] for the state.
     */
    public fun changeState(value: Variant)
}
