package com.rnett.kframejs.structure.element.styles

@Suppress("EnumEntryName")
enum class Unit {
    percent, cm, em, rem, ex, `in`, mm, pc, pt, px, vh, vw, vmin, raw;

    val unit = if (name == "raw") "" else if (name == "percent") "%" else name.trim('`')

    companion object {
        val inch = `in`

        operator fun get(value: String) = when (value.trim('s')) {
            "%" -> percent
            "in" -> `in`
            "percent" -> percent
            "inches" -> inch
            "" -> raw
            else -> if (value.isBlank()) raw else values().find { it.unit == value.trim('s') } ?: raw
        }

        operator fun invoke(value: String) = get(value)
    }

    override fun toString(): String = unit
}

class Size(val mag: Number?, val units: Unit) {
    constructor(
        size: String?,
        units: Unit = Unit.raw
    ) : this(size?.dropLastWhile { it.isLetterOrPercent() }?.toDoubleOrNull(), units)

    constructor(sizeAndUnits: String?) : this(
        sizeAndUnits?.dropLastWhile { it.isLetterOrPercent() }?.toDoubleOrNull(),
        Unit[sizeAndUnits?.takeLastWhile { it.isLetterOrPercent() }
            ?: ""])

    val css get() = (mag?.toString()?.plus(units.toString()) ?: "")

    override fun toString(): String = css
}

fun Char.isLetterOrPercent() = this == '%' || (this.toString().match("[a-zA-z]")?.isNotEmpty() ?: false)

val Number.px get() = Size(this, Unit.px)
val Number.rem get() = Size(this, Unit.rem)
val Number.percent get() = Size(this, Unit.percent)
val Number.cm get() = Size(this, Unit.cm)
val Number.em get() = Size(this, Unit.em)
val Number.ex get() = Size(this, Unit.ex)
val Number.inch get() = Size(this, Unit.inch)
val Number.mm get() = Size(this, Unit.mm)
val Number.pc get() = Size(this, Unit.pc)
val Number.pt get() = Size(this, Unit.pt)
val Number.vh get() = Size(this, Unit.vh)
val Number.vw get() = Size(this, Unit.vw)
val Number.vmin get() = Size(this, Unit.vmin)
val Number?.rawSize get() = Size(this, Unit.raw)