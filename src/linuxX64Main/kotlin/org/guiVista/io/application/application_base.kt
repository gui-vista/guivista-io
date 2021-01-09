package org.guiVista.io.application

import gio2.*
import glib2.TRUE
import glib2.g_object_unref
import glib2.gpointer
import kotlinx.cinterop.*
import org.guiVista.core.ObjectBase
import org.guiVista.core.connectGSignal
import org.guiVista.core.disconnectGSignal
import org.guiVista.io.File

private const val ACTIVATE_SIGNAL = "activate"
private const val STARTUP_SIGNAL = "startup"
private const val SHUTDOWN_SIGNAL = "shutdown"
private const val OPEN_SIGNAL = "open"

public actual interface ApplicationBase : ObjectBase {
    public val gApplicationPtr: CPointer<GApplication>?

    /** The unique identifier for the application. Default value is *""* (an empty String). */
    public val appId: String
        get() = g_application_get_application_id(gApplicationPtr)?.toKString() ?: ""

    /** Time (in ms) to stay alive after becoming idle. Default value is *0*. */
    public var inactivityTimeout: UInt
        get() = g_application_get_inactivity_timeout(gApplicationPtr)
        set(value) = g_application_set_inactivity_timeout(gApplicationPtr, value)

    /** The base resource path for the application. Default value is *""* (an empty String). */
    public var resourceBasePath: String
        get() = g_application_get_resource_base_path(gApplicationPtr)?.toKString() ?: ""
        set(value) = g_application_set_resource_base_path(gApplicationPtr, value)

    /**
     * Whether the application is currently marked as busy through `g_application_mark_busy()`, or
     * `g_application_bind_busy_property()`. Default value is *false*.
     */
    public val isBusy: Boolean
        get() = g_application_get_is_busy(gApplicationPtr) == TRUE

    /** If `g_application_register()` has been called. Default value is *false*. */
    public val isRegistered: Boolean
        get() = g_application_get_is_registered(gApplicationPtr) == TRUE

    /** If this application instance is remote. Default value is *false*. */
    public val isRemote: Boolean
        get() = g_application_get_is_remote(gApplicationPtr) == TRUE

    /**
     * Increases the use count of the application. Use this function to indicate that the application has a reason to
     * continue to run. For example, [hold] is called by GTK+ when a top level window is on the screen. To cancel the
     * hold call [release].
     */
    public fun hold() {
        g_application_hold(gApplicationPtr)
    }

    /**
     * Decrease the use count of the application. When the use count reaches *0* the application will stop running.
     * **Never** call this function except to cancel the effect of a previous call to [hold].
     */
    public fun release() {
        g_application_release(gApplicationPtr)
    }

    /**
     * Immediately quits the application. Upon return to the main loop, [run] will return, calling only the 'shutdown'
     * function before doing so. The hold count is ignored. Take care if your code has called [hold] on the
     * application, and is therefore still expecting it to exist. **Note** that you may have called [hold] indirectly,
     * for example through `gtk_application_add_window`.
     *
     * The result of calling [run] again after it returns is unspecified.
     */
    public fun quit() {
        g_application_quit(gApplicationPtr)
    }

    /**
     * Opens the given files. In essence this results in the *open* signal being emitted in the primary instance. The
     * [hint] is simply passed through to the ::open signal. It is intended to be used by applications that have
     * multiple modes for opening files (eg: *view* vs *edit*, etc). Unless you have a need for this functionality you
     * should not set the [hint] parameter.
     *
     * The application **MUST** be registered before calling this function, and it **MUST** have the
     * `G_APPLICATION_HANDLES_OPEN` flag set.
     * @param files An array of files to open.
     * @param hint The hint to use.
     */
    public fun open(files: Array<File>, hint: String = "") {
        if (files.isEmpty()) throw IllegalArgumentException("The files parameter cannot be empty.")
        val tmp = files.map { it.gFilePtr }.toTypedArray()
        g_application_open(application = gApplicationPtr, files = cValuesOf(*tmp), n_files = files.size, hint = hint)
    }

    /**
     * Increases the busy count of the application. Use this function to indicate that the application is busy, for
     * instance while a long running operation is pending. The busy state will be exposed to other processes, so a
     * session shell will use that information to indicate the state to the user (e.g. with a spinner).
     *
     * To cancel the busy indication use [unmarkBusy].
     */
    public fun markBusy() {
        g_application_mark_busy(gApplicationPtr)
    }

    /**
     * Decreases the busy count of the application. When the busy count reaches *0* the new state will be propagated to
     * other processes. This function **MUST** only be called to cancel the effect of a previous call to [markBusy].
     */
    public fun unmarkBusy() {
        g_application_unmark_busy(gApplicationPtr)
    }

    /**
     * Connects the *activate* signal to a [slot] on a application. This signal is used for initialising the
     * application window, and is emitted on the primary instance when an activation occurs.
     * @param slot The event handler for the signal.
     * @param userData User data to pass through to the [slot].
     * @return The handler ID.
     */
    public fun connectActivateSignal(slot: CPointer<ActivateSlot>, userData: gpointer): ULong =
        connectGSignal(obj = gApplicationPtr, signal = ACTIVATE_SIGNAL, slot = slot, data = userData)

    /**
     * Connects the *startup* signal to a [slot] on a application. This signal is emitted on the primary
     * instance immediately after registration.
     * @param slot The event handler for the signal.
     * @param userData User data to pass through to the [slot].
     * @return The handler ID.
     */
    public fun connectStartupSignal(slot: CPointer<StartupSlot>, userData: gpointer): ULong =
        connectGSignal(obj = gApplicationPtr, signal = STARTUP_SIGNAL, slot = slot, data = userData)

    /**
     * Connects the *shutdown* signal to a [slot] on a application. The *shutdown* signal is emitted only on the
     * registered primary instance immediately after the main loop terminates.
     * @param slot The event handler for the signal.
     * @param userData User data to pass through to the [slot].
     * @return The handler ID.
     */
    public fun connectShutdownSignal(slot: CPointer<ShutdownSlot>, userData: gpointer): ULong =
        connectGSignal(obj = gApplicationPtr, signal = SHUTDOWN_SIGNAL, slot = slot, data = userData)

    /**
     * Connects the *open* signal to a [slot] on a application. The *open* signal is emitted only when there are files
     * to open. Refer to [open][ApplicationBase.open] for more information.
     * @param slot The event handler for the signal.
     * @param userData User data to pass through to the [slot].
     * @return The handler ID.
     */
    public fun connectOpenSignal(slot: CPointer<OpenSlot>, userData: gpointer): ULong =
        connectGSignal(obj = gApplicationPtr, signal = OPEN_SIGNAL, slot = slot, data = userData)

    /**
     * Runs the application.
     * @return A status code is returned when the application exits. Any code not equal to *0* means a error has
     * occurred.
     */
    public fun run(): Int = g_application_run(application = gApplicationPtr, argc = 0, argv = null)

    override fun disconnectSignal(handlerId: ULong) {
        super.disconnectSignal(handlerId)
        disconnectGSignal(gApplicationPtr, handlerId)
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
    public fun sendNotification(id: String = "", notification: Notification) {
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
    public fun withdrawNotification(id: String) {
        g_application_withdraw_notification(gApplicationPtr, id)
    }

    override fun close() {
        g_object_unref(gApplicationPtr)
    }

    public companion object {
        /**
         * Checks if [id] is a valid application identifier. A valid ID is required for calls to
         * `Application()`, and [appId]. All application identifiers follow the same
         * format as D-Bus well-known bus names. For convenience the restrictions on application identifiers are
         * reproduced here:
         *
         * - Application identifiers are composed of 1 or more elements separated by a period (.) character. All elements must contain at least one character.
         * - Each element must only contain the ASCII characters [A-Z][a-z][0-9]_-, with - discouraged in new application identifiers. Each element must not begin with a digit.
         * - Application identifiers must contain at least one . (period) character (and thus at least two elements).
         * - Application identifiers must not begin with a . (period) character.
         * - Application identifiers must not exceed 255 characters.
         * @param id A potential application identifier.
         * @return A value of *true* if [id] is valid.
         */
        public fun validAppId(id: String): Boolean = g_application_id_is_valid(id) == TRUE
    }
}

/**
 * The event handler for the *activate* signal. Arguments:
 * 1. application: CPointer<GApplication>
 * 2. userData: gpointer
 */
public typealias ActivateSlot = CFunction<(application: CPointer<GApplication>, userData: gpointer) -> Unit>

/**
 * The event handler for the *startup* signal. Arguments:
 * 1. application: CPointer<GApplication>
 * 2. userData: gpointer
 */
public typealias StartupSlot = CFunction<(application: CPointer<GApplication>, userData: gpointer) -> Unit>

/**
 * The event handler for the *shutdown* signal. Arguments:
 * 1. application: CPointer<GApplication>
 * 2. userData: gpointer
 */
public typealias ShutdownSlot = CFunction<(application: CPointer<GApplication>, userData: gpointer) -> Unit>

/**
 * The event handler for the *open* signal that occurs when there are files to open. Arguments:
 * 1. application: CPointer<GApplication>
 * 2. files: gpointer
 * 3. totalFiles: Int
 * 4. hint: CPointer<ByteVar>
 * 5. userData: gpointer
 */
public typealias OpenSlot = CFunction<(
    application: CPointer<GApplication>,
    files: gpointer,
    totalFiles: Int,
    hint: CPointer<ByteVar>,
    userData: gpointer
) -> Unit>
