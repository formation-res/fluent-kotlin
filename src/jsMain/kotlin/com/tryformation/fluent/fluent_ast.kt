@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
@file:JsModule("@fluent/bundle")
@file:JsNonModule

package com.tryformation.fluent

import kotlin.js.Json

external interface Message {
    var id: String
    var value: dynamic /* String? | ComplexPattern? */
        get() = definedExternally
        set(value) = definedExternally
    var attributes: Json // <String, dynamic /* String | ComplexPattern */>
}

external interface Term {
    var id: String
    var value: dynamic /* String | ComplexPattern */
        get() = definedExternally
        set(value) = definedExternally
    var attributes: Json // Record<String, dynamic /* String | ComplexPattern */>
}

external interface SelectExpression {
    var type: String /* "select" */
    var selector: dynamic /* SelectExpression | VariableReference | TermReference | MessageReference | FunctionReference | StringLiteral | NumberLiteral */
        get() = definedExternally
        set(value) = definedExternally
    var variants: Array<Variant>
    var star: Int
}

external interface VariableReference {
    var type: String /* "var" */
    var name: String
}

external interface TermReference {
    var type: String /* "term" */
    var name: String
    var attr: String?
    var args: Array<dynamic /* SelectExpression | VariableReference | TermReference | MessageReference | FunctionReference | StringLiteral | NumberLiteral | NamedArgument */>
}

external interface MessageReference {
    var type: String /* "mesg" */
    var name: String
    var attr: String?
}

external interface FunctionReference {
    var type: String /* "func" */
    var name: String
    var args: Array<dynamic /* SelectExpression | VariableReference | TermReference | MessageReference | FunctionReference | StringLiteral | NumberLiteral | NamedArgument */>
}

external interface Variant {
    var key: dynamic /* StringLiteral | NumberLiteral */
        get() = definedExternally
        set(value) = definedExternally
    var value: dynamic /* String | ComplexPattern */
        get() = definedExternally
        set(value) = definedExternally
}

external interface NamedArgument {
    var type: String /* "narg" */
    var name: String
    var value: dynamic /* StringLiteral | NumberLiteral */
        get() = definedExternally
        set(value) = definedExternally
}

external interface StringLiteral {
    var type: String /* "str" */
    var value: String
}

external interface NumberLiteral {
    var type: String /* "num" */
    var value: Double
    var precision: Double
}