package org.guiVista.io.application.action

import gio2.*
import glib2.TRUE
import kotlinx.cinterop.*
import org.guiVista.core.Error
import org.guiVista.core.dataType.Variant
import org.guiVista.core.dataType.VariantType

public actual interface Action {
    public val gActionPtr: CPointer<GAction>?

    /** The name of the action. */
    public val name: String
        get() = g_action_get_name(gActionPtr)?.toKString() ?: ""

    /** The parameter type. */
    public val paramType: VariantType?
        get() {
            val ptr = g_action_get_parameter_type(gActionPtr)
            return if (ptr != null) VariantType.fromPointer(ptr) else null
        }

    /** The state type if the action is stateful, otherwise *null* is returned. */
    public val stateType: VariantType?
        get() {
            val ptr = g_action_get_state_type(gActionPtr)
            return if (ptr != null) VariantType.fromPointer(ptr) else null
        }

    /**
     * The state range hint. Will return *null* if the action isn't stateful, or there is no hint about the valid range
     * of values for the state of the action.
     */
    public val stateHint: Variant?
        get() {
            val ptr = g_action_get_state_hint(gActionPtr)
            return if (ptr != null) Variant.fromPointer(ptr) else null
        }

    /**
     * If action is currently enabled. In the case of the action being disabled all calls to `g_action_activate()`, and
     * [changeState] have no effect. Default value is *true*.
     */
    public val enabled: Boolean
        get() = g_action_get_enabled(gActionPtr) == TRUE

    /**
     * Request for the state of [Action] to be changed to value. The action must be stateful, and value **MUST** be of
     * the correct type. See g_action_get_state_type().

    This call merely requests a change. The action may refuse to change its state or may change its state to something other than value . See g_action_get_state_hint().

    If [value] is floating then it is consumed.
     * @param value The new state.
     */
    public fun changeState(value: Variant) {
        g_action_change_state(gActionPtr, value.gVariantPtr)
    }

    /**
     * Queries the current state of action. If the action is not stateful then *null* will be returned. However if the
     * action is stateful then the type of the return value is the type given by [stateType]. The return value
     * (if non null) should be freed with [Variant.close] when it is no longer required.
     */
    public fun fetchState(): Variant? {
        val ptr = g_action_get_state(gActionPtr)
        return if (ptr != null) Variant.fromPointer(ptr) else null
    }

    /**
     * Activates the action. Note that [actionParam] **MUST** be the correct type of parameter for the action
     * (ie: the parameter type given at construction time). If the parameter type was *null* then [actionParam]
     * must also be *null*.
     *
     * If [actionParam] is floating then it is consumed.
     * @param actionParam The parameter for the activation.
     */
    public fun activate(actionParam: Variant?) {
        g_action_activate(gActionPtr, actionParam?.gVariantPtr)
    }

    /**
     * Parses a detailed action name into its separate name and target components. Detailed action names can have three
     * formats. First format is used to represent an action name with no target value, and consists of just an action
     * name containing no whitespace nor the characters ':', '(' or ')'. For example: "app.action". Second format is
     * used to represent an action with a target value that is a non-empty string consisting only of alphanumerics,
     * plus '-' and '.'. In that case the action name, and target value are separated by a double colon ("::"). For
     * example: "app.action::target". Third format is used to represent an action with any type of target value,
     * including strings. The target value follows the action name, surrounded in parens. For example:
     * "app.action(42)". If a tuple-typed value is desired, it must be specified in the same way, resulting in two sets
     * of parens, for example: "app.action((1,2,3))". A string target can be specified this way as well:
     * "app.action('target')". For strings, this third format must be used if * target value is empty or contains
     * characters other than alphanumerics, **-** and **.**.
     * @param detailedName A detailed action name.
     * @param actionName The action name.
     * @param targetValue The target value, or *null* for no target.
     * @param error The error to use, or *null* to not use it.
     * @return A value of *true* if successful, else *false* with [error] set.
     */
    public fun parseDetailedName(
        detailedName: String,
        actionName: String,
        targetValue: Variant?,
        error: Error? = null
    ): Boolean = memScoped {
        // TODO: Cover error handling.
        return g_action_parse_detailed_name(
            detailed_name = detailedName,
            action_name = cValuesOf(actionName.cstr.ptr),
            target_value = cValuesOf(targetValue?.gVariantPtr),
            error = null
        ) == TRUE
    }

    /**
     * Formats a detailed action name from [actionName] and [targetValue]. It is an error to call this function with
     * an invalid [action name][actionName]. This function is the opposite of [parseDetailedName]. It will produce a
     * String that can be parsed back to the [actionName] and [targetValue] by that function.
     * @param actionName A valid action name.
     * @param targetValue A target value, or *null*.
     * @return A detailed format String.
     * @see parseDetailedName
     */
    public fun printDetailedName(actionName: String, targetValue: Variant?): String =
        g_action_print_detailed_name(actionName, targetValue?.gVariantPtr)?.toKString() ?: ""
}
