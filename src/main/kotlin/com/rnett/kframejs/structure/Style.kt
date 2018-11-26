package com.rnett.kframejs.structure

import com.rnett.kframejs.structure.styles.Color
import com.rnett.kframejs.structure.styles.Size

class Style(val element: AnyElement) : AttrDelegatableMap() {

    override fun transformName(name: String): String {
        val regex = Regex("([A-Z])")
        return regex.replace(name.removeSuffix("Raw")) { "-" + it.value.toLowerCase() }
    }

    private var raw: String
        get() = element.underlying.getAttribute("style") ?: ""
        set(v: String) {
            element.underlying.setAttribute("style", v)
        }

    var map: Map<String, String>
        get() {
            return raw.split(";").filter { it.isNotBlank() }
                .associate { it.substringBefore(":").trim() to it.substringAfter(":").trim() }
        }
        set(m) {
            raw = m.entries.joinToString("; ") { it.key + ": " + it.value }
        }

    fun <R> doThenUpdate(op: (MutableMap<String, String>) -> R): R {
        val m = map.toMutableMap()
        val r = op(m)
        map = m
        return r
    }

    override val size: Int get() = map.size

    override fun containsKey(key: String): Boolean = map.containsKey(key)

    override fun containsValue(value: String): Boolean = map.containsValue(value)

    override fun get(key: String): String? = map.get(key)

    override fun isEmpty(): Boolean = map.isEmpty()

    override val entries: MutableSet<MutableMap.MutableEntry<String, String>>
        get() = map.toMutableMap().entries
    override val keys: MutableSet<String>
        get() = map.toMutableMap().keys
    override val values: MutableCollection<String>
        get() = map.toMutableMap().values

    override fun clear() = doThenUpdate {
        it.clear()
    }

    override fun put(key: String, value: String): String? = doThenUpdate {
        it.put(key, value)
    }

    override fun putAll(from: Map<out String, String>) = doThenUpdate {
        it.putAll(from)
    }

    override fun remove(key: String): String? = doThenUpdate {
        it.remove(key)
    }

    val byColor get() = byCustom(null, { it?.let { Color(it) } }, { it!!.css }, true)
    val bySize get() = byCustom(null, { it?.let { Size(it) } }, { it!!.css }, true)

    operator fun invoke(builder: Style.() -> Unit): Style {
        builder()
        return this
    }

    operator fun invoke(vararg styles: Pair<String, String>): Style {
        putAll(styles)
        return this
    }

    // properties

    var border by this

    var borderColor by byColor

    var borderStyle by this

    var borderWidth by bySize

    var borderTop by this

    var borderBottom by this

    var borderRight by this

    var borderLeft by this

    var borderRadius by this

    var borderImage by this


    var borderCollapse by this


    var outline by this

    /*
    var outlineTop by this
	
    var outlineBottom by this
	
    var outlineRight by this
	
    var outlineLeft by this
	
    */

    var outlineOffset by this


    var color by byColor

    var backgroundColor by byColor


    var background by this

    var backgroundImage by this

    var backgroundRepeat by this

    var backgroundPosition by this

    var backgroundAttachment by this

    var backgroundSize by this

    var backgroundOrigin by this

    var backgroundClip by this


    var margin by this

    var marginTop by bySize

    var marginBottom by bySize

    var marginLeft by bySize

    var marginRight by bySize


    var padding by this

    var paddingTop by bySize

    var paddingBottom by bySize

    var paddingLeft by bySize

    var paddingRight by bySize


    var height by bySize

    var minHeight by bySize

    var maxHeight by bySize


    var width by this

    var minWidth by bySize

    var maxWidth by bySize


    var textAlign by this

    var verticalAlign by this

    var textDecoration by this

    var textTransform by this

    var textIndent by this

    var lineHeight by bySize


    var font by this

    var fontFamily by this

    var fontStyle by this

    var fontSize by bySize

    var fontWeight by this

    var fontVariant by this


    var listStyle by this

    var listStyleType by this

    var listStyleImage by this

    var listStylePosition by this


    var tableLayout by this

    var borderSpacing by this

    var captionSide by this

    var emptyCells by this


    var display by this

    var visibility by this


    var position by this

    var clip by this

    var zIndex by this

    var top by bySize

    var bottom by bySize

    var left by bySize

    var right by bySize


    var overflow by this

    var overflowX by this

    var overflowY by this


    var float by this

    @JsName("clearProp")
    var clear by this


    var boxSizing by this


    var opacity by this


    var boxShadow by this

    var textShadow by this


    var counterReset by this

    var counterIncrement by this

    var content by this


    var textOverflow by this

    var wordWrap by this

    var wordBreak by this


    var objectFit by this


    //TODO allow for keyframes (can I do this without a stylesheet?)

    var animation by this

    var animationName by this

    var animationDuration by this

    var animationDelay by this

    var animationIterationCount by this

    var animationDirection by this

    var animationTimingFunction by this

    var animationFillMode by this


    var columns by this

    var columnCount by this

    var columnFill by this

    var columnGap by this

    var columnRuleStyle by this

    var columnRuleWidth by bySize

    var columnRuleColor by byColor

    var columnRule by this

    var columnSpan by this

    var columnWidth by bySize


    var flex by this

    var flexDirection by this

    var flexWrap by this

    var flexFlow by this


    var justifyContent by this

    var alignItems by this

    var alignContent by this

    var order by this

    var alignSelf by this


    var resize by this


    var grid by this

    var gridGap by this

    var gridRowGap by this

    var gridColumnGap by this

    var gridColumnStart by this

    var gridColumnEnd by this

    var gridRowStart by this

    var gridRowEnd by this


    var whiteSpace by this

}