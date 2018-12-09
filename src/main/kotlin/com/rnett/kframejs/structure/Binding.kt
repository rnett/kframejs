package com.rnett.kframejs.structure

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class BindingDSL

sealed class BindingCondition {
    abstract fun needsUpdate(): Boolean
    abstract fun update()
}

data class FunctionBindingCondition(val func: () -> Any?) : BindingCondition() {

    private var lastValue: Any? = func()

    override fun needsUpdate() = func() == lastValue

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
fun binding(builder: FunctionBindingCondition.Builder.() -> Unit) = FunctionBindingCondition {
    FunctionBindingCondition.Builder().apply(builder).values()
}

data class BooleanBindingCondition(val func: () -> Boolean) : BindingCondition() {
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

//TODO watches on binding conditions.  so that I can have an update without a reset
//  something like div{ watch{ cond builder } then {} }
