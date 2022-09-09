package com.tryformation.localization

// ?<= is causing issues with Safari
//private val re = "(?<=[a-z0-9])[A-Z]".toRegex()
// simpler replacement; seems to work
private val re = "(.+?)([A-Z])".toRegex()

private fun String.camelCase2SnakeCase(): String {
    return re.replace(this) { m -> "${m.groups[1]?.value}_${m.groups[2]?.value}" }.lowercase()
}

interface Translatable {
    val name: String
    val prefix: String
    val messageId: String
        get() = "$prefix-" + (name.let { s ->
            // only apply conversion if it has lowercase letters
            if (s.firstOrNull { c -> c in 'a'..'z' } != null) {
                s.camelCase2SnakeCase()
            } else {
                s
            }
        }).replace('_', '-').lowercase()
}