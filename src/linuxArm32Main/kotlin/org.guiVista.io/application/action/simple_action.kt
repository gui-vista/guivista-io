package org.guiVista.io.application.action

import gio2.*
import glib2.*
import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret
import org.guiVista.core.ObjectBase
import org.guiVista.core.connectGSignal
import org.guiVista.core.dataType.Variant
import org.guiVista.core.dataType.VariantType
import org.guiVista.core.disconnectGSignal

private const val ACTIVATE_SIGNAL = "activate"
private const val CHANGE_STATE_SIGNAL = "change-state"

public actual class SimpleAction private constructor(ptr: CPointer<GSimpleAction>?) : Action, ObjectBase {
    public val gSimpleActionPtr: CPointer<GSimpleAction>? = ptr

    override val gActionPtr: CPointer<GAction>?
        get() = gSimpleActionPtr?.reinterpret()

    public companion object {
        public fun fromPointer(ptr: CPointer<GSimpleAction>?): SimpleAction = SimpleAction(ptr)

        /**
         * Creates a new instance of [SimpleAction].
         * @param name The name of the action.
         * @param paramType The type of parameter that will be passed to handlers for the **activate** signal, or
         * *null* for no parameter.
         * @param state The initial state of the action, or *null* for no state.
         * @return A [SimpleAction] instance.
         */
        public fun create(name: String, paramType: VariantType? = null, state: Variant? = null): SimpleAction {
            val ptr =
                if (state != null) {
                    g_simple_action_new_stateful(name = name, parameter_type = paramType?.gVariantTypePtr,
                        state = state.gVariantPtr)
                } else {
                    g_simple_action_new(name, paramType?.gVariantTypePtr)
                }
            return SimpleAction(ptr)
        }
    }

    public actual fun changedEnabled(enabled: Boolean) {
        g_simple_action_set_enabled(gSimpleActionPtr, if (enabled) TRUE else FALSE)
    }

    actual override fun changeState(value: Variant) {
        g_simple_action_set_state(gSimpleActionPtr, value.gVariantPtr)
    }

    public actual fun changeStateHint(stateHint: Variant) {
        g_simple_action_set_state_hint(gSimpleActionPtr, stateHint.gVariantPtr)
    }

    override fun close() {
        g_object_unref(gSimpleActionPtr)
    }

    /**
     * Connects the *activate* signal to a [slot] on a [SimpleAction]. This signal indicates that the action was just
     * activated. The **param** parameter will always be of the expected type, i.e. the parameter type specified when
     * the action was created. If an incorrect type is given when activating the action then this signal isn't emitted.
     *
     * Since GLib 2.40, if no handler is connected to this signal then the default behaviour for boolean stated actions
     * with a *null* parameter type is to toggle them via the **change-state** signal. For stateful actions where the
     * state type is equal to the parameter type, the default is to forward them directly to **change-state**. This
     * should allow almost all users of [SimpleAction] to connect only one handler, or the other.
     * @param slot The event handler for the signal.
     * @param userData User data to pass through to the [slot].
     */
    public fun connectActivateSignal(slot: CPointer<ActivateSlot>, userData: gpointer): ULong =
        connectGSignal(obj = gSimpleActionPtr, signal = ACTIVATE_SIGNAL, slot = slot, data = userData).toULong()

    /**
     * Connects the *change-state* signal to a [slot] on a [SimpleAction]. This signal indicates that the action just
     * received a request to change its state. The **value** parameter will always be of the correct state type, i.e.
     * the type of the initial state passed to [SimpleAction.create]. If an incorrect type is given when requesting to
     * change the state then this signal isn't emitted.
     *
     * If no handler is connected to this signal then the default behaviour is to call [changeState] to set the state
     * to the requested value. If you connect a signal handler then no default action is taken. If the state should
     * change then you must call [changeState] from the handler.
     * @param slot The event handler for the signal.
     * @param userData User data to pass through to the [slot].
     */
    public fun connectChangeStateSignal(slot: CPointer<ChangeStateSlot>, userData: gpointer): ULong =
        connectGSignal(obj = gSimpleActionPtr, signal = CHANGE_STATE_SIGNAL, slot = slot, data = userData).toULong()

    override fun disconnectSignal(handlerId: ULong) {
        super.disconnectSignal(handlerId)
        disconnectGSignal(gSimpleActionPtr, handlerId.toUInt())
    }
}

/**
 * The event handler for the *activate* signal. Arguments:
 * 1. action: CPointer<GSimpleAction>
 * 2. param: CPointer<GVariant>?
 * 3. userData: gpointer
 */
public typealias ActivateSlot = CFunction<(
    action: CPointer<GSimpleAction>,
    param: CPointer<GVariant>?,
    userData: gpointer
) -> Unit>

/**
 * The event handler for the *change-state* signal. Arguments:
 * 1. action: CPointer<GSimpleAction>
 * 2. param: CPointer<GVariant>?
 * 3. userData: gpointer
 */
public typealias ChangeStateSlot = CFunction<(
    action: CPointer<GSimpleAction>,
    value: CPointer<GVariant>?,
    userData: gpointer
) -> Unit>
