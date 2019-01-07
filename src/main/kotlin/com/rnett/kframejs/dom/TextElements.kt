package com.rnett.kframejs.dom

import com.rnett.kframejs.dom.classes.A
import com.rnett.kframejs.structure.element.*
import org.w3c.dom.*

@KFrameElementDSL
inline fun <ParentType> ParentType.h(
    size: Int,
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLHeadingElement> = {}
)
        where ParentType : AnyDisplayElement, ParentType : CanHaveElement<*> =
    StandardDisplayElement<HTMLHeadingElement>("h$size", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }

@KFrameElementDSL
inline fun <ParentType> ParentType.h1(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLHeadingElement> = {}
)
        where ParentType : AnyDisplayElement, ParentType : CanHaveElement<*> =
    h(1, klass, id, builder)

@KFrameElementDSL
inline fun <ParentType> ParentType.h2(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLHeadingElement> = {}
)
        where ParentType : AnyDisplayElement, ParentType : CanHaveElement<*> =
    h(2, klass, id, builder)

@KFrameElementDSL
inline fun <ParentType> ParentType.h3(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLHeadingElement> = {}
)
        where ParentType : AnyDisplayElement, ParentType : CanHaveElement<*> =
    h(3, klass, id, builder)

@KFrameElementDSL
inline fun <ParentType> ParentType.h4(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLHeadingElement> = {}
)
        where ParentType : AnyDisplayElement, ParentType : CanHaveElement<*> =
    h(4, klass, id, builder)

@KFrameElementDSL
inline fun <ParentType> ParentType.h5(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLHeadingElement> = {}
)
        where ParentType : AnyDisplayElement, ParentType : CanHaveElement<*> =
    h(5, klass, id, builder)

@KFrameElementDSL
inline fun <ParentType> ParentType.h6(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLHeadingElement> = {}
)
        where ParentType : AnyDisplayElement, ParentType : CanHaveElement<*> =
    h(6, klass, id, builder)


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

@KFrameElementDSL
inline fun <ParentType> ParentType.textarea(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLTextAreaElement> = {}
)
        where ParentType : AnyDisplayElement, ParentType : CanHaveElement<*> =
    StandardDisplayElement<HTMLTextAreaElement>("textarea", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }