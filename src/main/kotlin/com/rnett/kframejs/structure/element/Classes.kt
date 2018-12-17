package com.rnett.kframejs.structure.element

import org.w3c.dom.asList

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

}