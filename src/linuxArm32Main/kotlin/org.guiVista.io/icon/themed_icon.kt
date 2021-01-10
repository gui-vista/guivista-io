package org.guiVista.io.icon

import gio2.*
import kotlinx.cinterop.*

public actual class ThemedIcon private constructor(ptr: CPointer<GThemedIcon>?) : IconBase {
    public val gThemedIconPtr: CPointer<GThemedIcon>? = ptr
    override val gIconPtr: CPointer<GIcon>?
        get() = gThemedIconPtr?.reinterpret()

    public actual infix fun prependName(iconName: String) {
        g_themed_icon_prepend_name(gThemedIconPtr, iconName)
    }

    public actual infix fun appendName(iconName: String) {
        g_themed_icon_append_name(gThemedIconPtr, iconName)
    }

    public actual fun fetchNames(): Array<String> {
        val tmpResult = mutableListOf<String>()
        val tmpNames = g_themed_icon_get_names(gThemedIconPtr)
        var running = true
        var pos = 0
        while (running) {
            val item = tmpNames?.get(pos)
            pos++
            if (item != null) tmpResult += item.toKString()
            else running = false
        }
        return tmpResult.toTypedArray()
    }

    public actual companion object {
        public fun fromPointer(ptr: CPointer<GThemedIcon>?): ThemedIcon = ThemedIcon(ptr)

        public actual fun create(iconName: String): ThemedIcon = ThemedIcon(g_themed_icon_new(iconName)?.reinterpret())

        public actual fun fromNames(vararg iconNames: String): ThemedIcon {
            val result = create(iconNames.first())
            @Suppress("ReplaceRangeToWithUntil")
            iconNames.slice(1..(iconNames.size - 1)).forEach { result appendName it }
            return result
        }

        public actual fun createWithDefaultFallbacks(iconName: String): ThemedIcon =
            ThemedIcon(g_themed_icon_new_with_default_fallbacks(iconName)?.reinterpret())
    }
}

public fun themedIcon(ptr: CPointer<GThemedIcon>? = null, iconName: String, init: ThemedIcon.() -> Unit): ThemedIcon {
    val result = if (ptr != null) ThemedIcon.fromPointer(ptr) else ThemedIcon.create(iconName)
    result.init()
    return result
}
