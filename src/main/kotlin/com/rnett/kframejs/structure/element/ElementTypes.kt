package com.rnett.kframejs.structure.element

import org.w3c.dom.Text

interface IDisplayElement<U : W3Element> : ICanHaveElement<U>
interface IMetaElement<U : W3Element> : ICanHaveElement<U>

typealias AnyDisplayElement = IDisplayElement<*>
typealias AnyMetaElement = IMetaElement<*>

abstract class DisplayElement<E : DisplayElement<E, U>, U : W3Element>(tag: String, parent: CanHaveElement<*>) :
    Element<E, U>(tag, parent),
    IDisplayElement<U>

abstract class MetaElement<E : MetaElement<E, U>, U : W3Element>(tag: String, parent: CanHaveElement<*>) :
    Element<E, U>(tag, parent),
    IMetaElement<U>

class StandardDisplayElement<U : W3Element>(tag: String, parent: CanHaveElement<*>) :
    DisplayElement<StandardDisplayElement<U>, U>(tag, parent)
typealias StandardDisplayElementBuilder<U> = ElementBuilder<StandardDisplayElement<U>>

fun <U : W3Element> ICanHaveElement<*>.displayElement(tag: String) =
    StandardDisplayElement<U>(tag, this as CanHaveElement<*>)

class StandardMetaElement<U : W3Element>(tag: String, parent: CanHaveElement<*>) :
    MetaElement<StandardMetaElement<U>, U>(tag, parent)
typealias StandardMetaElementBuilder<U> = ElementBuilder<StandardMetaElement<U>>

fun <U : W3Element> ICanHaveElement<*>.metaElement(tag: String) = StandardMetaElement<U>(tag, this as CanHaveElement<*>)

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