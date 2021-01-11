package org.guiVista.io.application.menu

import org.guiVista.core.dataType.Variant
import org.guiVista.core.dataType.VariantType
import org.guiVista.io.icon.IconBase
import org.guiVista.io.application.action.Action
import org.guiVista.io.application.action.ActionGroup

/** A menu item. */
public expect class MenuItem {
    public companion object {
        /**
         * Creates a new menu item representing a section. The effect of having one menu appear as a section of another
         * is exactly as it sounds: the items from section become a direct part of the menu that the menu item is added
         * to.
         *
         * Visual separation is typically displayed between two non empty sections. If label is isn't empty (**""**)
         * then it will be incorporated into this visual indication. This allows for labeled subsections of a menu. As
         * a simple example consider a typical **Edit** menu from a simple program. It probably contains an **Undo**,
         * and **Redo** item, followed by a separator, followed by **Cut**, **Copy**, and **Paste**.
         *
         * This would be accomplished by creating three [Menu] instances. The first would be populated with the
         * **Undo**, and **Redo** items, and the second with the **Cut**, **Copy**, and **Paste** items. The first and
         * second menus would then be added as submenus of the third.
         * @param label The section label.
         * @param section A [MenuModelBase] with the items of the section.
         * @return A new [MenuItem].
         */
        public fun createSection(label: String, section: MenuModelBase): MenuItem

        /**
         * Creates a new menu item representing a sub menu.
         * @param label The section label.
         * @param subMenu A [MenuModelBase] with the items of the sub menu.
         * @return A new [MenuItem].
         */
        public fun createSubMenu(label: String, subMenu: MenuModelBase): MenuItem

        /**
         * Creates a [MenuItem] as an exact copy of an existing menu item in a [MenuModelBase]. The [itemIndex]
         * **MUST** be valid.
         * @param model The menu model.
         * @param itemIndex The index of an item in the [model].
         * @return A new [MenuItem].
         */
        public fun fromModel(model: MenuModelBase, itemIndex: Int): MenuItem
    }

    /**
     * Sets or unsets the **label** attribute of the menu item. If label is not empty then it is used as the label for
     * the menu item. If it is empty (**""**) then the label attribute is unset.
     * @param label The label to set, or **""** to unset.
     */
    public infix fun changeLabel(label: String)

    /**
     * Sets (or unsets) the icon on the menu item. This API is only intended for use with **noun** menu items; things
     * like bookmarks or applications in an **Open With** menu. Don't use it on menu items corresponding to verbs (eg:
     * stock icons for **Save** or **Quit**). If icon is *null* then the icon is unset.
     * @param icon An icon, or *null* to unset.
     */
    public infix fun changeIcon(icon: IconBase?)

    /**
     * Sets or unsets the **action**, and **target** attributes of the menu item. If [action] is empty (**""**) then
     * both the **action**, and **target** attributes are unset (and [targetValue] is ignored). However if [action] is
     * not empty then the **action** attribute is set. The **target** attribute is then set to the value of
     * [targetValue] if it is non null, or unset otherwise.
     *
     * Normal menu items (ie: not submenu, section or other custom item types) are expected to have the **action**
     * attribute set to identify the action that they are associated with. The state type of the action help to
     * determine the disposition of the menu item. In general clicking on the menu item will result in activation of
     * the named action with the **target** attribute given as the parameter to the action invocation.
     *
     * - If the **target** attribute is not set then the action is invoked with no parameter.
     * - If the action has no state then the menu item is usually drawn as a plain menu item (ie: with no additional
     * decoration).
     * - If the action has a boolean state then the menu item is usually drawn as a toggle menu item (ie: with a
     * checkmark or equivalent indication). The item should be marked as **toggled**, or **checked** when the Boolean
     * state is *true*.
     * - If the action has a string state then the menu item is usually drawn as a radio menu item (ie: with a radio
     * bullet or equivalent indication). The item should be marked as **selected** when the String state is equal to
     * the value of the target property.
     * @param action The name of the action for this item.
     * @param targetValue A [Variant] to use as the action target.
     * @see Action
     * @see ActionGroup
     * @see changeActionAndTarget
     * @see changeDetailedAction
     */
    public fun changeActionAndTargetValue(action: String, targetValue: Variant?)

    /**
     * Sets or unsets the **action**, and **target** attributes of the menu item. If action is empty (**""**) then both
     * the **action**, and **target** attributes are unset (and [formatStr] is ignored along with the positional
     * parameters). However if action is not empty (**""**) then the **action** attribute is set, and [formatStr] is
     * then inspected. If it is not empty then the proper position parameters are collected to create a [Variant]
     * instance to use as the target value. If it is null then the positional parameters are ignored, and the
     * **target** attribute is unset.
     * @param action the name of the action for this item.
     * @param formatStr A [Variant] format String.
     * @see changeActionAndTargetValue
     */
    public fun changeActionAndTarget(action: String, formatStr: String)

    /**
     * Sets the **action**, and possibly the **target** attribute of the menu item. The format of [detailedAction] is
     * the same format parsed by `Action.parseDetailedName`.
     * @param detailedAction The detailed action String.
     * @see changeActionAndTarget
     * @see changeActionAndTargetValue
     */
    public infix fun changeDetailedAction(detailedAction: String)

    /**
     * Sets or unsets the **section** link of the menu item to [section]. The effect of having one menu appear as a
     * section of another is exactly as it sounds: the items from section become a direct part of the menu that the
     * menu item is added to.
     * @param section A menu model, or *null*.
     * @see createSection
     */
    public infix fun changeSection(section: MenuModelBase?)

    /**
     * Sets or unsets the **submenu** link of the menu item to [subMenu]. If [subMenu] is non null then it is linked
     * to. If it is *null* then the link is unset. The effect of having one menu appear as a submenu of another is
     * exactly as it sounds.
     */
    public infix fun changeSubMenu(subMenu: MenuModelBase?)

    /**
     * Queries the named attribute on the menu item. If [expectedType] is specified, and the attribute does not have
     * this type then *null* is returned. Note that *null* is also returned if the attribute simply doesn't exist.
     * @param attrib The attribute name to query.
     * @param expectedType The expected type of the attribute.
     * @return The attribute value, or *null*.
     */
    public fun fetchAttributeValue(attrib: String, expectedType: VariantType): Variant?

    /**
     * Queries the named attribute on the menu item. If the attribute exists, and matches the [VariantType]
     * corresponding to [formatStr] then [formatStr] is used to deconstruct the value into the positional parameters,
     * and *true* is returned. However if the attribute doesn't exist, or it does exist but has the wrong type, then
     * the positional parameters are ignored and *false* is returned.
     * @param attrib The attribute name to query.
     * @param formatStr A [Variant] format String.
     * @return A value of *true* if the named attribute was found with the expected type.
     */
    public fun fetchAttribute(attrib: String, formatStr: String): Boolean

    /**
     * Queries the named link on the menu item.
     * @param link The name of the link to query.
     * @return The link, or *null*.
     */
    public fun fetchLink(link: String): MenuModelBase?

    /**
     * Sets or unsets an attribute on the menu item. The attribute to set or unset is specified by [attrib]. This can
     * be one of the standard attribute names `G_MENU_ATTRIBUTE_LABEL`, `G_MENU_ATTRIBUTE_ACTION`,
     * `G_MENU_ATTRIBUTE_TARGET`, or a custom attribute name. Attribute names are restricted to lowercase characters,
     * numbers and **-**. Furthermore, the names must begin with a lowercase character, must not end with a **-**, and
     * **MUST NOT** contain consecutive dashes. Can only contain lowercase ASCII characters, digits and **-**.
     *
     * If value is non null then it is used as the new value for the attribute. If value is *null* then the attribute
     * is unset. If the [value] is floating then it is consumed.
     * @param attrib The attribute to set.
     * @param value A [Variant] to use as the value, or *null*.
     * @see changeAttribute
     */
    public fun changeAttributeValue(attrib: String, value: Variant?)

    /**
     * Sets or unsets an attribute on the menu item. The attribute to set or unset is specified by [attrib]. This can
     * be one of the standard attribute names `G_MENU_ATTRIBUTE_LABEL`, `G_MENU_ATTRIBUTE_ACTION`,
     * `G_MENU_ATTRIBUTE_TARGET`, or a custom attribute name. Attribute names are restricted to lowercase characters,
     * numbers and **-**. Furthermore, the names must begin with a lowercase character, **MUST NOT** end with a **-**,
     * and **MUST NOT** contain consecutive dashes.
     *
     * If [formatStr] is not empty then the proper position parameters are collected to create a [Variant] instance to
     * use as the attribute value. If it is empty (**""**) then the positional parameters are ignored, and the named
     * attribute is unset.
     * @param attrib The attribute to set.
     * @param formatStr A [Variant] format String, or *""*.
     * @see changeAttributeValue
     */
    public fun changeAttribute(attrib: String, formatStr: String)

    /**
     * Creates a link from the menu item to model if non null, or unsets it. Links are used to establish a relationship
     * between a particular menu item, and another menu. For example `G_MENU_LINK_SUBMENU` is used to associate a
     * sub menu with a particular menu item, and `G_MENU_LINK_SECTION` is used to create a section. Other types of link
     * can be used, but there is no guarantee that clients will be able to make sense of them. Link types are
     * restricted to lowercase characters, numbers and **-**. Furthermore, the names must begin with a lowercase
     * character, must not end with a **-**, and **MUST NOT** contain consecutive dashes.
     */
    public fun changeLink(link: String, model: MenuModelBase?)
}
