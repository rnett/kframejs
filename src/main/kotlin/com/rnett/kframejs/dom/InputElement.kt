package com.rnett.kframejs.dom

import com.rnett.kframejs.dom.classes.DefaultInputElement
import com.rnett.kframejs.dom.classes.InputBinding
import com.rnett.kframejs.dom.classes.InputElement
import com.rnett.kframejs.dom.classes.Validator
import com.rnett.kframejs.structure.CanHaveElement
import com.rnett.kframejs.structure.ElementBuilder
import com.rnett.kframejs.structure.IDisplayElement
import kotlin.reflect.KMutableProperty0


inline fun IDisplayElement.stringInput(
    klass: String = "",
    noinline validator: Validator<String>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<InputElement<String>> = {}
) =
    DefaultInputElement("input", this as CanHaveElement, { it }, { it }, validator, onInvalid, { true }).invoke {
        this.klass = klass
        builder()
    }

inline fun IDisplayElement.stringInput(
    prop: KMutableProperty0<String>,
    klass: String = "",
    noinline validator: Validator<String>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<InputElement<String>> = {}
) =
    stringInput(klass, validator, onInvalid, builder).invoke {
        bindTo(prop)
    }

inline fun IDisplayElement.stringInput(
    binding: InputBinding<String>,
    klass: String = "",
    noinline validator: Validator<String>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<InputElement<String>> = {}
) =
    stringInput(klass, validator, onInvalid, builder).invoke {
        bindTo(binding)
    }

inline fun IDisplayElement.stringInput(
    noinline getter: () -> String, noinline setter: (String) -> Unit,
    klass: String = "",
    noinline validator: Validator<String>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<InputElement<String>> = {}
) =
    stringInput(InputBinding(getter, setter), klass, validator, onInvalid, builder)


inline fun IDisplayElement.doubleInput(
    klass: String = "",
    noinline validator: Validator<Double>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<InputElement<Double>> = {}
) =
    DefaultInputElement(
        "input",
        this as CanHaveElement,
        { it.toDouble() },
        { it.toString() },
        validator,
        onInvalid,
        { it.toDoubleOrNull() != null }).invoke {
        this.klass = klass
        builder()
    }

inline fun IDisplayElement.doubleInput(
    prop: KMutableProperty0<Double>,
    klass: String = "",
    noinline validator: Validator<Double>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<InputElement<Double>> = {}
) =
    doubleInput(klass, validator, onInvalid, builder).invoke {
        bindTo(prop)
    }

inline fun IDisplayElement.doubleInput(
    binding: InputBinding<Double>,
    klass: String = "",
    noinline validator: Validator<Double>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<InputElement<Double>> = {}
) =
    doubleInput(klass, validator, onInvalid, builder).invoke {
        bindTo(binding)
    }

inline fun IDisplayElement.doubleInput(
    noinline getter: () -> Double, noinline setter: (Double) -> Unit,
    klass: String = "",
    noinline validator: Validator<Double>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<InputElement<Double>> = {}
) =
    doubleInput(InputBinding(getter, setter), klass, validator, onInvalid, builder)


inline fun IDisplayElement.intInput(
    klass: String = "",
    noinline validator: Validator<Int>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<InputElement<Int>> = {}
) =
    DefaultInputElement(
        "input",
        this as CanHaveElement,
        { it.toInt() },
        { it.toString() },
        validator,
        onInvalid,
        { it.toIntOrNull() != null }).invoke {
        this.klass = klass
        builder()
    }

inline fun IDisplayElement.intInput(
    prop: KMutableProperty0<Int>,
    klass: String = "",
    noinline validator: Validator<Int>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<InputElement<Int>> = {}
) =
    intInput(klass, validator, onInvalid, builder).invoke {
        bindTo(prop)
    }

inline fun IDisplayElement.intInput(
    binding: InputBinding<Int>,
    klass: String = "",
    noinline validator: Validator<Int>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<InputElement<Int>> = {}
) =
    intInput(klass, validator, onInvalid, builder).invoke {
        bindTo(binding)
    }

inline fun IDisplayElement.intInput(
    noinline getter: () -> Int, noinline setter: (Int) -> Unit,
    klass: String = "",
    noinline validator: Validator<Int>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<InputElement<Int>> = {}
) =
    intInput(InputBinding(getter, setter), klass, validator, onInvalid, builder)

//TODO generic input

//TODO dropdowns, combo boxes?, all the rest (ugh)

