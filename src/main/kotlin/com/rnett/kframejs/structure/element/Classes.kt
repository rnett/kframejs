package com.rnett.kframejs.structure.element

import com.rnett.kframejs.structure.addons.HasClass
import kotlinx.serialization.enumMembers
import org.w3c.dom.asList
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class Classes(val element: AnyElement) : MutableSet<String> {

    private val raw get() = this.element.underlying.classList

    override fun add(element: String): Boolean {
        if (raw.contains(element))
            return false

        raw.add(element)
        return true
    }

    override fun addAll(elements: Collection<String>): Boolean = elements.map { add(it) }.any { it }

    override fun clear() {
        raw.value = ""
    }

    override fun iterator(): MutableIterator<String> = raw.asList().toMutableList().iterator()

    override fun remove(element: String): Boolean {
        if (raw.contains(element))
            return false

        raw.remove(element)
        return true
    }

    override fun removeAll(elements: Collection<String>) = elements.map { remove(it) }.any { it }

    override fun retainAll(elements: Collection<String>): Boolean = this.map {
        if (it !in elements)
            remove(it)
        else
            false
    }.any { it }

    override val size: Int
        get() = raw.length

    override fun contains(element: String): Boolean = raw.contains(element)

    override fun containsAll(elements: Collection<String>): Boolean = elements.map { contains(it) }.all { it }

    override fun isEmpty(): Boolean = size == 0

    inline fun <reified EC> by(): ClassEnumDelegate<EC> where EC : Enum<EC>, EC : HasClass = by(EC::class)
    fun <E> by(enumClass: KClass<E>) where E : Enum<E>, E : HasClass = ClassEnumDelegate<E>(this, enumClass)
}

class ClassEnumDelegate<E>(val classes: Classes, val enumClass: KClass<E>) :
    ReadWriteProperty<Any, E?> where E : Enum<E>, E : HasClass {
    private val values = enumClass.enumMembers().associateBy { it.klass }

    override fun getValue(thisRef: Any, property: KProperty<*>): E? =
        classes.find { it in values }?.let { values[it] }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: E?) {
        classes.removeAll { it in values }
        if (value != null)
            classes.add(value.klass)
    }
}