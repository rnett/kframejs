package com.rnett.kframejs.dom.input

import com.rnett.kframejs.dom.classes.DefaultInputElement
import com.rnett.kframejs.dom.classes.InputBinding
import com.rnett.kframejs.dom.classes.SelectElement
import com.rnett.kframejs.dom.classes.randomString
import com.rnett.kframejs.structure.element.*
import org.w3c.dom.HTMLDataListElement
import org.w3c.dom.HTMLOptionElement
import kotlin.reflect.KMutableProperty0

@KFrameElementDSL
inline fun <ParentType, T> ParentType.selectFrom(
    values: List<T>,
    noinline optionBuilder: StandardDisplayElement<HTMLOptionElement>.(T) -> Unit,
    defaultIndex: Int? = null,
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<SelectElement<T>> = {}
)
        where ParentType : IDisplayElement<*>, ParentType : CanHaveElement<*> =
    SelectElement<T>(values, optionBuilder, defaultIndex, this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }

@KFrameElementDSL
inline fun <ParentType, T> ParentType.selectFrom(
    prop: KMutableProperty0<T>,
    values: List<T>,
    noinline optionBuilder: StandardDisplayElement<HTMLOptionElement>.(T) -> Unit,
    defaultIndex: Int? = null,
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<SelectElement<T>> = {}
)
        where ParentType : IDisplayElement<*>, ParentType : CanHaveElement<*> =
    selectFrom(values, optionBuilder, defaultIndex, klass, id) {
        bindTo(prop)
        builder()
    }

@KFrameElementDSL
inline fun <ParentType, T> ParentType.selectFrom(
    binding: InputBinding<T>,
    values: List<T>,
    noinline optionBuilder: StandardDisplayElement<HTMLOptionElement>.(T) -> Unit,
    defaultIndex: Int? = null,
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<SelectElement<T>> = {}
)
        where ParentType : IDisplayElement<*>, ParentType : CanHaveElement<*> =
    selectFrom(values, optionBuilder, defaultIndex, klass, id) {
        bindTo(binding)
        builder()
    }

@KFrameElementDSL
inline fun <ParentType, T> ParentType.selectFrom(
    noinline getter: () -> T, noinline setter: (T) -> Unit,
    values: List<T>,
    noinline optionBuilder: StandardDisplayElement<HTMLOptionElement>.(T) -> Unit,
    defaultIndex: Int? = null,
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<SelectElement<T>> = {}
)
        where ParentType : IDisplayElement<*>, ParentType : CanHaveElement<*> =
    selectFrom(InputBinding(getter, setter), values, optionBuilder, defaultIndex, klass, id, builder)

private val usedListNames = mutableSetOf<String>()
internal fun getNextListId(): String {
    var len = 10
    var name = "list_" + randomString(len)
    while (name in usedListNames) {
        len++
        name = "list_" + randomString(len)
    }

    return name
}

@KFrameElementDSL
fun <ParentType, T> ParentType.selectFromAutocomplete(
    binding: InputBinding<T>,
    values: List<T>,
    toRaw: (T) -> String = { it.toString() },
    optionBuilder: StandardDisplayElement<HTMLOptionElement>.(T) -> Unit = { +toRaw(it) },
    klass: String = "",
    id: String = "",
    builder: ElementBuilder<SelectElement<T>> = {}
): DefaultInputElement<T>
        where ParentType : IDisplayElement<*>, ParentType : CanHaveElement<*> {

    val raws = values.associate { toRaw(it) to it }
    val listId = getNextListId()

    val i = input(binding, { raws[it]!! }, { toRaw(it) }, klass, id) {
        attributes.type = null
        attributes["list"] = listId
    }

    displayElement<HTMLDataListElement>("datalist")() {
        this.id = listId
        values.forEach {
            displayElement<HTMLOptionElement>("option")() {
                underlying.value = toRaw(it)
                optionBuilder(it)
            }
        }
    }

    return i
}

@KFrameElementDSL
fun <ParentType, T> ParentType.selectFromAutocomplete(
    prop: KMutableProperty0<T>,
    values: List<T>,
    toRaw: (T) -> String = { it.toString() },
    optionBuilder: StandardDisplayElement<HTMLOptionElement>.(T) -> Unit = { +toRaw(it) },
    klass: String = "",
    id: String = "",
    builder: ElementBuilder<SelectElement<T>> = {}
): DefaultInputElement<T>
        where ParentType : IDisplayElement<*>, ParentType : CanHaveElement<*> =
    selectFromAutocomplete(InputBinding(prop::get, prop::set), values, toRaw, optionBuilder, klass, id, builder)

@KFrameElementDSL
fun <ParentType, T> ParentType.selectFromAutocomplete(
    getter: () -> T, setter: (T) -> Unit,
    values: List<T>,
    toRaw: (T) -> String = { it.toString() },
    optionBuilder: StandardDisplayElement<HTMLOptionElement>.(T) -> Unit = { +toRaw(it) },
    klass: String = "",
    id: String = "",
    builder: ElementBuilder<SelectElement<T>> = {}
): DefaultInputElement<T>
        where ParentType : IDisplayElement<*>, ParentType : CanHaveElement<*> =
    selectFromAutocomplete(InputBinding(getter, setter), values, toRaw, optionBuilder, klass, id, builder)