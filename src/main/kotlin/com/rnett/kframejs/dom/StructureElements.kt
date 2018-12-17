package com.rnett.kframejs.dom

import com.rnett.kframejs.structure.element.*
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
inline fun <T> T.divs(
    vararg classes: String,
    crossinline builder: StandardDisplayElementBuilder<HTMLDivElement> = {}
): StandardDisplayElement<HTMLDivElement> where T : AnyDisplayElement, T : CanHaveElement<*> {

    if (classes.isEmpty())
        return div()

    var outer: CanHaveElement<*> = this
    for (klass in classes) {
        outer = div(klass)
    }
    @Suppress("UNCHECKED_CAST")
    return (outer as? StandardDisplayElement<HTMLDivElement>)?.invoke { builder() }!!
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


@KFrameElementDSL
inline fun <T> T.spans(
    vararg classes: String,
    crossinline builder: StandardDisplayElementBuilder<HTMLSpanElement> = {}
): StandardDisplayElement<HTMLSpanElement> where T : AnyDisplayElement, T : CanHaveElement<*> {

    if (classes.isEmpty())
        return span()

    var outer: CanHaveElement<*> = this
    for (klass in classes) {
        outer = span(klass)
    }
    @Suppress("UNCHECKED_CAST")
    return (outer as? StandardDisplayElement<HTMLSpanElement>)?.invoke { builder() }!!
}