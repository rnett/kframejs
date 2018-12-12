package com.rnett.kframejs.structure

import com.rnett.kframejs.descendants

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

@DslMarker
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.TYPE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY
)
annotation class SelectorSeperatorDSL

class Selector private constructor(private val ms: MutableList<SelectorElement>) {
    constructor() : this(mutableListOf())

    fun copy() = Selector(mutableListOf(*ms.toTypedArray()))

    fun select(start: List<AnyElement>): List<AnyElement> {
        var candidates = start
        for (m in ms) {
            candidates = m.select(candidates)
        }

        return candidates.distinct()
    }

    internal fun addFilter(filter: SelectorFilter) = copy().apply {

        if (ms.size == 0 || ms.last().next != null)
            ms.add(SelectorElement(mutableListOf(filter), null))
        else
            ms.last().addFilter(filter)
    }

    internal fun addSeperator(separator: SelectorSeparator) = copy().apply {
        if (ms.last().next == null)
            ms.last()._next = separator
        else
            throw FilterException("Can not add two separators in a row")
    }

}

@SelectorSeperatorDSL
val `$`
    get() = Selector()
@SelectorSeperatorDSL
val blankSelector
    get() = `$`

class FilterException(message: String) : RuntimeException(message)

data class SelectorElement(
    private val _filters: MutableList<SelectorFilter>,
    internal var _next: SelectorSeparator?
) {

    val next get() = _next
    val filters get() = _filters.toList()

    fun select(incoming: List<AnyElement>): List<AnyElement> {
        val candidates = incoming.filter { element -> filters.all { it.matches(element) } }

        return if (next == null)
            candidates.distinct()
        else
            candidates.flatMap { next!!.canidates(it) }.distinct()
    }

    internal fun addFilter(filter: SelectorFilter) {
        _filters.add(filter)
    }

}

sealed class SelectorFilter {
    abstract fun matches(element: AnyElement): Boolean

    data class Tag(val tags: List<String>) : SelectorFilter() {
        constructor(tag: String) : this(listOf(tag))

        override fun matches(element: AnyElement) = tags.any { it == element.tag }
    }

    data class Class(val classes: List<String>) : SelectorFilter() {
        constructor(klass: String) : this(listOf(klass))

        override fun matches(element: AnyElement) = classes.any { it in element.classes }
    }

    data class ID(val ids: List<String>) : SelectorFilter() {
        constructor(id: String) : this(listOf(id))

        override fun matches(element: AnyElement) = ids.any { it == element.id }
    }

    data class ChildPosition(val positions: List<Int>) : SelectorFilter() {
        constructor(position: Int) : this(listOf(position))

        override fun matches(element: AnyElement) =
            positions.any { position ->
                if (position == -1)
                    element.parent.children.indexOf(element) == element.parent.children.lastIndex
                else
                    element.parent.children.indexOf(element) == position
            }
    }

    data class BackwardsChildPosition(val positions: List<Int>) : SelectorFilter() {
        constructor(position: Int) : this(listOf(position))

        override fun matches(element: AnyElement) =
            positions.any { position ->
                if (position == -1)
                    element.parent.children.indexOf(element) == 0
                else
                    element.parent.children.indexOf(element) == element.parent.children.lastIndex - position
            }
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

    data class AttributeCustom(val attribute: String, val m: (String?) -> Boolean?) : SelectorFilter() {
        override fun matches(element: AnyElement) = m(element.attributes[attribute]) == true
    }

    data class Custom(val m: (AnyElement) -> Boolean) : SelectorFilter() {
        override fun matches(element: AnyElement) = m(element)
    }
}

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

    class Parent : SelectorSeparator() {
        override fun canidates(start: AnyElement) = listOfNotNull(start.rawParent as? AnyElement)
    }
}



typealias SelectorBuilder = Selector.() -> Selector

//Note: multiple calls apply as and, mutiple args apply as or

@SelectorDSL
fun Selector.tag(tag: String) = addFilter(SelectorFilter.Tag(tag))

@SelectorDSL
fun Selector.tag(vararg tags: String) = addFilter(SelectorFilter.Tag(tags.toList()))

@SelectorDSL
fun Selector.klass(klass: String) = addFilter(SelectorFilter.Class(klass))

@SelectorDSL
fun Selector.klass(vararg classes: String) = addFilter(SelectorFilter.Class(classes.toList()))

@SelectorDSL
fun Selector.id(id: String) = addFilter(SelectorFilter.ID(id))

@SelectorDSL
fun Selector.id(vararg ids: String) = addFilter(SelectorFilter.ID(ids.toList()))

@SelectorDSL
fun Selector.childAt(position: Int) = addFilter(SelectorFilter.ChildPosition(position))

@SelectorDSL
fun Selector.childAt(vararg positions: Int) = addFilter(SelectorFilter.ChildPosition(positions.toList()))

@SelectorDSL
fun Selector.lastChildAt(position: Int) = addFilter(SelectorFilter.BackwardsChildPosition(position))

@SelectorDSL
fun Selector.lastChildAt(vararg positions: Int) = addFilter(SelectorFilter.BackwardsChildPosition(positions.toList()))

@SelectorDSL
fun Selector.firstChild() = childAt(0)

@SelectorDSL
fun Selector.lastChild() = childAt(-1)

@SelectorDSL
fun Selector.onlyChild() = addFilter(SelectorFilter.OnlyChild())

@SelectorDSL
fun Selector.odd() = addFilter(SelectorFilter.OddEven(true))

@SelectorDSL
fun Selector.even() = addFilter(SelectorFilter.OddEven(false))

@SelectorDSL
fun Selector.attributeEquals(attribute: String, value: String) =
    addFilter(SelectorFilter.AttributeValue(attribute, value))

@SelectorDSL
fun Selector.attribute(attribute: String, passes: (String?) -> Boolean?) =
    addFilter(SelectorFilter.AttributeCustom(attribute, passes))

//TODO add first/last, see https://stackoverflow.com/questions/2946205/difference-between-first-and-first-child-not-clear
//TODO add other hard index instead of parent index operators
//TODO firstOfType

// seperators

@SelectorSeperatorDSL
val Selector.child
    get() = addSeperator(SelectorSeparator.Child())

@SelectorSeperatorDSL
val Selector.descendant
    get() = addSeperator(SelectorSeparator.Descendant())

@SelectorSeperatorDSL
val Selector.nextTo
    get() = addSeperator(SelectorSeparator.NextTo())

@SelectorSeperatorDSL
val Selector.siblings
    get() = addSeperator(SelectorSeparator.Siblings())


// usages

infix fun Page.select(m: Selector): List<AnyElement> = m.select(elements.toList())
operator fun Page.get(m: Selector): List<AnyElement> = select(m)

infix fun AnyElement.select(m: Selector): List<AnyElement> = m.select(this.children)
operator fun AnyElement.get(m: Selector) = select(m)

infix fun AnyElement.topSelect(m: Selector) = page.select(m)

infix fun Iterable<AnyElement>.select(m: Selector) = flatMap { it[m] }
operator fun Iterable<AnyElement>.get(m: Selector) = select(m)

infix fun Sequence<AnyElement>.select(m: Selector) = flatMap { it[m].asSequence() }
operator fun Sequence<AnyElement>.get(m: Selector) = select(m)

infix fun Selector.selectFrom(element: AnyElement) = element[this]
infix fun Selector.selectFrom(elements: Iterable<AnyElement>) = elements[this]
infix fun Selector.selectFrom(elements: Sequence<AnyElement>) = elements[this]
infix fun Selector.selectFrom(page: Page) = page[this]

operator fun Selector.get(element: AnyElement) = this selectFrom element
operator fun Selector.get(elements: Iterable<AnyElement>) = this selectFrom elements
operator fun Selector.get(elements: Sequence<AnyElement>) = this selectFrom elements
operator fun Selector.get(page: Page) = this selectFrom page

@SelectorSeperatorDSL
fun `$`(jqyerySelector: String) = jquerySelector(jqyerySelector)

//TODO implement
fun jquerySelector(jqyerySelector: String): Selector {
    var start = `$`

    //TODO don't break on spaces in quotes
    val matches =
        Regex(" > | | + | ~ |([#.:\\[])?(?:\\1|[^ #.:/])*").findAll(jqyerySelector).toList().flatMap { it.groupValues }
            .filter { it.isNotEmpty() }
            .filter { it == " " || it.length > 1 }

    for (m in matches) {

        when (m[0]) {
            ' ' -> {
                when (m) {
                    " " -> start = start.descendant
                    " > " -> start = start.child
                    " + " -> start = start.nextTo
                    " ~ " -> start = start.siblings
                }
            }
            '#' -> {
                val ids = m.split(",").map { it.trim('#') }
                start = start.id(*ids.toTypedArray())
            }
            '.' -> {
                val classes = m.split(",").map { it.trim('.') }
                start = start.klass(*classes.toTypedArray())
            }
            ':' -> {
                val sel = m.drop(1)
                when (sel) {
                    "first-child" -> start = start.firstChild()
                    "last-child" -> start = start.lastChild()
                    "only-child" -> start = start.onlyChild()
                    else -> {
                        when {
                            "nth-child" in sel -> {
                                val n = "nth-child[(]([0-9]*)[)]".toRegex().find(sel)!!.groupValues[1].toInt()
                                start = start.childAt(n)
                            }
                            "nth-last-child" in sel -> {
                                val n = "nth-last-child[(]([0-9]*)[)]".toRegex().find(sel)!!.groupValues[1].toInt()
                                start = start.lastChildAt(n)

                            }
                        }
                    }
                }
            }
            '[' -> {
                val str = m.trim('[', ']')
                val attr = str.split('=')[0]
                val value = str.split('=')[1]
                start = start.attributeEquals(attr, value)
            }
            else -> {
                val tags = m.split(",")
                start = start.tag(*tags.toTypedArray())
            }
        }
    }

    console.log(start)

    return start
}
