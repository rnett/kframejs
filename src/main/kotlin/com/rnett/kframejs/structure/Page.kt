package com.rnett.kframejs.structure

import org.w3c.dom.get
import kotlin.browser.document
import kotlin.browser.window
import org.w3c.dom.Element as W3Element

@KFrameElementDSL
inline fun page(crossinline builder: Page.() -> Unit): Page {
    val page = Page()
    if (window.onload != null)
        window.onload = {
            window.onload!!(it)
            page.builder()
        }
    else
        window.onload = {
            page.builder()
        }

    return page
}

class Page() {
    private val elements: MutableSet<AnyElement> = mutableSetOf()

    fun update() {
        elements.forEach { it.update() }
    }

    fun W3Element.kframeWrapper() = W3ElementWrapper(this, this@Page)

    val head get() = Head(this)
    val body get() = Body(this)

    internal fun addElement(element: AnyElement) {
        elements.add(element)
    }
}

class Body internal constructor(override val page: Page) : IDisplayElement {
    override val underlying: W3Element
        get() = document.getElementsByTagName("body")[0]!!
}

class Head internal constructor(override val page: Page) : IMetaElement {
    override val underlying: W3Element
        get() = document.getElementsByTagName("head")[0]!!

}