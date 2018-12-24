package com.rnett.kframejs.dom

import com.rnett.kframejs.dom.classes.A
import com.rnett.kframejs.structure.element.*
import org.w3c.dom.HTMLBRElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.HTMLParagraphElement

@KFrameElementDSL
inline fun <T> T.p(
    text: String = "",
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLParagraphElement> = {}
)
        where T : AnyDisplayElement, T : CanHaveElement<*> =
    StandardDisplayElement<HTMLParagraphElement>("p", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        if (text != "") {
            +text
        }
        builder()
    }

@KFrameElementDSL
inline fun <ParentType> ParentType.a(
    href: String = "",
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<A> = {}
)
        where ParentType : AnyDisplayElement, ParentType : CanHaveElement<*> =
    A(this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        if (href != "")
            this.href = href
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

@KFrameElementDSL
inline fun <ParentType> ParentType.label(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLLabelElement> = {}
)
        where ParentType : AnyDisplayElement, ParentType : CanHaveElement<*> =
    StandardDisplayElement<HTMLLabelElement>("label", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }