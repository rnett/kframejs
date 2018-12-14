package com.rnett.kframejs.dom

import com.rnett.kframejs.structure.*
import org.w3c.dom.HTMLBRElement
import org.w3c.dom.HTMLParagraphElement

@KFrameElementDSL
inline fun <T> T.p(
    text: String = "",
    klass: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLParagraphElement> = {}
)
        where T : AnyDisplayElement, T : CanHaveElement<*> =
    StandardDisplayElement<HTMLParagraphElement>("p", this).invoke {
        this.klass = klass
        if (text != "") {
            +text
        }
        builder()
    }

@KFrameElementDSL
inline fun <T> T.br() where T : AnyDisplayElement, T : CanHaveElement<*> =
    StandardDisplayElement<HTMLBRElement>("br", this)

@KFrameElementDSL
inline fun <T> T.br(lines: Int) where T : AnyDisplayElement, T : CanHaveElement<*> {
    for (i in 0..lines)
        br()
}

@KFrameElementDSL
val <T> T.br where T : AnyDisplayElement, T : CanHaveElement<*>
    get() = br()