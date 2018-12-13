package com.rnett.kframejs.structure

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class BindingDSL

typealias AnyBinding = () -> Any?
typealias BoolBinding = () -> Boolean

sealed class BindingCondition {
    abstract fun needsUpdate(): Boolean
    abstract fun update()
}

data class FunctionBindingCondition(val func: AnyBinding) : BindingCondition() {

    private var lastValue: Any? = func()

    override fun needsUpdate(): Boolean = func() != lastValue

    override fun update() {
        lastValue = func()
    }

    class Builder {

        internal fun values() = values.toList()

        private val values: MutableList<Any?> = mutableListOf()

        @BindingDSL
        fun bind(value: Any?) {
            values.add(value)
        }
    }
}


@BindingDSL
fun <R> binding(builder: FunctionBindingCondition.Builder.() -> R) = FunctionBindingCondition {
    val b = FunctionBindingCondition.Builder()
    val result = builder(b)
    b.values().plus(result)
}

data class BooleanBindingCondition(val func: BoolBinding) : BindingCondition() {
    override fun needsUpdate() = func()

    override fun update() {}
}

sealed class BindingValue {
    abstract fun set(element: AnyElement)
}

data class StateBindingValue(val innerHTML: String, val attributes: Map<String, String>) : BindingValue() {
    override fun set(element: AnyElement) {
        element.attributes.clear()
        element.attributes.putAll(attributes)
        element.innerHTML = innerHTML
    }
}

data class BuildersBindingValue(val builders: List<AnyElementBuilder>) : BindingValue() {
    override fun set(element: AnyElement) {
        element.attributes.clear()
        element.innerHTML = ""
        builders.forEach { (it as AnyElementBuilder)(element) }
    }
}

data class Binding(val element: AnyElement, val condition: BindingCondition, val value: BindingValue) {
    fun checkAndUpdate() {
        if (condition.needsUpdate()) {
            condition.update()
            value.set(element)
        }
    }
}

class BindingException : RuntimeException("Binding already set")

data class Watch(val condition: BindingCondition, val update: () -> Unit) {
    fun doUpdate() {
        if (condition.needsUpdate()) {
            condition.update()
            update()
        }
    }
}

fun AnyBinding.binding() = FunctionBindingCondition(this)
fun BoolBinding.valueBinding() = FunctionBindingCondition(this)
fun BoolBinding.binding() = BooleanBindingCondition(this)

/**
 * WARNING: Do not add elements inside a watch.  They will be updated every time the value changes
 */
@BindingDSL
fun Page.watch(cond: BindingCondition, update: () -> Unit) {
    addWatch(Watch(cond, update))
}

//TODO want something like binding { style.backgroundColor } bindAll {} syntax for single elements

@BindingDSL
fun CanHaveElement.bindAll(binding: BindingCondition, builder: () -> Unit) {
    val added = mutableSetOf<AnyElement>()
    page.watch(binding) {
        added.forEach { it.remove() }
        added.clear()
        val listenerID = subscribeOnAdd {
            added.add(it)
        }
        builder()
        removeAddSubscriber(listenerID)
    }
}
