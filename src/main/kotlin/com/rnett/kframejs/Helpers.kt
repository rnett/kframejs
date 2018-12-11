package com.rnett.kframejs

import com.rnett.kframejs.structure.AnyElement
import com.rnett.kframejs.structure.Element

inline fun <T> Iterable<T>.applyForEach(func: T.() -> Unit) = also { forEach { it.apply(func) } }
inline fun <T> Sequence<T>.applyForEach(func: T.() -> Unit) = also { forEach { it.apply(func) } }

inline fun <E : Element<E>> Iterable<AnyElement>.filterType() = mapNotNull {
    @Suppress("UNCHECKED_CAST")
    it as? E
}

inline operator fun <E : AnyElement> Iterable<E>.invoke(func: E.() -> Unit) = applyForEach(func)