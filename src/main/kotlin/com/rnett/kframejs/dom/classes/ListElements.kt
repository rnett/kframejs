package com.rnett.kframejs.dom.classes

import com.rnett.kframejs.structure.element.CanHaveElement
import com.rnett.kframejs.structure.element.Element
import org.w3c.dom.HTMLElement

class ListElement<U : HTMLElement>(tag: String, parent: CanHaveElement<*>) : Element<ListElement<U>, U>(tag, parent)

