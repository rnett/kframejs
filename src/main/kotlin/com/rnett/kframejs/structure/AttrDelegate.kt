package com.rnett.kframejs.structure

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface AttrDelegatable : ReadWriteProperty<Any, String?> {
    fun getForDelegate(key: String): String?
    fun setForDelegate(key: String, value: String)
    fun removeForDelegate(key: String)
    fun transformName(name: String): String = name
    fun getPropertyName(property: KProperty<*>) = transformName(property.name)

    fun <T> byCustom(
        name: String?,
        getTransform: (String?) -> T,
        setTransform: (T) -> String,
        removeOnNull: Boolean = true
    ) =
        AttrDelegate(this, name, getTransform, setTransform, removeOnNull)

    val by get() = byCustom(null, { it }, { it!! })
    val byNotNull get() = byCustom(null, { it ?: "" }, { it }, false)

    fun by(name: String) = byCustom(name, { it }, { it!! })
    fun byNotNull(name: String) = byCustom(name, { it ?: "" }, { it }, false)


    val byInt get() = byCustom(null, { it?.toInt() }, { it!!.toString() })
    val byIntNotNull get() = byCustom(null, { it?.toInt() ?: 0 }, { it.toString() }, false)

    fun byInt(name: String) = byCustom(name, { it?.toInt() }, { it!!.toString() })
    fun byIntNotNull(name: String) = byCustom(name, { it?.toInt() ?: 0 }, { it.toString() }, false)

    val byDouble get() = byCustom(null, { it?.toDouble() }, { it!!.toString() })
    val byDoubleNotNull get() = byCustom(null, { it?.toDouble() ?: 0.0 }, { it.toString() }, false)

    fun byDouble(name: String) = byCustom(name, { it?.toDouble() }, { it!!.toString() })
    fun byDoubleNotNull(name: String) = byCustom(name, { it?.toDouble() ?: 0.0 }, { it.toString() }, false)

    val byNumber get() = byCustom<Number?>(null, { it?.toDouble() }, { it!!.toString() })
    val byNumberNotNull get() = byCustom<Number>(null, { it?.toDouble() ?: 0 }, { it.toString() }, false)

    fun byNumber(name: String) = byCustom<Number?>(name, { it?.toDouble() }, { it!!.toString() })
    fun byNumberNotNull(name: String) = byCustom<Number>(name, { it?.toDouble() ?: 0 }, { it.toString() }, false)

    val byBoolean get() = byCustom(null, { it }, { it!! })
    val byBooleanNotNull get() = byCustom(null, { it ?: "" }, { it }, false)

    fun byBoolean(name: String) = byCustom(name, { it?.toBoolean() }, { it!!.toString() })
    fun byBooleanNotNull(name: String) = byCustom(name, { it?.toBoolean() ?: false }, { it.toString() }, false)

    override fun getValue(thisRef: Any, property: KProperty<*>) = getForDelegate(property.name)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        if (value == null)
            removeForDelegate(getPropertyName(property))
        else
            setForDelegate(getPropertyName(property), value)
    }
}

abstract class AttrDelegatableMap : MutableMap<String, String>, AttrDelegatable {
    override fun getForDelegate(key: String): String? = get(key)
    override fun setForDelegate(key: String, value: String) {
        put(key, value)
    }

    override fun removeForDelegate(key: String) {
        remove(key)
    }
}

class AttrDelegate<T> internal constructor(
    val delegatable: AttrDelegatable,
    val name: String?,
    val getTransform: (String?) -> T,
    val setTransform: (T) -> String,
    val removeOnNull: Boolean = true
) : ReadWriteProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T =
        getTransform(delegatable.getForDelegate(name ?: delegatable.getPropertyName(property)))

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if (value == null && removeOnNull)
            delegatable.removeForDelegate(name ?: delegatable.getPropertyName(property))
        else
            delegatable.setForDelegate(name ?: delegatable.getPropertyName(property), setTransform(value))
    }
}