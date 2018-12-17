package com.rnett.kframejs.dom.classes

import com.rnett.kframejs.structure.element.CanHaveElement
import com.rnett.kframejs.structure.element.DisplayElement
import com.rnett.kframejs.structure.element.W3Element
import org.w3c.dom.HTMLInputElement
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

typealias UpdateHandler<T> = (T) -> Unit
typealias Validator<T> = (T) -> Boolean

data class InputBinding<T>(val getter: () -> T, val setter: (T) -> Unit)

abstract class InputElement<E : InputElement<E, T, U>, T, U : W3Element>(
    tag: String,
    parent: CanHaveElement<*>,
    val validator: Validator<T>?,
    val onInvalid: () -> Unit = {}
) : DisplayElement<E, U>(tag, parent), ReadWriteProperty<Any, T> {
    abstract fun getValue(): T
    abstract fun setValue(value: T)
    abstract fun isRawValid(): Boolean

    override operator fun getValue(thisRef: Any, property: KProperty<*>): T = getValue()

    override operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) = setValue(value)

    private val _onUpdates = mutableListOf<UpdateHandler<T>>()

    fun onUpdate(handler: UpdateHandler<T>): E {
        _onUpdates.add(handler)
        return this as E
    }

    private var binding: InputBinding<T>? = null

    /**
     * Binding will get updated before onUpdates, and then the value will be updated to binding afterwards
     */
    infix fun bindTo(binding: InputBinding<T>): E {
        if (this.binding != null)
            throw RuntimeException("Input binding is already bound")

        this.binding = binding
        updateFromBinding()
        return this as E
    }

    infix fun bindTo(property: KMutableProperty0<T>) = bindTo(InputBinding({ property.get() }, { property.set(it) }))

    fun valid() = isRawValid() && (validator?.invoke(getValue()) ?: true)

    fun updateFromBinding() {
        binding?.apply { setValue(getter()) }
    }

    override fun onUpdate() {
        val newValue = getValue()
        if (valid()) {
            binding?.apply { setter(newValue) }
            _onUpdates.forEach { it(newValue) }
        } else
            onInvalid()

        binding?.apply { setValue(getter()) }
    }

    var value: T
        get() = getValue()
        set(value) = setValue(value)


    init {
        on.change {

        }
        updateFromBinding()
    }

}

typealias Converter<T, R> = (T) -> R

class DefaultInputElement<T>(
    tag: String,
    parent: CanHaveElement<*>,
    val fromRaw: Converter<String, T>,
    val toRaw: Converter<T, String>,
    validator: Validator<T>?,
    onInvalid: () -> Unit,
    val rawValidator: (String) -> Boolean
) : InputElement<DefaultInputElement<T>, T, HTMLInputElement>(tag, parent, validator, onInvalid) {

    override fun getValue(): T = fromRaw(underlying.value)
    override fun setValue(value: T) {
        attributes.value = toRaw(value)
    }

    override fun isRawValid(): Boolean = rawValidator(underlying.value) &&
        try {
            getValue()
            true
        } catch (e: Exception) {
            false
        }
}