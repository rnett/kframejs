package com.rnett.kframejs.dom

import com.rnett.kframejs.structure.*
import org.w3c.dom.HTMLButtonElement

@KFrameElementDSL
inline fun <T> T.button(klass: String = "", crossinline builder: StandardDisplayElementBuilder<HTMLButtonElement> = {})
        where T : AnyDisplayElement, T : CanHaveElement<*> =
    StandardDisplayElement<HTMLButtonElement>("button", this).invoke {
        this.klass = klass
        builder()
    }