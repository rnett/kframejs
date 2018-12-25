package com.rnett.kframejs.dom.input

import com.rnett.kframejs.dom.classes.CheckboxElement
import com.rnett.kframejs.dom.classes.InputBinding
import com.rnett.kframejs.dom.classes.NullableCheckboxElement
import com.rnett.kframejs.structure.element.AnyDisplayElement
import com.rnett.kframejs.structure.element.CanHaveElement
import com.rnett.kframejs.structure.element.ElementBuilder
import kotlin.reflect.KMutableProperty0

inline fun <T> T.booleanInput(
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<CheckboxElement> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    CheckboxElement(this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }

inline fun <T> T.booleanInput(
    prop: KMutableProperty0<Boolean>,
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<CheckboxElement> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    booleanInput(klass, id, builder).invoke {
        bindTo(prop)
    }

inline fun <T> T.booleanInput(
    binding: InputBinding<Boolean>,
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<CheckboxElement> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    booleanInput(klass, id, builder).invoke {
        bindTo(binding)
    }

inline fun <T> T.booleanInput(
    noinline getter: () -> Boolean, noinline setter: (Boolean) -> Unit,
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<CheckboxElement> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    booleanInput(InputBinding(getter, setter), klass, id, builder)

inline fun <T> T.nullableBooleanInput(
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<NullableCheckboxElement> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    NullableCheckboxElement(this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }

inline fun <T> T.nullableBooleanInput(
    prop: KMutableProperty0<Boolean?>,
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<NullableCheckboxElement> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    nullableBooleanInput(klass, id, builder).invoke {
        bindTo(prop)
    }

inline fun <T> T.nullableBooleanInput(
    binding: InputBinding<Boolean?>,
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<NullableCheckboxElement> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    nullableBooleanInput(klass, id, builder).invoke {
        bindTo(binding)
    }

inline fun <T> T.nullableBooleanInput(
    noinline getter: () -> Boolean?, noinline setter: (Boolean?) -> Unit,
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<NullableCheckboxElement> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    nullableBooleanInput(InputBinding(getter, setter), klass, id, builder)