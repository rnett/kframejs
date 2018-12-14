package com.rnett.kframejs

import com.rnett.kframejs.structure.AnyElement
import com.rnett.kframejs.structure.Element
import com.rnett.kframejs.structure.W3Element

inline fun <T> Iterable<T>.applyForEach(func: T.() -> Unit) = also { forEach { it.apply(func) } }
inline fun <T> Sequence<T>.applyForEach(func: T.() -> Unit) = also { forEach { it.apply(func) } }

inline fun <E : Element<E, U>, U : W3Element> Iterable<AnyElement>.filterType() = mapNotNull {
    @Suppress("UNCHECKED_CAST")
    it as? E
}

fun AnyElement.descendants(): List<AnyElement> = children + children.flatMap { it.descendants() }

inline operator fun <E : AnyElement> Iterable<E>.invoke(func: E.() -> Unit) = applyForEach(func)