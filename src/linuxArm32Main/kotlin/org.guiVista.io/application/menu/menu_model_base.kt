package org.guiVista.io.application.menu

import gio2.*
import glib2.TRUE
import glib2.gpointer
import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointer
import org.guiVista.core.ObjectBase
import org.guiVista.core.connectGSignal
import org.guiVista.core.dataType.Variant
import org.guiVista.core.dataType.VariantType
import org.guiVista.core.disconnectGSignal

private const val ITEMS_CHANGED_SIGNAL = "item-changed"

public actual interface MenuModelBase : ObjectBase {
    public val gMenuModelPtr: CPointer<GMenuModel>?

    /** Queries if model is mutable. */
    public val isMutable: Boolean
        get() = g_menu_model_is_mutable(gMenuModelPtr) == TRUE

    /** tTe number of items in model. */
    public val totalItems: Int
        get() = g_menu_model_get_n_items(gMenuModelPtr)

    /**
     * Queries the item at position [itemIndex] in model for the attribute specified by [attrib]. If [expectedType] is
     * non null then it specifies the expected type of the attribute. If it is *null* then any type will be accepted.
     * If the attribute exists, and matches [expectedType] (or if the expected type is unspecified) then the value is
     * returned. However if the attribute doesn't exist, or doesn't match the expected type then *null* is returned.
     * @param itemIndex The index of the item.
     * @param attrib The attribute to query.
     * @param expectedType The expected type of the attribute, or *null*.
     * @return The value of the attribute.
     */
    public fun fetchItemAttributeValue(itemIndex: Int, attrib: String, expectedType: VariantType?): Variant? {
        val ptr = g_menu_model_get_item_attribute_value(
            model = gMenuModelPtr,
            item_index = itemIndex,
            attribute = attrib,
            expected_type = expectedType?.gVariantTypePtr
        )
        return if (ptr != null) Variant.fromPointer(ptr) else null
    }

    /**
     * Queries item at position [itemIndex] in model for the attribute specified by [attrib]. If the attribute
     * exists, and matches the [VariantType] corresponding to [formatStr] then [formatStr] is used to deconstruct the
     * value into the positional parameters and *true* is returned. However if the attribute doesn't exist, or it
     * doesn't but has the wrong type, then the positional parameters are ignored and *false* is returned.
     *
     * This function is a mix of [fetchItemAttributeValue], and `g_variant_get()`, followed by a [Variant.close]. As
     * such [formatStr] must make a complete copy of the data (since the [Variant] may go away after the call to
     * [Variant.close]). In particular, no **&** characters are allowed in [formatStr].
     * @param itemIndex The index of the item.
     * @param attrib The attribute ot query.
     * @param formatStr A [Variant] format String.
     */
    public fun fetchItemAttribute(itemIndex: Int, attrib: String, formatStr: String): Boolean =
        g_menu_model_get_item_attribute(
            model = gMenuModelPtr,
            item_index = itemIndex,
            attribute = attrib,
            format_string = formatStr
        ) == TRUE

    /**
     * Queries the item at position [itemIndex] in model for the link specified by [link]. If the link exists then the
     * linked [MenuModelBase] is returned. If the link doesn't exist then *null* is returned.
     * @param itemIndex The index of the item.
     * @param link The link to the query.
     * @return The linked [MenuModelBase] or *null*.
     */
    public fun fetchItemLink(itemIndex: Int, link: String): MenuModelBase? {
        val ptr = g_menu_model_get_item_link(model = gMenuModelPtr, item_index = itemIndex, link = link)
        return if (ptr != null) MenuModel(ptr) else null
    }

    /**
     * Creates a [MenuAttributeIterator] to iterate over the attributes of the item at position [itemIndex] in the
     * model. You **MUST** free the iterator with [MenuAttributeIterator.close] when you are done.
     * @param itemIndex The index of the item.
     * @return A new [MenuAttributeIterator].
     */
    public fun iterateItemAttributes(itemIndex: Int): MenuAttributeIterator =
        MenuAttributeIterator(g_menu_model_iterate_item_attributes(gMenuModelPtr, itemIndex))

    /**
     * Creates a [MenuLinkIterator] to iterate over the links of the item at position [itemIndex] in model. You
     * **MUST** free the iterator with [MenuLinkIterator.close] when you are done.
     * @param itemIndex The index of the item.
     * @return A new [MenuLinkIterator].
     */
    public fun iterateItemLinks(itemIndex: Int): MenuLinkIterator =
        MenuLinkIterator(g_menu_model_iterate_item_links(gMenuModelPtr, itemIndex))

    override fun disconnectSignal(handlerId: ULong) {
        super.disconnectSignal(handlerId)
        disconnectGSignal(gMenuModelPtr, handlerId.toUInt())
    }

    /**
     * Connects the *items-changed* signal to a [slot] on a menu model. This signal occurs when a change has occurred
     * to the menu. The only changes that can occur to a menu is that items are removed or added. Items may not change
     * (except by being removed and added back in the same location). This signal is capable of describing both of
     * those changes (at the same time).
     *
     * The signal means that starting at the index position, removed items were removed and added items were added in
     * their place. If removed is zero then only items were added. If added is zero then only items were removed. As an
     * example if the menu contains items a, b, c, d (in that order), and the signal (2, 1, 3) occurs then the new
     * composition of the menu will be a, b, _, _, _, d (with each _ representing some new item).
     *
     * Signal handlers may query the model (particularly the added items), and expect to see the results of the
     * modification that is being reported. The signal is emitted after the modification.
     * @param slot The event handler for the signal.
     * @param userData User data to pass through to the [slot].
     * @return The handler ID.
     */
    public fun connectItemsChangedSignal(slot: CPointer<ItemsChangedSlot>, userData: gpointer): ULong =
        connectGSignal(obj = gMenuModelPtr, signal = ITEMS_CHANGED_SIGNAL, slot = slot, data = userData).toULong()
}

/**
 * The event handler for the *items-changed* signal. Arguments:
 * 1. model: CPointer<GMenuModel>
 * 2. pos: Int
 * 3. removed: Int
 * 4. added: Int
 * 5. userData: gpointer
 */
public typealias ItemsChangedSlot = CFunction<(
    model: CPointer<GMenuModel>,
    pos: Int,
    removed: Int,
    added: Int,
    userData: gpointer
) -> Unit>
