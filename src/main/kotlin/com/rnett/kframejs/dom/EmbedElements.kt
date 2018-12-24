package com.rnett.kframejs.dom

import com.rnett.kframejs.dom.classes.ImageElement
import com.rnett.kframejs.structure.element.CanHaveElement
import com.rnett.kframejs.structure.element.ElementBuilder
import com.rnett.kframejs.structure.element.IDisplayElement
import com.rnett.kframejs.structure.element.KFrameElementDSL

@KFrameElementDSL
inline fun <ParentType> ParentType.img(
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<ImageElement> = {}
)
        where ParentType : IDisplayElement<*>, ParentType : CanHaveElement<*> =
    ImageElement(this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }

@KFrameElementDSL
inline fun <ParentType> ParentType.img(
    src: String,
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<ImageElement> = {}
)
        where ParentType : IDisplayElement<*>, ParentType : CanHaveElement<*> =
    img {
        this.src = src
        builder()
    }

@KFrameElementDSL
inline fun <ParentType> ParentType.img(
    src: String,
    width: Int,
    height: Int,
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<ImageElement> = {}
)
        where ParentType : IDisplayElement<*>, ParentType : CanHaveElement<*> =
    img(src) {
        this.width = width
        this.height = height
        builder()
    }
