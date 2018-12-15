package com.rnett.kframejs.dom

import com.rnett.kframejs.structure.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLSpanElement

@KFrameElementDSL
inline fun <T> T.div(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLDivElement> = {}
)
        where T : AnyDisplayElement, T : CanHaveElement<*> =
    StandardDisplayElement<HTMLDivElement>("div", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }

@KFrameElementDSL
inline fun <T> T.span(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLSpanElement> = {}
)
        where T : AnyDisplayElement, T : CanHaveElement<*> =
    StandardDisplayElement<HTMLSpanElement>("span", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }