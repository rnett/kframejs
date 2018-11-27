package com.rnett.kframejs.dom

import com.rnett.kframejs.structure.IDisplayElement
import com.rnett.kframejs.structure.KFrameElementDSL
import com.rnett.kframejs.structure.StandardDisplayElementBuilder
import com.rnett.kframejs.structure.displayElement

@KFrameElementDSL
inline fun IDisplayElement.p(
    text: String = "",
    klass: String = "",
    crossinline builder: StandardDisplayElementBuilder = {}
) =
    displayElement("p").invoke {
        this.klass = klass
        if (text != "") {
            +text
        }
        builder()
    }

@KFrameElementDSL
inline fun IDisplayElement.br() =
    displayElement("br")

@KFrameElementDSL
inline fun IDisplayElement.br(lines: Int) {
    for (i in 0..lines)
        br()
}


val IDisplayElement.br get() = br()