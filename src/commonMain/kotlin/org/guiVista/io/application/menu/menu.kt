package org.guiVista.io.application.menu

/** A simple implementation of [MenuModelBase]. */
public expect class Menu : MenuModelBase {
    /**
     * Marks menu as frozen. After the menu is frozen it is an error to attempt to make any changes to it. In effect
     * this means that the [Menu] API can no longer be used. This function causes `MenuModelBase.isMutable` to begin
     * returning *false* which has some positive performance implications.
     */
    public fun freeze()

    /**
     * Convenience function for inserting a normal menu item into the menu.
     * @param pos The position at which to insert the item.
     * @param label The section label.
     * @param detailedAction The detailed action String.
     */
    public fun insert(pos: Int, label: String, detailedAction: String)

    /**
     * Convenience function for prepending a normal menu item to the start of the menu.
     * @param label The section label.
     * @param detailedAction The detailed action String.
     */
    public fun prepend(label: String, detailedAction: String)

    /**
     * Convenience function for appending a normal menu item to the end of the menu.
     * @param label The section label.
     * @param detailedAction The detailed action String.
     */
    public fun append(label: String, detailedAction: String)

    /**
     * Inserts item into the menu. The insertion is actually done by copying all of the attribute, and link values of
     * item and using them to form a new item within the menu. As such the item itself is not really inserted, but
     * rather a menu item that is exactly the same as the one presently described by item. This means that [item] is
     * essentially useless after the insertion occurs. Any changes you make to it are ignored unless it is inserted
     * again (at which point its updated values will be copied).
     *
     * You should probably just free item once you're done. There are many convenience functions to take care of common
     * cases.
     * @param pos The position at which to insert the item.
     * @param item The menu item to insert.
     * @see insert
     * @see insertSection
     * @see insertSubMenu
     * @see prepend
     * @see append
     */
    public fun insertItem(pos: Int, item: MenuItem)

    /**
     * Appends item to the end of menu.
     * @param item The menu item to append.
     * @see insertItem
     */
    public infix fun appendItem(item: MenuItem)

    /**
     * Prepends item to the start of menu.
     * @param item The menu item to prepend.
     */
    public infix fun prependItem(item: MenuItem)

    /**
     * Convenience function for inserting a section menu item into the menu.
     * @param pos The position at which to insert the item.
     * @param label The section label.
     * @param section A menu model with the items of the section.
     */
    public fun insertSection(pos: Int, label: String, section: MenuModelBase)

    /**
     * Convenience function for prepending a section menu item to the start of the menu.
     * @param label The section label.
     * @param section A menu model with the items of the section.
     */
    public fun prependSection(label: String, section: MenuModelBase)

    /**
     * Convenience function for appending a section menu item to the end of the menu.
     * @param label The section label.
     * @param section A menu model with the items of the section.
     */
    public fun appendSection(label: String, section: MenuModelBase)

    /**
     * Convenience function for appending a submenu menu item to the end of the menu.
     * @param label The section label.
     * @param subMenu A [MenuModelBase] with the items of the sub menu.
     */
    public fun appendSubMenu(label: String, subMenu: MenuModelBase)

    /**
     * Convenience function for inserting a submenu menu item into the menu.
     * @param pos The position at which to insert the item.
     * @param label The section label.
     * @param subMenu A [MenuModelBase] with the items of the sub menu.
     */
    public fun insertSubMenu(pos: Int, label: String, subMenu: MenuModelBase)

    /**
     * Convenience function for prepending a submenu menu item to the start of the menu.
     * @param label The section label.
     * @param subMenu A [MenuModelBase] with the items of the sub menu.
     */
    public fun prependSubMenu(label: String, subMenu: MenuModelBase)

    /**
     * Removes an item from the menu. The [position][pos] gives the index of the item to remove. It is an error if
     * [pos] is not in the range from 0 to one less than the number of items in the menu. It is not possible to remove
     * items by identity since items are added to the menu simply by copying their links, and attributes (ie: identity
     * of the item itself is not preserved).
     * @param pos The position of the item to remove.
     */
    public fun remove(pos: Int)

    /** Removes all items in the menu. */
    public fun removeAll()
}
