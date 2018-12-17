package com.rnett.kframejs.dom

import com.rnett.kframejs.structure.element.*
import org.w3c.dom.HTMLButtonElement

@KFrameElementDSL
inline fun <T> T.button(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLButtonElement> = {}
)
        where T : AnyDisplayElement, T : CanHaveElement<*> =
    StandardDisplayElement<HTMLButtonElement>("button", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }