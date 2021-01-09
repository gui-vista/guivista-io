package org.guiVista.io.application.action

import org.guiVista.core.ObjectBase
import org.guiVista.core.dataType.Variant

/** A simple [Action] implementation. */
public expect class SimpleAction : Action, ObjectBase {
    public fun changedEnabled(enabled: Boolean)

    public fun changeStateHint(stateHint: Variant)
}
