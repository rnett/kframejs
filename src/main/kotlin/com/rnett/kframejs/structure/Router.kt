package com.rnett.kframejs.structure

import kotlin.browser.window

internal data class SubpageInstance(val subpage: Subpage, val data: Map<String, String>) {
    fun build() = subpage.builder(data)
    fun urlString() = "/${subpage.url}" +
            if (data.isNotEmpty())
                "/?${data.entries.joinToString("&") { it.run { "$key=$value" } }}"
            else
                ""
}

object Router {

    internal var _currentPage: SubpageInstance? = null
    private val _pages: MutableMap<String, Subpage> = mutableMapOf()

    val currentPage get() = _currentPage?.subpage
    val pages get() = _pages.toMap()

    fun addPage(name: String, subpage: Subpage) = _pages.put(name, subpage)
    fun addPage(subpage: Subpage) = _pages.put(subpage.name, subpage)

    operator fun set(name: String, subpage: Subpage) = addPage(name, subpage)

    fun goto(subpage: String, data: Map<String, String>): Boolean {
        pages[subpage].let {
            return if (it != null) {
                goto(it, data)
                true
            } else
                false
        }
    }

    fun goto(subpage: String, data: Map<String, Any>) = goto(subpage, data.mapValues { it.value.toString() })
    fun goto(subpage: String, vararg data: Pair<String, Any>) = goto(subpage, data.toMap())

    fun goto(subpage: Subpage, data: Map<String, String>) {
        val si = SubpageInstance(subpage, data)
        window.history.pushState(null, subpage.title(data), si.urlString())
        _currentPage = si
    }

    fun goto(subpage: Subpage, data: Map<String, Any>) = goto(subpage, data.mapValues { it.value.toString() })
    fun goto(subpage: Subpage, vararg data: Pair<String, Any>) = goto(subpage, data.toMap())


    operator fun invoke(subpage: String, data: Map<String, String>) = goto(subpage, data)
    operator fun invoke(subpage: Subpage, data: Map<String, String>) = goto(subpage, data)

    operator fun invoke(subpage: String, data: Map<String, Any>) = goto(subpage, data)
    operator fun invoke(subpage: Subpage, data: Map<String, Any>) = goto(subpage, data)

    operator fun invoke(subpage: String, vararg data: Pair<String, Any>) = goto(subpage, *data)
    operator fun invoke(subpage: Subpage, vararg data: Pair<String, Any>) = goto(subpage, *data)
}

open class Subpage(
    val name: String,
    val title: (Map<String, String>) -> String,
    val url: String,
    val builder: (Map<String, String>) -> Unit
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Subpage) return false

        if (name != other.name) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + url.hashCode()
        return result
    }
}

open class SubpageNoData(name: String, title: String, url: String, builder: () -> Unit) :
    Subpage(name, { title }, url, { builder() })

@KFrameElementDSL
fun subPage(
    name: String,
    url: String,
    title: (Map<String, String>) -> String,
    builder: (Map<String, String>) -> Unit
): Subpage {
    val sp = Subpage(name, title, url, builder)
    Router[name] = sp
    return sp
}

@KFrameElementDSL
fun subPageNoData(name: String, url: String, title: String, builder: () -> Unit): SubpageNoData {
    val sp = SubpageNoData(name, title, url, builder)
    Router[name] = sp
    return sp
}

@KFrameElementDSL
fun <E : AnyElement> E.routerLink(subpage: String): E = also { on.click { Router(subpage) } }

@KFrameElementDSL
fun <E : AnyElement> E.routerLink(subpage: Subpage): E = also { on.click { Router(subpage) } }