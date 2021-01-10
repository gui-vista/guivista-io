package org.guiVista.io.application.action

import gio2.*
import glib2.FALSE
import glib2.GVariant
import glib2.TRUE
import glib2.gpointer
import kotlinx.cinterop.*
import org.guiVista.core.ObjectBase
import org.guiVista.core.connectGSignal
import org.guiVista.core.dataType.Variant
import org.guiVista.core.dataType.VariantType
import org.guiVista.core.disconnectGSignal

private const val ACTION_ADDED_SIGNAL = "action-added"
private const val ACTION_ENABLED_CHANGED_SIGNAL = "action-enabled-changed"
private const val ACTION_REMOVED_SIGNAL = "action-removed"
private const val ACTION_STATE_CHANGED = "action-state-changed"

public actual interface ActionGroup : ObjectBase {
    public val gActionGroupPtr: CPointer<GActionGroup>?

    /**
     * Lists the actions contained within [ActionGroup]. The caller is responsible for freeing the list with
     * `g_strfreev()` when it is no longer required.
     * @return An array of the names of the actions in the group.
     */
    public fun listActions(): Array<String> {
        val tmpResult = mutableListOf<String>()
        val tmpActions = g_action_group_list_actions(gActionGroupPtr)
        var running = true
        var pos = 0
        while (running) {
            val item = tmpActions?.get(pos)
            if (item == null) running = false
            else tmpResult += item.toKString()
            pos++
        }
        return tmpResult.toTypedArray()
    }

    /**
     * Queries all aspects of the named action within an [ActionGroup]. This function acquires the information
     * available from [hasAction], [fetchActionEnabled], [fetchActionParameterType], [fetchActionStateType],
     * [fetchActionStateHint], and [fetchActionState] with a single function call. This provides two main benefits.
     *
     * The first is the improvement in efficiency that comes with not having to perform repeated lookups of the action
     * in order to discover different things about it. The second is that implementing [ActionGroup] can now be done by
     * only overriding this function. The interface provides a default implementation of this function that calls the
     * individual functions, as required to fetch the information. The interface also provides default implementations
     * of those functions that call this function. All implementations **MUST** override either this function, or all
     * of the others.
     *
     * If the action exists then *true* is returned and any of the requested fields (as indicated by having a non null
     * reference passed in) are filled. However if the action doesn't exist then *false* is returned, and the fields
     * may or may not have been modified.
     * @param actionName The name of the action in the group.
     * @param enabled If the action is presently enabled.
     * @param paramType The parameter type, or *null* if none needed.
     * @param stateType The state type, or *null* if stateless..
     * @param stateHint The state hint, or *null* if none.
     * @param state The current state, or *null* if stateless.
     * @return A value of *true* if the action exists, otherwise *false*.
     */
    public fun queryAction(
        actionName: String,
        enabled: Boolean,
        paramType: VariantType?,
        stateType: VariantType?,
        stateHint: Variant,
        state: Variant
    ): Boolean = g_action_group_query_action(
        action_group = gActionGroupPtr,
        action_name = actionName,
        parameter_type = cValuesOf(paramType?.gVariantTypePtr),
        state_type = cValuesOf(stateType?.gVariantTypePtr),
        state_hint = cValuesOf(stateHint.gVariantPtr),
        state = cValuesOf(state.gVariantPtr),
        enabled = if (enabled) cValuesOf(TRUE) else cValuesOf(FALSE)
    ) == TRUE

    /**
     * Checks if the named action exists within [ActionGroup].
     * @param actionName The name of the action to check for.
     * @return Whether the named action exists.
     */
    public fun hasAction(actionName: String): Boolean = g_action_group_has_action(gActionGroupPtr, actionName) == TRUE

    /**
     * Checks if the named action within [ActionGroup] is currently enabled. An action **MUST** be enabled in order to
     * be activated, or in order to have its state changed from outside callers.
     * @param actionName The name of the action to query.
     * @return Whether or not the action is currently enabled.
     */
    public fun fetchActionEnabled(actionName: String): Boolean =
        g_action_group_get_action_enabled(gActionGroupPtr, actionName) == TRUE

    /**
     * Queries the type of the parameter that must be given when activating the named action within [ActionGroup]. When
     * activating the action using [activateAction], the [Variant] given to that function must be of the type returned
     * by this function. In the case that this function returns *null*, you must not give any [Variant], but *null*
     * instead.
     *
     * The parameter type of a particular action will never change, but it is possible for an action to be removed, and
     * for a new action to be added with the same name but a different parameter type.
     * @param actionName The name of the action to query.
     * @return The parameter type.
     */
    public fun fetchActionParameterType(actionName: String): VariantType? {
        val ptr = g_action_group_get_action_parameter_type(gActionGroupPtr, actionName)
        return if (ptr != null) VariantType.fromPointer(ptr) else null
    }

    /**
     * Queries the type of the state of the named action within [ActionGroup]. If the action is stateful then this
     * function returns the [VariantType] of the state. All calls to [changeActionState] **MUST** give a [Variant] of
     * this type, and [fetchActionState] will return a [Variant] of the same type. If the action is not stateful then
     * this function will return *null*. In that case, [fetchActionState] will return *null*, and you must not call
     * [changeActionState].
     *
     * The state type of a particular action will never change, but it is possible for an action to be removed, and for
     * a new action to be added with the same name but a different state type.
     * @param actionName The name of the action to query.
     * @return The state type, if the action is stateful.
     */
    public fun fetchActionStateType(actionName: String): VariantType? {
        val ptr = g_action_group_get_action_state_type(gActionGroupPtr, actionName)
        return if (ptr != null) VariantType.fromPointer(ptr) else null
    }

    /**
     * Requests a hint about the valid range of values for the state of the named action within [ActionGroup]. If
     * *null* is returned it either means that the action is not stateful, or that there is no hint about the valid
     * range of values for the state of the action. If a [Variant] array is returned then each item in the array is a
     * possible value for the state. If a [Variant] pair (ie: two-tuple) is returned then the tuple specifies the
     * inclusive lower, and upper bound of valid values for the state.
     *
     * In any case the information is merely a hint. It may be possible to have a state value outside of the hinted
     * range, and setting a value within the range may fail. The return value (if non null) should be freed with
     * [Variant.close] when it is no longer required.
     * @param actionName The name of the action to query.
     * @return The state range hint
     */
    public fun fetchActionStateHint(actionName: String): Variant? {
        val ptr = g_action_group_get_action_state_hint(gActionGroupPtr, actionName)
        return if (ptr != null) Variant.fromPointer(ptr) else null
    }

    /**
     * Queries the current state of the named action within [ActionGroup]. If the action is not stateful then *null*
     * will be returned. If the action is stateful then the type of the return value is the type given by
     * [fetchActionStateType]. The return value (if non null) should be freed with [Variant.close] when it is no
     * longer required.
     * @param actionName The name of the action to query.
     * @return The current state of the action.
     */
    public fun fetchActionState(actionName: String): Variant? {
        val ptr = g_action_group_get_action_state(gActionGroupPtr, actionName)
        return if (ptr != null) Variant.fromPointer(ptr) else null
    }

    /**
     * Request for the state of the named action within [ActionGroup] to be changed to [value]. The action **MUST** be
     * stateful, and [value] **MUST** be of the correct type. This call merely requests a change. The action may refuse
     * to change its state, or may change its state to something other than [value]. If the value GVariant is floating
     * then it is consumed.
     * @param actionName The name of the action to request the change on.
     * @param value The new state.
     * @see fetchActionStateType
     * @see fetchActionStateHint
     */
    public fun changeActionState(actionName: String, value: Variant) {
        g_action_group_change_action_state(action_group = gActionGroupPtr, action_name = actionName,
            value = value.gVariantPtr)
    }

    /**
     * Activate the named action within [ActionGroup]. If the action is expecting a parameter then the correct type of
     * parameter **MUST** be given as parameter. If the action is expecting no parameters then [param] **MUST** be
     * *null*.
     * @See fetchActionParameterType
     */
    public fun activateAction(actionName: String, param: Variant?) {
        g_action_group_activate_action(action_group = gActionGroupPtr, action_name = actionName,
            parameter = param?.gVariantPtr)
    }

    /**
     * Connects the *action-added* signal to a [slot] on a [ActionGroup]. This signal occurs after the action has been
     * added and is now visible.
     * @param slot The event handler for the signal.
     * @param userData User data to pass through to the [slot].
     */
    public fun connectActionAddedSignal(slot: CPointer<ActionAddedSlot>, userData: gpointer): ULong =
        connectGSignal(obj = gActionGroupPtr, signal = ACTION_ADDED_SIGNAL, slot = slot, data = userData).toULong()

    /**
     * Connects the *action-enabled-changed* signal to a [slot] on a [ActionGroup]. This signal indicates that the
     * enabled status of the named action has changed.
     * @param slot The event handler for the signal.
     * @param userData User data to pass through to the [slot].
     */
    public fun connectActionEnabledChangedSignal(slot: CPointer<ActionEnabledChangedSlot>, userData: gpointer): ULong =
        connectGSignal(obj = gActionGroupPtr, signal = ACTION_ENABLED_CHANGED_SIGNAL, slot = slot, data = userData)
            .toULong()

    /**
     * Connects the *action-removed* signal to a [slot] on a [ActionGroup]. This signal occurs before the action is
     * removed, so the action is still visible, and can be queried from the signal handler.
     * @param slot The event handler for the signal.
     * @param userData User data to pass through to the [slot].
     */
    public fun connectActionRemovedSignal(slot: CPointer<ActionRemovedSlot>, userData: gpointer): ULong =
        connectGSignal(obj = gActionGroupPtr, signal = ACTION_REMOVED_SIGNAL, slot = slot, data = userData).toULong()

    /**
     * Connects the *action-state-changed* signal to a [slot] on a [ActionGroup]. This signal indicates that the state
     * of the named action has changed.
     * @param slot The event handler for the signal.
     * @param userData User data to pass through to the [slot].
     */
    public fun connectActionStateChangedSignal(slot: CPointer<ActionStateChangedSlot>, userData: gpointer): ULong =
        connectGSignal(obj = gActionGroupPtr, signal = ACTION_STATE_CHANGED, slot = slot, data = userData).toULong()

    override fun disconnectSignal(handlerId: ULong) {
        super.disconnectSignal(handlerId)
        disconnectGSignal(gActionGroupPtr, handlerId.toUInt())
    }
}

/**
 * The event handler for the *action-added* signal. Arguments:
 * 1. actionGroup: CPointer<GActionGroup>
 * 2. actionName: CPointer<ByteVar>
 * 3. userData: gpointer
 */
public typealias ActionAddedSlot = CFunction<(
    actionGroup: CPointer<GActionGroup>,
    actionName: CPointer<ByteVar>,
    userData: gpointer
) -> Unit>

/**
 * The event handler for the *action-enabled-changed* signal. Arguments:
 * 1. actionGroup: CPointer<GActionGroup>
 * 2. actionName: CPointer<ByteVar>
 * 3. enabled: Int
 * 4. userData: gpointer
 */
public typealias ActionEnabledChangedSlot = CFunction<(
    actionGroup: CPointer<GActionGroup>,
    actionName: CPointer<ByteVar>,
    enabled: Int,
    userData: gpointer
) -> Unit>

/**
 * The event handler for the *action-removed* signal. Arguments:
 * 1. actionGroup: CPointer<GActionGroup>
 * 2. actionName: CPointer<ByteVar>
 * 3. userData: gpointer
 */
public typealias ActionRemovedSlot = CFunction<(
    actionGroup: CPointer<GActionGroup>,
    actionName: CPointer<ByteVar>,
    userData: gpointer
) -> Unit>

/**
 * The event handler for the *action-state-changed* signal. Arguments:
 * 1. actionGroup: CPointer<GActionGroup>
 * 2. actionName: CPointer<ByteVar>
 * 3. value: CPointer<GVariant>
 * 4. userData: gpointer
 */
public typealias ActionStateChangedSlot = CFunction<(
    actionGroup: CPointer<GActionGroup>,
    actionName: CPointer<ByteVar>,
    value: CPointer<GVariant>,
    userData: gpointer
) -> Unit>
