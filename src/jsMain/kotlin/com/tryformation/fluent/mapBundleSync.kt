@file:JsModule("@fluent/sequence")
@file:JsNonModule

package com.tryformation.fluent

external fun mapBundleSync(bundles: Array<FluentBundle>, ids: String): FluentBundle?

external fun mapBundleSync(bundles: Array<FluentBundle>, ids: Array<String>): Array<FluentBundle?>
