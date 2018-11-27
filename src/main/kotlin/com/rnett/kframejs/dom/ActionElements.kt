package com.rnett.kframejs.dom

import com.rnett.kframejs.structure.IDisplayElement
import com.rnett.kframejs.structure.KFrameElementDSL
import com.rnett.kframejs.structure.StandardDisplayElementBuilder
import com.rnett.kframejs.structure.displayElement

@KFrameElementDSL
inline fun IDisplayElement.button(klass: String = "", crossinline builder: StandardDisplayElementBuilder = {}) =
    displayElement("button").invoke {
        this.klass = klass
        builder()
    }