package com.rnett.kframejs.dom.classes

import com.rnett.kframejs.structure.element.CanHaveElement
import com.rnett.kframejs.structure.element.DisplayElement
import org.w3c.dom.HTMLIFrameElement
import org.w3c.dom.HTMLImageElement

class ImageElement(parent: CanHaveElement<*>) : DisplayElement<ImageElement, HTMLImageElement>("img", parent) {
    var src by attributes
    var width by attributes.byInt
    var height by attributes.byInt
    var altText by attributes.by("alt")
}

class IFrameElement(parent: CanHaveElement<*>) : DisplayElement<IFrameElement, HTMLIFrameElement>("iframe", parent) {
    var src by attributes
    var width by attributes.byInt
    var height by attributes.byInt
}