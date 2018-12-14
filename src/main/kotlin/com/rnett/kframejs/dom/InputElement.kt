package com.rnett.kframejs.dom

import com.rnett.kframejs.dom.classes.DefaultInputElement
import com.rnett.kframejs.dom.classes.InputBinding
import com.rnett.kframejs.dom.classes.Validator
import com.rnett.kframejs.structure.AnyDisplayElement
import com.rnett.kframejs.structure.CanHaveElement
import com.rnett.kframejs.structure.ElementBuilder
import kotlin.reflect.KMutableProperty0


inline fun <T> T.stringInput(
    klass: String = "",
    noinline validator: Validator<String>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<String>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    DefaultInputElement("input", this, { it }, { it }, validator, onInvalid, { true }).invoke {
        this.klass = klass
        builder()
    }

inline fun <T> T.stringInput(
    prop: KMutableProperty0<String>,
    klass: String = "",
    noinline validator: Validator<String>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<String>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    stringInput(klass, validator, onInvalid, builder).invoke {
        bindTo(prop)
    }

inline fun <T> T.stringInput(
    binding: InputBinding<String>,
    klass: String = "",
    noinline validator: Validator<String>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<String>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    stringInput(klass, validator, onInvalid, builder).invoke {
        bindTo(binding)
    }

inline fun <T> T.stringInput(
    noinline getter: () -> String, noinline setter: (String) -> Unit,
    klass: String = "",
    noinline validator: Validator<String>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<String>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    stringInput(InputBinding(getter, setter), klass, validator, onInvalid, builder)


inline fun <T> T.doubleInput(
    klass: String = "",
    noinline validator: Validator<Double>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<Double>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    DefaultInputElement(
        "input",
        this,
        { it.toDouble() },
        { it.toString() },
        validator,
        onInvalid,
        { it.toDoubleOrNull() != null }).invoke {
        this.klass = klass
        builder()
    }

inline fun <T> T.doubleInput(
    prop: KMutableProperty0<Double>,
    klass: String = "",
    noinline validator: Validator<Double>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<Double>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    doubleInput(klass, validator, onInvalid, builder).invoke {
        bindTo(prop)
    }

inline fun <T> T.doubleInput(
    binding: InputBinding<Double>,
    klass: String = "",
    noinline validator: Validator<Double>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<Double>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    doubleInput(klass, validator, onInvalid, builder).invoke {
        bindTo(binding)
    }

inline fun <T> T.doubleInput(
    noinline getter: () -> Double, noinline setter: (Double) -> Unit,
    klass: String = "",
    noinline validator: Validator<Double>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<Double>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    doubleInput(InputBinding(getter, setter), klass, validator, onInvalid, builder)


inline fun <T> T.intInput(
    klass: String = "",
    noinline validator: Validator<Int>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<Int>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    DefaultInputElement(
        "input",
        this,
        { it.toInt() },
        { it.toString() },
        validator,
        onInvalid,
        { it.toIntOrNull() != null }).invoke {
        this.klass = klass
        builder()
    }

inline fun <T> T.intInput(
    prop: KMutableProperty0<Int>,
    klass: String = "",
    noinline validator: Validator<Int>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<Int>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    intInput(klass, validator, onInvalid, builder).invoke {
        bindTo(prop)
    }

inline fun <T> T.intInput(
    binding: InputBinding<Int>,
    klass: String = "",
    noinline validator: Validator<Int>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<Int>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    intInput(klass, validator, onInvalid, builder).invoke {
        bindTo(binding)
    }

inline fun <T> T.intInput(
    noinline getter: () -> Int, noinline setter: (Int) -> Unit,
    klass: String = "",
    noinline validator: Validator<Int>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<Int>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    intInput(InputBinding(getter, setter), klass, validator, onInvalid, builder)

//TODO generic input

//TODO dropdowns, combo boxes?, all the rest (ugh)

