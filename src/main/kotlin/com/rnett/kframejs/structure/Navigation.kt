package com.rnett.kframejs.structure

@DslMarker
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.TYPE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY
)
annotation class SelectorDSL

class Selector private constructor(private val selectors: MutableList<SelectorElement>) {
    constructor() : this(mutableListOf())

    fun copy() = Selector(mutableListOf(*selectors.toTypedArray()))

    fun select(start: List<AnyElement>): List<AnyElement> {
        var candidates = start
        for (selector in selectors) {
            candidates = selector.select(candidates)
        }

        return candidates.distinct()
    }

    internal fun addFilter(filter: SelectorFilter) = copy().apply {

        if (selectors.size == 0 || selectors.last().next != null)
            selectors.add(SelectorElement(mutableListOf(filter), null))
        else
            selectors.last().addFilter(filter)
    }

    internal fun addSeperator(separator: SelectorSeparator) = copy().apply {
        if (selectors.last().next == null)
            selectors.last()._next = separator
        else
            throw FilterException("Can not add two separators in a row")
    }

}

@SelectorDSL
val `$`
    get() = Selector()

class FilterException(message: String) : RuntimeException(message)

data class SelectorElement(private val _filters: MutableList<SelectorFilter>, internal var _next: SelectorSeparator?) {

    val next get() = _next
    val filters get() = _filters.toList()

    fun select(incoming: List<AnyElement>): List<AnyElement> {
        val candidates = incoming.filter { element -> filters.all { it.matches(element) } }

        console.log("Searching: ", incoming.map { it.underlying }.toTypedArray())
        console.log("With: ", filters.toTypedArray())
        console.log("Passed: ", candidates.map { it.underlying }.toTypedArray())

        console.log("Moving on to: ", next)

        return if (next == null)
            candidates.distinct()
        else
            candidates.flatMap { next!!.canidates(it) }.distinct()
    }

    internal fun addFilter(filter: SelectorFilter) {
        _filters.add(filter)
    }

}

typealias SelectorBuilder = Selector.() -> Selector

@SelectorDSL
fun Selector.tag(tag: String) = addFilter(SelectorFilter.Tag(tag))

@SelectorDSL
fun Selector.klass(klass: String) = addFilter(SelectorFilter.Class(klass))

@SelectorDSL
fun Selector.id(id: String) = addFilter(SelectorFilter.ID(id))

@SelectorDSL
fun Selector.child(position: Int) = addFilter(SelectorFilter.ChildPosition(position))

@SelectorDSL
fun Selector.lastChild(position: Int) = addFilter(SelectorFilter.BackwardsChildPosition(position))

@SelectorDSL
fun Selector.firstChild() = child(0)

@SelectorDSL
fun Selector.lastChild() = child(-1)

@SelectorDSL
fun Selector.onlyChild() = addFilter(SelectorFilter.OnlyChild())

@SelectorDSL
fun Selector.odd() = addFilter(SelectorFilter.OddEven(true))

@SelectorDSL
fun Selector.even() = addFilter(SelectorFilter.OddEven(false))

@SelectorDSL
fun Selector.child() = addSeperator(SelectorSeparator.Child())

@SelectorDSL
fun Selector.descendant() = addSeperator(SelectorSeparator.Descendant())

@SelectorDSL
fun Selector.nextTo() = addSeperator(SelectorSeparator.NextTo())

@SelectorDSL
fun Selector.siblings() = addSeperator(SelectorSeparator.Siblings())

fun Page.select(selector: Selector): List<AnyElement> = selector.select(elements.toList())
operator fun Page.get(selector: Selector): List<AnyElement> = select(selector)
operator fun Page.invoke(selector: SelectorBuilder) = select(Selector().let(selector))
fun Page.`$`(selector: Selector) = select(selector)
fun Page.`$`(selector: SelectorBuilder) = select(Selector().let(selector))

fun AnyElement.select(selector: Selector): List<AnyElement> = selector.select(this.children)
fun AnyElement.topSelect(selector: Selector) = page.select(selector)

operator fun AnyElement.get(selector: Selector) = select(selector)
operator fun AnyElement.invoke(selector: SelectorBuilder) = select(Selector().let(selector))
fun AnyElement.`$`(selector: Selector) = topSelect(selector)

sealed class SelectorFilter {
    abstract fun matches(element: AnyElement): Boolean

    data class Tag(val tag: String) : SelectorFilter() {
        override fun matches(element: AnyElement) = tag == element.tag
    }

    data class Class(val klass: String) : SelectorFilter() {
        override fun matches(element: AnyElement) = klass in element.classes
    }

    data class ID(val id: String) : SelectorFilter() {
        override fun matches(element: AnyElement) = id == element.id
    }

    data class ChildPosition(val position: Int) : SelectorFilter() {
        override fun matches(element: AnyElement) =
            if (position == -1) element.parent.children.indexOf(element) == element.parent.children.lastIndex
            else element.parent.children.indexOf(element) == position
    }

    data class BackwardsChildPosition(val position: Int) : SelectorFilter() {
        override fun matches(element: AnyElement) =
            if (position == -1) element.parent.children.indexOf(element) == 0
            else element.parent.children.indexOf(element) == element.parent.children.lastIndex - position
    }

    class OnlyChild : SelectorFilter() {
        override fun matches(element: AnyElement) = element.parent.children.size == 1
    }

    data class OddEven(val odd: Boolean) : SelectorFilter() {
        override fun matches(element: AnyElement) =
            if (odd) element.parent.children.indexOf(element) % 2 == 1
            else element.parent.children.indexOf(element) % 2 == 0
    }

    data class AttributeValue(val attribute: String, val value: String) : SelectorFilter() {
        constructor(attribute: String, value: Any) : this(attribute, value.toString())

        override fun matches(element: AnyElement) = element.attributes[attribute] == value
    }

    data class AttributeCustom(val attribute: String, val selector: (String?) -> Boolean?) : SelectorFilter() {
        override fun matches(element: AnyElement) = selector(element.attributes[attribute]) == true
    }

    data class Custom(val selector: (AnyElement) -> Boolean) : SelectorFilter() {
        override fun matches(element: AnyElement) = selector(element)
    }
}

fun AnyElement.descendants(): List<AnyElement> = children + children.flatMap { it.descendants() }

sealed class SelectorSeparator {
    abstract fun canidates(start: AnyElement): List<AnyElement>

    class Descendant : SelectorSeparator() {
        override fun canidates(start: AnyElement) = start.descendants()
    }

    class Child : SelectorSeparator() {
        override fun canidates(start: AnyElement) = start.children
    }

    class NextTo : SelectorSeparator() {
        override fun canidates(start: AnyElement): List<AnyElement> {
            val index = start.parent.children.indexOf(start)
            return listOfNotNull(start.parent.children.getOrNull(index + 1), start.parent.children.getOrNull(index - 1))
        }
    }

    class Siblings : SelectorSeparator() {
        override fun canidates(start: AnyElement) = start.parent.children - start
    }
}

