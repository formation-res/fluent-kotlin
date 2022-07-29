@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
@file:JsModule("@fluent/sequence")
@file:JsNonModule

package com.tryformation.fluent

external fun mapBundleSync(bundles: Array<FluentBundle>, ids: String): FluentBundle?

external fun mapBundleSync(bundles: Array<FluentBundle>, ids: Array<String>): Array<FluentBundle?>
