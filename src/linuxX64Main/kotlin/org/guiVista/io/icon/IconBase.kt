package org.guiVista.io.icon

import gio2.GIcon
import kotlinx.cinterop.CPointer

public actual interface IconBase {
    public val gIconPtr: CPointer<GIcon>?
}
