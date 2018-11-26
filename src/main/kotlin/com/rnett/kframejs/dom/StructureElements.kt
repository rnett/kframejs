package com.rnett.kframejs.dom

import com.rnett.kframejs.structure.IDisplayElement
import com.rnett.kframejs.structure.KFrameElementDSL
import com.rnett.kframejs.structure.StandardDisplayElementBuilder
import com.rnett.kframejs.structure.displayElement

@KFrameElementDSL
inline fun IDisplayElement.div(klass: String = "", crossinline builder: StandardDisplayElementBuilder = {}) =
    displayElement("div").invoke {
        this.klass = klass
        builder()
    }

@KFrameElementDSL
inline fun IDisplayElement.span(klass: String = "", crossinline builder: StandardDisplayElementBuilder = {}) =
    displayElement("span").invoke {
        this.klass = klass
        builder()
    }