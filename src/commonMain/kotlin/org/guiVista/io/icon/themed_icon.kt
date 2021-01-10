package org.guiVista.io.icon

public expect class ThemedIcon : IconBase {
    /**
     * Prepend a name to the list of icons from within icon. Note that doing so invalidates the hash computed by prior
     * calls to [Icon.hashCode].
     * @param iconName Name of icon to prepend to list of icons from within icon.
     */
    public infix fun prependName(iconName: String)

    /**
     * Append a name to the list of icons from within icon. Note that doing so invalidates the hash computed by prior
     * calls to [Icon.hashCode].
     * @param iconName Name of icon to append to list of icons from within icon.
     */
    public infix fun appendName(iconName: String)

    /**
     * Gets the names of icons from within icon.
     * @return An Array of icon names.
     */
    public fun fetchNames(): Array<String>

    public companion object {
        /**
         * Creates a new themed icon for [iconName].
         * @param iconName The icon name.
         * @return A new [ThemedIcon].
         */
        public fun create(iconName: String): ThemedIcon

        /**
         * Creates a new themed icon for [iconNames].
         * @param iconNames One or more icon names.
         * @return A new [ThemedIcon].
         */
        public fun fromNames(vararg iconNames: String): ThemedIcon

        /**
         * Creates a new themed icon for [iconName], and all the names that can be created by shortening [iconName] at
         * **-** characters.
         * @param iconName The icon name.
         * @return A new [ThemedIcon].
         */
        public fun createWithDefaultFallbacks(iconName: String): ThemedIcon
    }
}
