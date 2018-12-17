package com.rnett.kframejs.dom.classes

import com.rnett.kframejs.structure.CanHaveElement
import com.rnett.kframejs.structure.Element
import org.w3c.dom.HTMLElement

class ListElement<U : HTMLElement>(tag: String, parent: CanHaveElement<*>) : Element<ListElement<U>, U>(tag, parent)

