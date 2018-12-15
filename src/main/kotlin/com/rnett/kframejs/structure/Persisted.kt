package com.rnett.kframejs.structure

import kotlinx.serialization.KSerializer
import kotlinx.serialization.cbor.CBOR
import kotlinx.serialization.dumps
import kotlinx.serialization.loads
import kotlin.browser.localStorage
import kotlin.browser.sessionStorage
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import org.w3c.dom.Storage as RawJsStorage

typealias JsStorage = RawJsStorage

class StorageException(message: String) : RuntimeException(message)

sealed class Storage(val underlying: JsStorage) {

    private val serializers: MutableMap<String, KSerializer<*>> = mutableMapOf()

    fun <R> setSerializer(key: String, serializer: KSerializer<R>) {
        serializers[key] = serializer
    }

    operator fun <R> set(key: String, serializer: KSerializer<R>) = setSerializer(key, serializer)

    fun <R> getSerializer(key: String) = serializers.get(key) as? KSerializer<R>

    operator fun <R : Any> get(key: String, serializer: KSerializer<R>?): R {
        if (serializer != null)
            setSerializer(key, serializer)
        return this[key]
    }


    operator fun <R : Any> get(key: String): R =
        underlying.getItem(key)?.let {
            CBOR.plain.loads(
                getSerializer<R>(key) ?: throw StorageException("No Serializer for $key"),
                it
            )
        } ?: throw StorageException("Key $key is not present in storage")

    fun <R : Any> getOrNull(key: String): R? = try {
        get(key)
    } catch (se: StorageException) {
        null
    }

    operator fun <R : Any> set(key: String, value: R) {
        underlying.setItem(
            key, CBOR.plain.dumps(
                getSerializer(key) ?: throw StorageException("No Serializer for $key"),
                value
            )
        )
    }

    operator fun <R : Any> set(key: String, value: R, serializer: KSerializer<R>?) {
        if (serializer != null)
            setSerializer(key, serializer)
        this[key] = value
    }

    fun remove(key: String) = underlying.removeItem(key)

    operator fun <R : Any> getValue(thisRef: Any?, property: KProperty<*>): R = this[property.name]
    operator fun <R : Any> setValue(thisRef: Any?, property: KProperty<*>, value: R) = set(property.name, value)

    inner class Delegate<T : Any> internal constructor(val serializer: KSerializer<T>?, val name: String? = null) :
        ReadWriteProperty<Any?, T> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return get(name ?: property.name, serializer)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            set(name ?: property.name, value, serializer)
        }
    }

    fun <T : Any> by(name: String) = Delegate<T>(null, name)
    fun <T : Any> by(serializer: KSerializer<T>) = Delegate(serializer, null)
    fun <T : Any> by(name: String, serializer: KSerializer<T>) = Delegate<T>(serializer, name)
}


object LocalStorage : Storage(localStorage)


object SessionStorage : Storage(sessionStorage)
