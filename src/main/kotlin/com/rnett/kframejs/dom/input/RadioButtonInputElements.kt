package com.rnett.kframejs.dom.input

import com.rnett.kframejs.dom.classes.InputBinding
import com.rnett.kframejs.dom.classes.RadioButton
import com.rnett.kframejs.dom.classes.RadioGroup
import com.rnett.kframejs.dom.classes.genGroupName
import com.rnett.kframejs.structure.element.*
import kotlin.reflect.KMutableProperty0


fun <T> Page.RadioGroup(binding: InputBinding<T>, groupName: String = genGroupName()) =
    RadioGroup(binding, this, groupName)

fun <T> Page.RadioGroup(binding: KMutableProperty0<T>, groupName: String = genGroupName()) =
    RadioGroup(binding, this, groupName)

fun <T> Page.RadioGroup(getter: () -> T, setter: (T) -> Unit, groupName: String = genGroupName()) =
    RadioGroup(getter, setter, this, groupName)

@KFrameElementDSL
inline fun <ParentType, T> ParentType.radioButton(
    value: T,
    group: RadioGroup<T>,
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<RadioButton<T>> = {}
)
        where ParentType : IDisplayElement<*>, ParentType : CanHaveElement<*> =
    RadioButton(group, value, this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }