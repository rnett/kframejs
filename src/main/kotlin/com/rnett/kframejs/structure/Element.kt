package com.rnett.kframejs.structure

import org.w3c.dom.Text
import org.w3c.dom.events.Event
import org.w3c.dom.get
import kotlin.browser.document
import kotlin.reflect.KProperty
import org.w3c.dom.Element as W3Element

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class KFrameElementDSL

typealias ElementBuilder<E> = E.() -> Unit

typealias AnyElementBuilder = ElementBuilder<*>
typealias AnyElement = Element<*>

interface ICanHaveElement {
    val underlying: W3Element
}

class W3ElementWrapper(override val underlying: W3Element) : ICanHaveElement

fun W3Element.kframeWrapper() = W3ElementWrapper(this)

@KFrameElementDSL
object Body : IDisplayElement {
    override val underlying: W3Element
        get() = document.getElementsByTagName("body")[0]!!
}

@KFrameElementDSL
object Head : IMetaElement {
    override val underlying: W3Element
        get() = document.getElementsByTagName("head")[0]!!
}

abstract class Element<E : Element<E>>(val tag: String, parent: ICanHaveElement) : ICanHaveElement {

    constructor(tag: String, parent: AnyElement) : this(tag, parent.underlying.kframeWrapper())

    override val underlying: W3Element = parent.underlying.appendChild(document.createElement(tag)) as W3Element

    init {
        if (parent is AnyElement) {
            parent._children.add(this)
        }
    }

    val attributes = Attributes(this)
    val style = Style(this)

    var id by attributes
    var classes = Classes(this)
    var klass by attributes.by("class")
    var value by attributes
    var type by attributes
    var title by attributes

    private val _children = mutableListOf<AnyElement>()
    val children = _children.toList()

    val rawParent = parent

    inline fun <reified P : Element<P>> parent(): P {
        if (rawParent is P)
            return rawParent
        else throw ClassCastException("Node is at the top of the tree of KFrame nodes.  Use rawParent instead")
    }

    val on = Events(this as E)

    var innerHTML: String
        get() = underlying.innerHTML
        set(v) {
            underlying.innerHTML = v
        }

    operator fun invoke(builder: ElementBuilder<in E>): E {
        builder(this as E)
        return this
    }

    operator fun invoke(vararg classes: String): E {

        if (classes.size == 1 && classes[0].startsWith("#"))
            this.id == classes[0].drop(1)
        else {
            this.classes.clear()
            this.classes.addAll(classes)
        }

        return this as E
    }

    operator fun ElementBuilder<in E>.unaryPlus() = this@Element.invoke(this)

    operator fun String.unaryPlus(): TextElement {
        val t = underlying.ownerDocument!!.createTextNode(this)
        underlying.appendChild(t)
        return TextElement(t)
    }

    fun addText(text: String) = text.unaryPlus()


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is W3Element) return underlying == other

        if (other !is Element<*>) return false

        if (underlying != other.underlying) return false

        return true
    }

    override fun hashCode(): Int {
        return underlying.hashCode()
    }

    inline fun on(event: String, useCapture: Boolean = false, noinline handler: (Event) -> Unit): EventHandler {
        underlying.addEventListener(event, handler, useCapture)
        return EventHandler(this, event, useCapture, handler)
    }

    inline fun <H : Event> on(
        event: String,
        useCapture: Boolean = false,
        crossinline handler: (H) -> Unit
    ): EventHandler {
        val actualHandler: (Event) -> Unit = { handler(it as H) }
        underlying.addEventListener(event, actualHandler, useCapture)
        return EventHandler(this, event, useCapture, actualHandler)
    }

    inline fun String.on(useCapture: Boolean = false, noinline handler: (Event) -> Unit) = on(this, useCapture, handler)


    fun bound(vararg props: KProperty<*>, builder: ElementBuilder<in E>) {
        //TODO need a way of intercepting events.  Can add to on event lambda.  Then need a way of getting all listeners (page class probably)
    }

}

abstract class DisplayElement<E : DisplayElement<E>>(tag: String, parent: ICanHaveElement) : Element<E>(tag, parent),
    IDisplayElement

abstract class MetaElement<E : MetaElement<E>>(tag: String, parent: ICanHaveElement) : Element<E>(tag, parent),
    IDisplayElement

class StandardDisplayElement(tag: String, parent: ICanHaveElement) : DisplayElement<StandardDisplayElement>(tag, parent)
typealias StandardDisplayElementBuilder = ElementBuilder<StandardDisplayElement>

fun ICanHaveElement.displayElement(tag: String) = StandardDisplayElement(tag, this)

class StandardMetaElement(tag: String, parent: ICanHaveElement) : MetaElement<StandardMetaElement>(tag, parent)
typealias StandardMetaElementBuilder = ElementBuilder<StandardMetaElement>

fun ICanHaveElement.metaElement(tag: String) = StandardMetaElement(tag, this)


class TextElement(private var _underlying: Text) {
    var value: String
        get() = _underlying.wholeText
        set(v) {
            val new = Text(v)
            _underlying.replaceWith(new)
            _underlying = new
        }

    val underlying get() = _underlying
}