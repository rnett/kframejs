package com.rnett.kframejs.structure.addons

import com.rnett.kframejs.structure.element.AnyElement

interface HasClass {
    val klass: String
}

fun AnyElement.addClass(klass: String) {
    classes.add(klass)
}

fun AnyElement.addClass(klass: HasClass) {
    classes.add(klass.klass)
}