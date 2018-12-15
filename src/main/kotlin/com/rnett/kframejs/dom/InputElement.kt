package com.rnett.kframejs.dom

import com.rnett.kframejs.dom.classes.DefaultInputElement
import com.rnett.kframejs.dom.classes.InputBinding
import com.rnett.kframejs.dom.classes.Validator
import com.rnett.kframejs.structure.*
import org.w3c.dom.HTMLInputElement
import kotlin.reflect.KMutableProperty0


inline fun <T> T.stringInput(
    klass: String = "",
    id: String = "",
    noinline validator: Validator<String>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<String>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    DefaultInputElement("input", this, { it }, { it }, validator, onInvalid, { true }).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }

inline fun <T> T.stringInput(
    prop: KMutableProperty0<String>,
    klass: String = "",
    id: String = "",
    noinline validator: Validator<String>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<String>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    stringInput(klass, id, validator, onInvalid, builder).invoke {
        bindTo(prop)
    }

inline fun <T> T.stringInput(
    binding: InputBinding<String>,
    klass: String = "",
    id: String = "",
    noinline validator: Validator<String>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<String>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    stringInput(klass, id, validator, onInvalid, builder).invoke {
        bindTo(binding)
    }

inline fun <T> T.stringInput(
    noinline getter: () -> String, noinline setter: (String) -> Unit,
    klass: String = "",
    id: String = "",
    noinline validator: Validator<String>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<String>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    stringInput(InputBinding(getter, setter), klass, id, validator, onInvalid, builder)


inline fun <T> T.doubleInput(
    klass: String = "",
    id: String = "",
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
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        attributes.type = "number"
        underlying.type = "number"
        builder()
    }

inline fun <T> T.doubleInput(
    prop: KMutableProperty0<Double>,
    klass: String = "",
    id: String = "",
    noinline validator: Validator<Double>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<Double>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    doubleInput(klass, id, validator, onInvalid, builder).invoke {
        bindTo(prop)
    }

inline fun <T> T.doubleInput(
    binding: InputBinding<Double>,
    klass: String = "",
    id: String = "",
    noinline validator: Validator<Double>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<Double>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    doubleInput(klass, id, validator, onInvalid, builder).invoke {
        bindTo(binding)
    }

inline fun <T> T.doubleInput(
    noinline getter: () -> Double, noinline setter: (Double) -> Unit,
    klass: String = "",
    id: String = "",
    noinline validator: Validator<Double>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<Double>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    doubleInput(InputBinding(getter, setter), klass, id, validator, onInvalid, builder)


inline fun <T> T.intInput(
    klass: String = "",
    id: String = "",
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
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        attributes.type = "number"
        underlying.type = "number"
        builder()
    }

inline fun <T> T.intInput(
    prop: KMutableProperty0<Int>,
    klass: String = "",
    id: String = "",
    noinline validator: Validator<Int>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<Int>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    intInput(klass, id, validator, onInvalid, builder).invoke {
        bindTo(prop)
    }

inline fun <T> T.intInput(
    binding: InputBinding<Int>,
    klass: String = "",
    id: String = "",
    noinline validator: Validator<Int>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<Int>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    intInput(klass, id, validator, onInvalid, builder).invoke {
        bindTo(binding)
    }

inline fun <T> T.intInput(
    noinline getter: () -> Int, noinline setter: (Int) -> Unit,
    klass: String = "",
    id: String = "",
    noinline validator: Validator<Int>? = null,
    noinline onInvalid: () -> Unit = {},
    crossinline builder: ElementBuilder<DefaultInputElement<Int>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    intInput(InputBinding(getter, setter), klass, id, validator, onInvalid, builder)


inline fun <T, R> T.input(
    noinline fromRaw: (String) -> R,
    noinline toRaw: (R) -> String = { it.toString() },
    klass: String = "",
    id: String = "",
    noinline validator: Validator<R>? = null,
    noinline onInvalid: () -> Unit = {},
    noinline rawValidator: Validator<String> = { true },
    crossinline builder: ElementBuilder<DefaultInputElement<R>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    DefaultInputElement(
        "input",
        this,
        fromRaw,
        toRaw,
        validator,
        onInvalid,
        rawValidator
    ).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }

inline fun <T, R> T.input(
    prop: KMutableProperty0<R>,
    noinline fromRaw: (String) -> R,
    noinline toRaw: (R) -> String = { it.toString() },
    klass: String = "",
    id: String = "",
    noinline validator: Validator<R>? = null,
    noinline onInvalid: () -> Unit = {},
    noinline rawValidator: Validator<String> = { true },
    crossinline builder: ElementBuilder<DefaultInputElement<R>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    input(fromRaw, toRaw, klass, id, validator, onInvalid, rawValidator, builder).invoke {
        bindTo(prop)
    }

inline fun <T, R> T.input(
    binding: InputBinding<R>,
    noinline fromRaw: (String) -> R,
    noinline toRaw: (R) -> String = { it.toString() },
    klass: String = "",
    id: String = "",
    noinline validator: Validator<R>? = null,
    noinline onInvalid: () -> Unit = {},
    noinline rawValidator: Validator<String> = { true },
    crossinline builder: ElementBuilder<DefaultInputElement<R>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    input(fromRaw, toRaw, klass, id, validator, onInvalid, rawValidator, builder).invoke {
        bindTo(binding)
    }
/* causes conflicts for some reason
inline fun <T, R> T.input(
    noinline getter: () -> R, noinline setter: (R) -> Unit,
    noinline fromRaw: (String) -> R,
    noinline toRaw: (R) -> String = {it.toString()},
    klass: String = "",
    noinline validator: Validator<R>? = null,
    noinline onInvalid: () -> Unit = {},
    noinline rawValidator: Validator<String> = {true},
    crossinline builder: ElementBuilder<DefaultInputElement<R>> = {}
) where T : AnyDisplayElement, T : CanHaveElement<*> =
    input(InputBinding(getter, setter), fromRaw, toRaw, klass, validator, onInvalid, rawValidator, builder)
*/

@KFrameElementDSL
inline fun <ParentType> ParentType.rawInput(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLInputElement> = {}
)
        where ParentType : AnyDisplayElement, ParentType : CanHaveElement<*> =
    StandardDisplayElement<HTMLInputElement>("input", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }


//TODO dropdowns, combo boxes?, all the rest (ugh)