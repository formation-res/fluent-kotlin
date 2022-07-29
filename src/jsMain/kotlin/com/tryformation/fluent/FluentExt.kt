@file:Suppress("UnsafeCastFromDynamic", "UNUSED_PARAMETER", "unused")

package com.tryformation.fluent

import com.tryformation.localization.Translatable
import kotlin.js.Json
import kotlin.js.json

typealias ComplexPattern = Array<dynamic>
/* String | SelectExpression | VariableReference | TermReference | MessageReference | FunctionReference | StringLiteral | NumberLiteral */

typealias BundleSequence = Array<FluentBundle>

fun FluentBundle.translate(id: String, args: Json? = null) : String {
    check(hasMessage(id)) { "message $id not found in bundle" }
    val message = getMessage(id) ?: run {
        console.error("message", id, "not found in bundle", locales)
        object : Message {
            override var id: String
                get() = id
                set(value) {}
            override var attributes: Json
                get() = json()
                set(value) {}

        }
    }
    return when (val messageValue = message.value) {
        is String -> messageValue
        is ComplexPattern -> {
            formatPattern(messageValue, args)
        }
        else -> {
            console.error("failed to translate unknown type", id, message)
            "ERROR translating $id"
        }
    }
}

fun BundleSequence.translate(id: String, args: Json? = null) : String {
    val bundle = findBundle(id)
    if(bundle == null) {
        console.error("$id not found in any bundle")
        return id
    }
    check(bundle.hasMessage(id)) { "message $id not found in bundle" }
    val message = bundle.getMessage(id) ?: run {
        console.error("message", id, "not found in bundle", bundle.locales)
        return  "ERROR translating $id"
    }
    return when (val messageValue = message.value) {
        is String -> messageValue
        is ComplexPattern -> {
            try {
                bundle.formatPattern(messageValue, args)
            } catch (e: Throwable) {
                console.error("error processing translation for $id: ${e.message}")
                id
            }
        }
        else -> {
            console.error("failed to translate unknown type", id, message)
            "ERROR translating $id"
        }
    }
}

fun BundleSequence.getMessage(id: String) : Message? {
    val bundle = findBundle(id) ?: return null
    return bundle.getMessage(id)
}

fun BundleSequence.translate(translatable: Translatable, args: Json? = null) : String {
    val id = translatable.messageId
    return translate(id, args)
}

fun BundleSequence.findBundle(id: String): FluentBundle? {
    return mapBundleSync(this, id)
}
