@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
@file:JsModule("@fluent/bundle")
@file:JsNonModule

package com.tryformation.fluent

import kotlin.js.Json

//@JsName("FluentResource")
open external class FluentResource(source: String) {
    open var body: Array<dynamic /* Message | Term */>
}

open external class FluentBundle {
    open var locales: Array<String>
    constructor(locales: String)
    constructor(locales: Array<String>)
    open fun hasMessage(id: String): Boolean
    open fun getMessage(id: String): Message?
    open fun addResource(res: FluentResource): Array<Error>
    open fun formatPattern(pattern: dynamic /* String | ComplexPattern */, args: Json /* FluentType<Any> | String | Number | Date */): String
    open fun formatPattern(pattern: ComplexPattern, args: Json? /* FluentType<Any> | String | Number | Date */): String
}


external class FluentDateTime(
    date: kotlin.js.Date,
    opts: Json?
) {
    val opts: Json?;
}
