package com.rnett.kframejs.dom

import com.rnett.kframejs.dom.classes.ListElement
import com.rnett.kframejs.structure.element.*
import org.w3c.dom.HTMLLIElement
import org.w3c.dom.HTMLOListElement
import org.w3c.dom.HTMLUListElement

@KFrameElementDSL
inline fun <ParentType> ParentType.ul(
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<ListElement<HTMLUListElement>> = {}
)
        where ParentType : AnyDisplayElement, ParentType : CanHaveElement<*> =
    ListElement<HTMLUListElement>("ul", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }


@KFrameElementDSL
inline fun <ParentType> ParentType.ol(
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<ListElement<HTMLOListElement>> = {}
)
        where ParentType : AnyDisplayElement, ParentType : CanHaveElement<*> =
    ListElement<HTMLOListElement>("ul", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }


@KFrameElementDSL
inline fun ListElement<*>.li(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLLIElement> = {}
) =
    StandardDisplayElement<HTMLLIElement>("li", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }
