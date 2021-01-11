package org.guiVista.io.application.menu

import gio2.*
import glib2.TRUE
import kotlinx.cinterop.CPointer
import org.guiVista.core.dataType.Variant
import org.guiVista.core.dataType.VariantType
import org.guiVista.io.icon.IconBase

public actual class MenuItem private constructor(ptr: CPointer<GMenuItem>?) {
    public val gMenuItemPtr: CPointer<GMenuItem>? = ptr

    public actual companion object {
        public fun fromPointer(ptr: CPointer<GMenuItem>?): MenuItem = MenuItem(ptr)

        public actual fun createSection(label: String, section: MenuModelBase): MenuItem =
            MenuItem(g_menu_item_new_section(if (label.isNotEmpty()) label else null, section.gMenuModelPtr))

        public actual fun createSubMenu(label: String, subMenu: MenuModelBase): MenuItem =
            MenuItem(g_menu_item_new_submenu(label, subMenu.gMenuModelPtr))

        public actual fun fromModel(model: MenuModelBase, itemIndex: Int): MenuItem =
            MenuItem(g_menu_item_new_from_model(model.gMenuModelPtr, itemIndex))
    }

    public actual infix fun changeLabel(label: String) {
        g_menu_item_set_label(gMenuItemPtr, if (label.isNotEmpty()) label else null)
    }

    public actual infix fun changeIcon(icon: IconBase?) {
        g_menu_item_set_icon(gMenuItemPtr, icon?.gIconPtr)
    }

    public actual fun changeActionAndTargetValue(action: String, targetValue: Variant?) {
        g_menu_item_set_action_and_target_value(
            menu_item = gMenuItemPtr,
            action = if (action.isNotEmpty()) action else null,
            target_value = targetValue?.gVariantPtr
        )
    }

    public actual fun changeActionAndTarget(action: String, formatStr: String) {
        g_menu_item_set_action_and_target(
            menu_item = gMenuItemPtr,
            action = if (action.isNotEmpty()) action else null,
            format_string = formatStr
        )
    }

    public actual infix fun changeDetailedAction(detailedAction: String) {
        g_menu_item_set_detailed_action(gMenuItemPtr, detailedAction)
    }

    public actual infix fun changeSection(section: MenuModelBase?) {
        g_menu_item_set_section(gMenuItemPtr, section?.gMenuModelPtr)
    }

    public actual infix fun changeSubMenu(subMenu: MenuModelBase?) {
        g_menu_item_set_submenu(gMenuItemPtr, subMenu?.gMenuModelPtr)
    }

    public actual fun fetchAttributeValue(attrib: String, expectedType: VariantType): Variant? {
        val ptr = g_menu_item_get_attribute_value(menu_item = gMenuItemPtr, attribute = attrib,
            expected_type = expectedType.gVariantTypePtr)
        return if (ptr != null) Variant.fromPointer(ptr) else null
    }

    public actual fun fetchAttribute(attrib: String, formatStr: String): Boolean =
        g_menu_item_get_attribute(menu_item = gMenuItemPtr, attribute = attrib, format_string = formatStr) == TRUE

    public actual fun fetchLink(link: String): MenuModelBase? {
        val ptr = g_menu_item_get_link(gMenuItemPtr, link)
        return if (ptr != null) MenuModel(ptr) else null
    }

    public actual fun changeAttributeValue(attrib: String, value: Variant?) {
        g_menu_item_set_attribute_value(menu_item = gMenuItemPtr, attribute = attrib, value = value?.gVariantPtr)
    }

    public actual fun changeAttribute(attrib: String, formatStr: String) {
        g_menu_item_set_attribute(menu_item = gMenuItemPtr, attribute = attrib, format_string = formatStr)
    }

    public actual fun changeLink(link: String, model: MenuModelBase?) {
        g_menu_item_set_link(menu_item = gMenuItemPtr, link = link, model = model?.gMenuModelPtr)
    }
}

public fun menuItem(
    ptr: CPointer<GMenuItem>? = null,
    model: MenuModelBase? = null,
    itemIndex: Int = 0,
    init: MenuItem.() -> Unit
): MenuItem {
    val result =
        if (ptr != null && model == null) MenuItem.fromPointer(ptr)
        else if (model != null && ptr == null) MenuItem.fromModel(model, itemIndex)
        else throw IllegalArgumentException("Both ptr and model parameters cannot be null.")
    result.init()
    return result
}

public fun section(label: String, section: MenuModelBase, init: MenuItem.() -> Unit): MenuItem {
    val result = MenuItem.createSection(label, section)
    result.init()
    return result
}

public fun subMenu(label: String, subMenuModel: MenuModelBase, init: MenuItem.() -> Unit): MenuItem {
    val result = MenuItem.createSubMenu(label, subMenuModel)
    result.init()
    return result
}
