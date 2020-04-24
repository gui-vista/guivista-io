package org.guiVista.io.application

import gio2.*
import glib2.TRUE
import glib2.g_object_unref
import glib2.gpointer
import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString
import org.guiVista.core.ObjectBase
import org.guiVista.core.connectGSignal
import org.guiVista.core.disconnectGSignal

private const val ACTIVATE_SIGNAL = "activate"
private const val STARTUP_SIGNAL = "startup"
private const val SHUTDOWN_SIGNAL = "shutdown"

actual interface ApplicationBase : ObjectBase {
    val gApplicationPtr: CPointer<GApplication>?

    /** The unique identifier for the application. Default value is *""* (an empty String). */
    val appId: String
        get() = g_application_get_application_id(gApplicationPtr)?.toKString() ?: ""

    /** Time (in ms) to stay alive after becoming idle. Default value is *0*. */
    var inactivityTimeout: UInt
        get() = g_application_get_inactivity_timeout(gApplicationPtr)
        set(value) = g_application_set_inactivity_timeout(gApplicationPtr, value)

    /** The base resource path for the application. Default value is *""* (an empty String). */
    var resourceBasePath: String
        get() = g_application_get_resource_base_path(gApplicationPtr)?.toKString() ?: ""
        set(value) = g_application_set_resource_base_path(gApplicationPtr, value)

    /**
     * Whether the application is currently marked as busy through `g_application_mark_busy()`, or
     * `g_application_bind_busy_property()`. Default value is *false*.
     */
    val isBusy: Boolean
        get() = g_application_get_is_busy(gApplicationPtr) == TRUE

    /** If `g_application_register()` has been called. Default value is *false*. */
    val isRegistered: Boolean
        get() = g_application_get_is_registered(gApplicationPtr) == TRUE

    /** If this application instance is remote. Default value is *false*. */
    val isRemote: Boolean
        get() = g_application_get_is_remote(gApplicationPtr) == TRUE

    /**
     * Connects the *activate* signal to a [slot] on a application. This signal is used for initialising the
     * application window, and is emitted on the primary instance when an activation occurs.
     * @param slot The event handler for the signal.
     * @param userData User data to pass through to the [slot].
     */
    fun connectActivateSignal(slot: CPointer<ActivateSlot>, userData: gpointer): UInt =
        connectGSignal(obj = gApplicationPtr, signal = ACTIVATE_SIGNAL, slot = slot, data = userData)

    /**
     * Connects the *startup* signal to a [slot] on a application. This signal is emitted on the primary
     * instance immediately after registration.
     * @param slot The event handler for the signal.
     * @param userData User data to pass through to the [slot].
     */
    fun connectStartupSignal(slot: CPointer<StartupSlot>, userData: gpointer): UInt =
        connectGSignal(obj = gApplicationPtr, signal = STARTUP_SIGNAL, slot = slot, data = userData)

    /**
     * Connects the *shutdown* signal to a [slot] on a application. The *shutdown* signal is emitted only on the
     * registered primary instance immediately after the main loop terminates.
     * @param slot The event handler for the signal.
     * @param userData User data to pass through to the [slot].
     */
    fun connectShutdownSignal(slot: CPointer<ShutdownSlot>, userData: gpointer): UInt =
        connectGSignal(obj = gApplicationPtr, signal = SHUTDOWN_SIGNAL, slot = slot, data = userData)

    /**
     * Runs the application.
     * @return A status code is returned when the application exits. Any code not equal to *0* means a error has
     * occurred.
     */
    fun run(): Int = g_application_run(gApplicationPtr, 0, null)

    override fun disconnectSignal(handlerId: ULong) {
        super.disconnectSignal(handlerId)
        disconnectGSignal(gApplicationPtr, handlerId.toUInt())
    }

    /**
     * Sends a [notification] on behalf of application to the desktop shell. There is no guarantee that the
     * notification is displayed immediately, or even at all. Notifications **may** persist after the application
     * exits. It will be D-Bus-activated when the [notification], or one of its actions is activated. Modifying
     * [notification] after this call has no effect. However the object can be reused for a later call to this function.
     *
     * The [id] may be any string that uniquely identifies the event for the [application][ApplicationBase]. It does
     * not need to be in any special format. For example, "new-message" might be appropriate for a notification about
     * new messages. If a previous notification was sent with the same [id] it will be replaced with [notification],
     * and shown again as if it was a new notification. This works even for notifications sent from a previous
     * execution of the [application][ApplicationBase], as long as [id] is the same string.
     *
     * Note that [id] may be *""* (an empty String), but it is impossible to replace, or withdraw notifications without
     * an id. If [notification] is no longer relevant it can be withdrawn with [withdrawNotification].
     * @param id The ID of the [application][ApplicationBase], or *""* (an empty String).
     * @param notification The notification to send.
     */
    fun sendNotification(id: String = "", notification: Notification) {
        g_application_send_notification(application = gApplicationPtr, id = id,
            notification = notification.gNotificationPtr)
    }

    /**
     * Withdraws a notification that was sent with [sendNotification]. This call does nothing if a notification with
     * [id] doesn't exist, or the notification was never sent. This function works even for notifications sent in
     * previous executions of this [application][ApplicationBase], as long as [id] is the same as it was for the sent
     * notification.
     *
     * Note that notifications are dismissed when the user clicks on one of the buttons in a notification, or triggers
     * its default action, so there is no need to explicitly withdraw the notification in that case.
     * @param id The ID of the previously sent notification..
     */
    fun withdrawNotification(id: String) {
        g_application_withdraw_notification(gApplicationPtr, id)
    }

    override fun close() {
        g_object_unref(gApplicationPtr)
    }
}

/**
 * The event handler for the *activate* signal. Arguments:
 * 1. application: CPointer<GApplication>
 * 2. userData: gpointer
 */
typealias ActivateSlot = CFunction<(application: CPointer<GApplication>, userData: gpointer) -> Unit>

/**
 * The event handler for the *startup* signal. Arguments:
 * 1. application: CPointer<GApplication>
 * 2. userData: gpointer
 */
typealias StartupSlot = CFunction<(application: CPointer<GApplication>, userData: gpointer) -> Unit>

/**
 * The event handler for the *shutdown* signal. Arguments:
 * 1. application: CPointer<GApplication>
 * 2. userData: gpointer
 */
typealias ShutdownSlot = CFunction<(application: CPointer<GApplication>, userData: gpointer) -> Unit>
