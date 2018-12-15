package com.rnett.kframejs.structure

import kotlin.reflect.KProperty0

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class BindingDSL

typealias AnyBinding<T> = () -> T
typealias BoolBinding = () -> Boolean

sealed class BindingCondition<T> {
    abstract fun needsUpdate(): Boolean
    abstract fun update()
    abstract fun value(): T
}

data class FunctionBindingCondition<T>(val func: AnyBinding<T>) : BindingCondition<T>() {

    private var lastValue: Any? = value()

    override fun needsUpdate(): Boolean = value() != lastValue

    override fun update() {
        lastValue = value()
    }

    override fun value() = func()

    class Builder {

        internal fun values() = values.toList()

        private val values: MutableList<Any?> = mutableListOf()

        @BindingDSL
        fun bind(value: Any?) {
            values.add(value)
        }
    }
}

data class PropertyBindingCondition<T>(val prop: KProperty0<T>) : BindingCondition<T>() {

    private var lastValue: Any? = value()

    override fun needsUpdate(): Boolean = value() != lastValue

    override fun update() {
        lastValue = value()
    }

    override fun value() = prop.get()
}


@BindingDSL
fun <R> binding(builder: FunctionBindingCondition.Builder.() -> R) = FunctionBindingCondition {
    val b = FunctionBindingCondition.Builder()
    val result = builder(b)
    b.values().plus(result)
}

data class BooleanBindingCondition(val func: BoolBinding) : BindingCondition<Boolean>() {
    override fun needsUpdate() = value()

    override fun update() {}

    override fun value() = func()
}

sealed class BindingValue<T> {
    abstract fun set(element: AnyElement, value: T)
}

data class StateBindingValue<T>(val innerHTML: String, val attributes: Map<String, String>) : BindingValue<T>() {
    override fun set(element: AnyElement, value: T) {
        element.attributes.clear()
        element.attributes.putAll(attributes)
        element.innerHTML = innerHTML
    }
}

data class BuildersBindingValue<T>(val builders: List<BindingElementBuilder<*, T>>) : BindingValue<T>() {
    override fun set(element: AnyElement, value: T) {
        element.attributes.clear()
        element.innerHTML = ""
        builders.forEach { (it as BindingElementBuilder<*, T>)(element, value) }
    }
}

data class Binding<T>(val element: AnyElement, val condition: BindingCondition<T>, val value: BindingValue<T>) {
    fun checkAndUpdate() {
        if (condition.needsUpdate()) {
            condition.update()
            value.set(element, condition.value())
        }
    }
}

class BindingException : RuntimeException("Binding already set")

data class Watch<T>(val condition: BindingCondition<T>, val update: (T) -> Unit) {
    internal fun doUpdate(force: Boolean = true) {
        if (force || condition.needsUpdate()) {
            condition.update()
            update(condition.value())
        }
    }
}

fun <T> AnyBinding<T>.binding() = FunctionBindingCondition(this)
fun BoolBinding.valueBinding() = FunctionBindingCondition(this)
fun BoolBinding.binding() = BooleanBindingCondition(this)
fun <T> KProperty0<T>.binding() = PropertyBindingCondition(this)

/**
 * WARNING: Do not add elements inside a watch.  They will be updated every time the value changes
 */
@BindingDSL
fun <T> Page.watch(cond: BindingCondition<T>, update: (T) -> Unit) {
    addWatch(Watch(cond, update))
}

/**
 * WARNING: Do not add elements inside a watch.  They will be updated every time the value changes
 */
@BindingDSL
fun <T> Page.watch(cond: KProperty0<T>, update: (T) -> Unit) {
    addWatch(Watch(cond.binding(), update))
}

//TODO want something like binding { style.backgroundColor } bindAll {} syntax for single elements

@BindingDSL
fun <T, U : W3Element> CanHaveElement<U>.bindAll(binding: BindingCondition<T>, builder: (T) -> Unit) {
    val added = mutableSetOf<AnyElement>()
    page.watch(binding) {
        added.forEach { it.remove() }
        added.clear()
        val listenerID = subscribeOnAdd {
            added.add(it)
        }
        builder(it)
        removeAddSubscriber(listenerID)
    }
}

@BindingDSL
fun <T, U : W3Element> CanHaveElement<U>.bindAll(binding: KProperty0<T>, builder: (T) -> Unit) {
    val added = mutableSetOf<AnyElement>()
    page.watch(binding) {
        added.forEach { it.remove() }
        added.clear()
        val listenerID = subscribeOnAdd {
            added.add(it)
        }
        builder(it)
        removeAddSubscriber(listenerID)
    }
}
