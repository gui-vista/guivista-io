package org.guiVista.io.application

import org.guiVista.core.ObjectBase
import org.guiVista.io.application.action.ActionGroup
import org.guiVista.io.application.action.ActionMap

/** Base interface for application objects. */
public expect interface ApplicationBase : ActionMap, ActionGroup, ObjectBase
