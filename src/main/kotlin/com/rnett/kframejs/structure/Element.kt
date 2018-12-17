package com.rnett.kframejs.structure

import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.reflect.KProperty0
import org.w3c.dom.Element as W3Element

@DslMarker
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.TYPE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY
)
annotation class KFrameElementDSL

typealias ElementBuilder<E> = E.() -> Unit

typealias BindingElementBuilder<E, B> = E.(B) -> Unit

typealias AnyElementBuilder = ElementBuilder<*>
typealias AnyElement = Element<*, *>

typealias W3Element = W3Element

interface ICanHaveElement<U>

abstract class CanHaveElement<U : W3Element>(val page: Page, val underlying: U) : ICanHaveElement<U> {
    protected abstract fun internalAdd(element: AnyElement)

    private var subs = 0
    private val subscribers = mutableMapOf<Int, (AnyElement) -> Unit>()

    fun subscribeOnAdd(sub: (AnyElement) -> Unit): Int {
        val s = subs
        subs++
        subscribers[s] = sub
        return s
    }

    fun removeAddSubscriber(sub: Int) {
        subscribers.remove(sub)
    }

    internal fun add(element: AnyElement) {
        subscribers.values.forEach { it(element) }
        page.addElement(element)

        internalAdd(element)
    }

    @BindingDSL
    infix fun <T> BindingCondition<T>.bindAll(builder: (T) -> Unit) = this@CanHaveElement.bindAll(this, builder)


    fun Router.view(main: String, data: Map<String, String> = emptyMap()) {
        goto(main, data)
        this@view::_currentPage.binding() bindAll {
            it?.build()
            it?.apply { this@CanHaveElement.page.title = subpage.title(data) }
        }
    }
}

open class W3ElementWrapper<U : W3Element>(underlying: U, page: Page) : CanHaveElement<U>(page, underlying) {
    override fun internalAdd(element: AnyElement) {}
}

typealias DynamicString = () -> String

abstract class Element<E : Element<E, U>, U : W3Element>(val tag: String, val rawParent: CanHaveElement<*>) :
    CanHaveElement<U>(
        rawParent.page, rawParent.underlying.appendChild(document.createElement(tag)) as U
) {

    init {
        rawParent.add(this)
    }

    override fun internalAdd(element: AnyElement) {
        _children.add(element)
    }

    val attributes = Attributes(this)
    val style = Style(this)

    var id by attributes
    var classes = Classes(this)
    var klass by attributes.by("class")

    private val _children = mutableListOf<AnyElement>()
    val children get() = _children.toList()

    inline fun <reified P : Element<P, Pu>, Pu : W3Element> parent(): P {
        if (rawParent is P)
            return rawParent
        else throw ClassCastException("Node is at the top of the tree of KFrame nodes.  Use parent property instead")
    }

    val parent: AnyElement
        get() {
            if (rawParent is AnyElement)
                return rawParent
            else throw ClassCastException("Node is at the top of the tree of KFrame nodes.  Use parent property instead")
        }


    fun remove() {
        underlying.remove()
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

    @KFrameElementDSL
    operator fun ElementBuilder<in E>.unaryPlus() = this@Element.invoke(this)

    @KFrameElementDSL
    operator fun String.unaryPlus() = addText(this)

    @KFrameElementDSL
    fun addText(text: String): TextElement {
        val t = underlying.ownerDocument!!.createTextNode(text)
        underlying.appendChild(t)
        return TextElement(t)
    }

    @KFrameElementDSL
    fun text(text: DynamicString): TextElement {
        val t = addText(text())
        page.watch(binding { text() }) {
            t.value = text()
        }
        return t
    }

    @KFrameElementDSL
    operator fun DynamicString.unaryPlus() = text(this)


    @KFrameElementDSL
    operator fun View<E>.unaryPlus() = this@Element{
        FunctionBindingCondition { this@unaryPlus } bindAll {
            this@unaryPlus.apply {
                display()
            }
        }
    }

    @KFrameElementDSL
    operator fun View<E>.unaryMinus() = this@Element{
        this@unaryMinus.apply {
            display()
        }
    }

    @KFrameElementDSL
    fun boundView(prop: KProperty0<View<E>>) = this{
        prop.binding().bindAll {
            it.apply {
                display()
            }
        }
    }

    @KFrameElementDSL
    operator fun KProperty0<View<E>>.unaryPlus() = boundView(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is W3Element) return underlying == other

        if (other !is Element<*, *>) return false

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

    val currentListeners = mutableSetOf<String>()

    //TODO getting muptiple page updates when using multiple `on`s
    fun <H : Event> on(
        event: String,
        useCapture: Boolean = false,
        handler: (H) -> Unit
    ): EventHandler {
        val actualHandler: (Event) -> Unit =
            if (event in currentListeners) {
                { handler(it as H) }
            } else {
                currentListeners.add(event);
                { handler(it as H); page.update() }
            }

        underlying.addEventListener(event, actualHandler, useCapture)
        return EventHandler(this, event, useCapture, actualHandler)
    }

    inline fun String.on(useCapture: Boolean = false, noinline handler: (Event) -> Unit) = on(this, useCapture, handler)
    inline operator fun String.invoke(useCapture: Boolean = false, noinline handler: (Event) -> Unit) =
        on(this, useCapture, handler)

    private var myBinding: Binding<*>? = null

    fun update() {
        myBinding?.checkAndUpdate()
        onUpdate()
    }

    protected open fun onUpdate() {}

    @BindingDSL
    fun <T> bindNow(cond: BindingCondition<T>): E {
        if (myBinding != null)
            throw BindingException()
        else
            myBinding = Binding(this, cond, StateBindingValue(innerHTML, attributes.toList().toMap()))

        return this as E
    }

    @BindingDSL
    fun bindNow(cond: () -> Any?) = bindNow(FunctionBindingCondition(cond))

    @BindingDSL
    fun bindNow(cond: () -> Boolean) = bindNow(BooleanBindingCondition(cond))

    @BindingDSL
    fun <T> bindNow(cond: KProperty0<T>) = bindNow(PropertyBindingCondition(cond))

    @BindingDSL
    fun <T> bound(cond: BindingCondition<T>, builders: List<BindingElementBuilder<E, T>>): E {
        if (myBinding != null)
            throw BindingException()
        else
            myBinding = Binding(this, cond, BuildersBindingValue(builders))

        return this as E
    }

    @BindingDSL
    fun <T> bound(cond: BindingCondition<T>, builder: BindingElementBuilder<E, T>) = bound(cond, listOf(builder))

    @BindingDSL
    operator fun <T> invoke(cond: BindingCondition<T>, builder: BindingElementBuilder<E, T>) = bound(cond, builder)

    //TODO update the rest of the binding forms to work like this
    @BindingDSL
    fun <T> bound(cond: KProperty0<T>, builder: BindingElementBuilder<E, T>) = bound(cond.binding(), listOf(builder))

    @BindingDSL
    operator fun <T> invoke(cond: KProperty0<T>, builder: BindingElementBuilder<E, T>) = bound(cond, builder)
}