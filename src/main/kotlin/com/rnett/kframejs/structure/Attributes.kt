package com.rnett.kframejs.structure

import org.w3c.dom.asList
import org.w3c.dom.get

class Attributes(val element: AnyElement) : AttrDelegatableMap() {

    private val raw get() = element.underlying.attributes

    override val size: Int get() = raw.length

    override fun containsKey(key: String): Boolean = raw[key] != null

    override fun containsValue(value: String): Boolean = raw.asList().any { it.value == value }

    override fun get(key: String): String? = element.underlying.getAttribute(key)

    override fun isEmpty(): Boolean = size == 0

    override val entries: MutableSet<MutableMap.MutableEntry<String, String>>
        get() = raw.asList().associate { it.name to it.value }.toMutableMap().entries
    override val keys: MutableSet<String>
        get() = raw.asList().map { it.name }.toMutableSet()
    override val values: MutableCollection<String>
        get() = raw.asList().map { it.value }.toMutableList()

    override fun clear() = listOf(*raw.asList().toTypedArray()).map { it.name }.forEach { raw.removeNamedItem(it) }

    override fun put(key: String, value: String): String? {
        val old = element.underlying.getAttribute(key)
        element.underlying.setAttribute(key, value)
        return old
    }

    override fun putAll(from: Map<out String, String>) = from.forEach { put(it.key, it.value) }

    override fun remove(key: String): String? {
        val old = element.underlying.getAttribute(key)
        element.underlying.removeAttribute(key)
        return old
    }

    inline operator fun invoke(builder: Attributes.() -> Unit): Attributes {
        builder()
        return this
    }


    var value by this
    var type by this
    var title by this

}