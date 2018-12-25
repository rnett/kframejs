package com.rnett.kframejs.dom.classes

import com.rnett.kframejs.structure.addons.binding
import com.rnett.kframejs.structure.addons.watch
import com.rnett.kframejs.structure.element.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLOptionElement
import org.w3c.dom.HTMLSelectElement
import kotlin.collections.set
import kotlin.reflect.KMutableProperty0

//TODO dropdown, combobox, autoselect dropdown, multiline string input

class CheckboxElement(parent: CanHaveElement<*>) :
    InputElement<CheckboxElement, Boolean, HTMLInputElement>("input", parent, { true }, {}) {
    override fun getValue(): Boolean = underlying.checked

    override fun setValue(value: Boolean) {
        underlying.checked = value
    }

    override fun isRawValid(): Boolean = true

    init {
        attributes.type = "checkbox"
    }
}

class NullableCheckboxElement(parent: CanHaveElement<*>) :
    InputElement<NullableCheckboxElement, Boolean?, HTMLInputElement>("input", parent, { true }, {}) {
    override fun getValue(): Boolean? =
        if (underlying.indeterminate)
            null
        else
            underlying.checked

    override fun setValue(value: Boolean?) {
        if (value == null)
            underlying.indeterminate = true
        else {
            underlying.indeterminate = false
            underlying.checked = value
        }
    }

    override fun isRawValid(): Boolean = true

    init {
        attributes.type = "checkbox"
    }
}

private val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

internal fun randomString(length: Int) =
    buildString {
        (0..length).forEach { _ -> append(source.random()) }
    }


private val usedGroupNames = mutableSetOf<String>()
internal fun genGroupName(): String {
    var len = 10
    var name = randomString(len)
    while (name in usedGroupNames) {
        len++
        name = randomString(len)
    }

    return name
}

class RadioGroup<T>(val binding: InputBinding<T>, val page: Page, val groupName: String = genGroupName()) {
    constructor(binding: KMutableProperty0<T>, page: Page, groupName: String = genGroupName()) : this(
        InputBinding(
            binding::get,
            binding::set
        ), page, groupName
    )

    constructor(getter: () -> T, setter: (T) -> Unit, page: Page, groupName: String = genGroupName()) : this(
        InputBinding(getter, setter),
        page,
        groupName
    )

    init {
        if (groupName in usedGroupNames)
            throw IllegalArgumentException("Name $groupName is already used")

        usedGroupNames.add(groupName)
    }

    private var currentIndex = 0

    internal fun nextIndex() = currentIndex++

    private val _values = mutableMapOf<Int, T>()
    val values get() = _values.toMap()

    private val _radioButtons = mutableMapOf<Int, RadioButton<T>>()
    val radioButtons get() = _radioButtons.toMap()

    internal fun addRadioButton(button: RadioButton<T>) {
        val idx = nextIndex()
        _values[idx] = button.value
        button.attributes.value = idx.toString()
        _radioButtons[idx] = button
    }

    internal fun select(index: Int) {
        binding.setter(_values[index]!!)
    }

    init {
        page.watch(binding.getter.binding()) {
            update(it)
        }
    }

    private fun update(newValue: T) {
        _radioButtons.values.forEach { it.checked = false }
        val index = _values.entries.find { it.value == newValue }?.key
        if (index != null) {
            _radioButtons[index]?.checked = true
        }
    }

}

class RadioButton<T>(val group: RadioGroup<T>, val value: T, parent: CanHaveElement<*>) :
    DisplayElement<RadioButton<T>, HTMLInputElement>("input", parent) {

    val index get() = attributes.value!!.toInt()

    var checked
        get() = underlying.checked
        set(v) {
            underlying.checked = v
        }

    init {
        attributes.type = "radio"
        attributes["name"] = group.groupName
        group.addRadioButton(this)

        on.change {
            if (underlying.checked) {
                group.select(index)
            }
        }
    }
}

//TODO indexing is kind of odd for nulls, it just defaults to the first value
class SelectElement<T>(
    val values: List<T>,
    val optionBuilder: StandardDisplayElement<HTMLOptionElement>.(T) -> Unit,
    defaultIndex: Int?,
    parent: CanHaveElement<*>
) :
    InputElement<SelectElement<T>, T, HTMLSelectElement>("select", parent, { true }, {}) {

    override fun getValue(): T = values[index!!]

    override fun setValue(value: T) {
        index = values.indexOf(value).let { if (it == -1) null else it }
    }

    override fun isRawValid(): Boolean = index != null

    private val optionElements = mutableListOf<Element<*, HTMLOptionElement>>()

    var index
        get() = optionElements.indexOfFirst { it.underlying.selected }.let { if (it == -1) null else it }
        set(v) {
            if (v == null)
                optionElements.forEach { it.underlying.selected = false }
            else
                optionElements[v].underlying.selected = true
        }

    init {
        values.forEach {
            val o = StandardDisplayElement<HTMLOptionElement>("option", this)
            optionElements.add(o)
            o {
                optionBuilder(it)
            }
        }
        index = defaultIndex
    }
}