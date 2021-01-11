package org.guiVista.io.application.menu

import gio2.*
import glib2.g_object_unref
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret

public actual class Menu(ptr: CPointer<GMenu>? = null) : MenuModelBase {
    public val gMenuPtr: CPointer<GMenu>? = ptr ?: g_menu_new()
    override val gMenuModelPtr: CPointer<GMenuModel>?
        get() = gMenuPtr?.reinterpret()

    override fun close() {
        g_object_unref(gMenuPtr)
    }

    public actual fun freeze() {
        g_menu_freeze(gMenuPtr)
    }

    public actual fun insert(pos: Int, label: String, detailedAction: String) {
        g_menu_insert(menu = gMenuPtr, position = pos, label = label, detailed_action = detailedAction)
    }

    public actual fun prepend(label: String, detailedAction: String) {
        g_menu_prepend(menu = gMenuPtr, label = label, detailed_action = detailedAction)
    }

    public actual fun append(label: String, detailedAction: String) {
        g_menu_append(menu = gMenuPtr, label = label, detailed_action = detailedAction)
    }

    public actual fun insertItem(pos: Int, item: MenuItem) {
        g_menu_insert_item(menu = gMenuPtr, position = pos, item = item.gMenuItemPtr)
    }

    public actual infix fun appendItem(item: MenuItem) {
        g_menu_append_item(gMenuPtr, item.gMenuItemPtr)
    }

    public actual infix fun prependItem(item: MenuItem) {
        g_menu_prepend_item(gMenuPtr, item.gMenuItemPtr)
    }

    public actual fun insertSection(pos: Int, label: String, section: MenuModelBase) {
        g_menu_insert_section(menu = gMenuPtr, label = label, position = pos, section = section.gMenuModelPtr)
    }

    public actual fun prependSection(label: String, section: MenuModelBase) {
        g_menu_prepend_section(menu = gMenuPtr, label = label, section = section.gMenuModelPtr)
    }

    public actual fun appendSection(label: String, section: MenuModelBase) {
        g_menu_append_section(menu = gMenuPtr, label = label, section = section.gMenuModelPtr)
    }

    public actual fun appendSubMenu(label: String, subMenu: MenuModelBase) {
        g_menu_append_submenu(menu = gMenuPtr, label = label, submenu = subMenu.gMenuModelPtr)
    }

    public actual fun insertSubMenu(pos: Int, label: String, subMenu: MenuModelBase) {
        g_menu_insert_submenu(menu = gMenuPtr, position = pos, label = label, submenu = subMenu.gMenuModelPtr)
    }

    public actual fun prependSubMenu(label: String, subMenu: MenuModelBase) {
        g_menu_prepend_submenu(menu = gMenuPtr, label = label, submenu = subMenu.gMenuModelPtr)
    }

    public actual fun remove(pos: Int) {
        g_menu_remove(gMenuPtr, pos)
    }

    public actual fun removeAll() {
        g_menu_remove_all(gMenuPtr)
    }
}
