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

//TODO make head and body actual elements

class Page() {
    private val _elements: MutableSet<AnyElement> = mutableSetOf()
    private val watches: MutableSet<Watch> = mutableSetOf()

    val elements get() = _elements.toSet()

    fun update(sender: AnyElement? = null, event: String? = null) {
        _elements.forEach { it.update() }
        watches.forEach { it.doUpdate() }
    }

    fun W3Element.kframeWrapper() = W3ElementWrapper(this, this@Page)

    val head get() = Head(this)
    val body get() = Body(this)

    internal fun addElement(element: AnyElement) = _elements.add(element)
    internal fun addWatch(watch: Watch) = watches.add(watch)

    @BindingDSL
    infix fun BindingCondition.watch(update: () -> Unit) = this@Page.watch(this, update)
}

class Body internal constructor(page: Page) : W3ElementWrapper(document.getElementsByTagName("body")[0]!!, page),
    IDisplayElement {
    override fun internalAdd(element: AnyElement) {}
}

class Head internal constructor(page: Page) : W3ElementWrapper(document.getElementsByTagName("head")[0]!!, page),
    IMetaElement {
    override fun internalAdd(element: AnyElement) {}
}