package org.guiVista.io

import gio2.*
import kotlinx.cinterop.CPointer

/** A system for reporting a error. */
class Error private constructor(errorPtr: CPointer<GError>?) : Closable {
    val gErrorPtr: CPointer<GError>? = errorPtr

    companion object {
        /** Creates a new [Error] instance from a errorPtr. */
        fun fromErrorPtr(errorPtr: CPointer<GError>): Error = Error(errorPtr)

        /**
         * Creates a new [Error] instance. Unlike `g_error_new()`, [message] isn't a `printf` style format string. Use
         * this function if [message] contains text you don't have control over, that could include `printf` escape
         * sequences.
         * @param domain Error domain.
         * @param code Error code.
         * @param message Error message.
         * @return A new [Error] instance.
         */
        fun fromLiteral(domain: GQuark, code: Int, message: String): Error =
            Error(g_error_new_literal(domain = domain, code = code, message = message))
    }

    override fun close() {
        g_error_free(gErrorPtr)
    }

    /**
     * Checks to see if the error matches the [domain], and [code]. In particular when error is *null*, *false* will be
     * returned. If [domain] contains a FAILED (or otherwise generic) error code you should generally not check for it
     * explicitly, but should instead treat any not explicitly recognized error code as being equivalent to the FAILED
     * code. This way if the [domain] is extended in the future to provide a more specific error code for a certain
     * case, your code will still work.
     * @param domain An error domain.
     * @param code An error code.
     * @return If error matches [domain], and [code] then *true*, otherwise *false*.
     */
    fun matches(domain: GQuark, code: Int): Boolean =
        g_error_matches(error = gErrorPtr, domain = domain, code = code) == TRUE
}
