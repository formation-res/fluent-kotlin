package com.tryformation.localization

// ?<= is causing issues with Safari
//private val re = "(?<=[a-z0-9])[A-Z]".toRegex()
// simpler replacement; seems to work
private val re = "(.+?)([A-Z])".toRegex()

private fun String.camelCase2SnakeCase(): String {
    return re.replace(this) { m -> "${m.groups[1]?.value}_${m.groups[2]?.value}" }.lowercase()
}

fun String.humanReadable(): String {
    return this.replace(Regex("([a-z])([A-Z])"), "$1 $2") // Handle camelCase
        .replace(Regex("[-_]"), " ") // Replace - or _ with space
        .lowercase() // Convert to lowercase
        .replaceFirstChar { it.uppercase() } // Capitalize the first letter
}

interface Translatable {
    /** Name of the translatable. Note, if you are using enums, this would be the enum value's name. */
    val name: String
    /** Prefix used in your fluent translation files */
    val prefix: String
    /**
     * Prefix with your enum value. Note, the default
     * implementation takes care of lower casing and converting camel case to snake case for you.
     */
    val messageId: String
        get() = "$prefix-" + (name.let { s ->
            // only apply conversion if it has lowercase letters
            if (s.firstOrNull { c -> c in 'a'..'z' } != null) {
                s.camelCase2SnakeCase()
            } else {
                s
            }
        }).replace('_', '-').lowercase()

    /**
     * Falback if no translation is found. The default simply
     */
    val defaultTranslation: String
        get() = name.humanReadable()
}

/**
 * Useful on enums or other translatable constants. Allows you to have a sane default translation in code and fall back
 * to fluent files otherwise.
 *
 * Note. Any translation file for the locale will still override the default in code.
 */
interface TranslatableWithDefault : Translatable {

    val localizedDefaultTranslation: String?
    val locale: String get() = "en_GB"

    override val prefix: String
        get() = this::class.simpleName?.camelCase2SnakeCase()
            ?: error("no class name, don't use anonymous objects with TranslatableWithDefault")

    override val defaultTranslation: String
        get() = localizedDefaultTranslation ?: super.defaultTranslation
}
