package com.rnett.kframejs.dom.classes

import com.rnett.kframejs.structure.element.CanHaveElement
import com.rnett.kframejs.structure.element.DisplayElement
import org.w3c.dom.HTMLAnchorElement

class A(parent: CanHaveElement<*>) : DisplayElement<A, HTMLAnchorElement>("a", parent) {
    var href by attributes
}