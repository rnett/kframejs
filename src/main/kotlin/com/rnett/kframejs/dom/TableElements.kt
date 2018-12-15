package com.rnett.kframejs.dom

import com.rnett.kframejs.dom.classes.Table
import com.rnett.kframejs.dom.classes.TableElement
import com.rnett.kframejs.dom.classes.TableRowElement
import com.rnett.kframejs.dom.classes.TableSectionElement
import com.rnett.kframejs.structure.*
import org.w3c.dom.HTMLTableCellElement
import org.w3c.dom.HTMLTableSectionElement
import kotlin.reflect.KProperty0

@KFrameElementDSL
inline fun <ParentType> ParentType.table(
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<Table> = {}
)
        where ParentType : AnyDisplayElement, ParentType : CanHaveElement<*> =
    Table(this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }

@KFrameElementDSL
inline fun Table.tbody(
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<TableSectionElement<HTMLTableSectionElement>> = {}
) =
    TableSectionElement<HTMLTableSectionElement>("tbody", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }

@KFrameElementDSL
inline fun Table.thead(
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<TableSectionElement<HTMLTableSectionElement>> = {}
) =
    TableSectionElement<HTMLTableSectionElement>("thead", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }

@KFrameElementDSL
inline fun Table.tfoot(
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<TableSectionElement<HTMLTableSectionElement>> = {}
) =
    TableSectionElement<HTMLTableSectionElement>("tfoot", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        underlying.align
        builder()
    }

@KFrameElementDSL
inline fun <ParentType : TableElement<*, *>> ParentType.tr(
    klass: String = "",
    id: String = "",
    crossinline builder: ElementBuilder<TableRowElement> = {}
) =
    TableRowElement(this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }

@KFrameElementDSL
inline fun TableRowElement.td(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLTableCellElement> = {}
) =
    StandardDisplayElement<HTMLTableCellElement>("td", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }

@KFrameElementDSL
inline fun TableRowElement.th(
    klass: String = "",
    id: String = "",
    crossinline builder: StandardDisplayElementBuilder<HTMLTableCellElement> = {}
) =
    StandardDisplayElement<HTMLTableCellElement>("th", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        builder()
    }

@KFrameElementDSL
inline fun TableRowElement.tdRow(
    vararg values: String, klass: String = "", id: String = "", tdKlass: String = "",
    crossinline tdBuilder: StandardDisplayElement<HTMLTableCellElement>.(String) -> Unit = {},
    crossinline builder: ElementBuilder<TableRowElement> = {}
) = tr(klass, id) {
    values.forEach {
        td(tdKlass) {
            +it
            tdBuilder(it)
        }
    }
    builder()
}

@KFrameElementDSL
inline fun TableRowElement.tdRow(
    vararg values: () -> String, klass: String = "", id: String = "", tdKlass: String = "",
    crossinline tdBuilder: StandardDisplayElement<HTMLTableCellElement>.(String) -> Unit = {},
    crossinline builder: ElementBuilder<TableRowElement> = {}
) = tr(klass, id) {
    values.forEach {
        td(tdKlass) {
            +it
            tdBuilder(it())
        }
    }
    builder()
}

@KFrameElementDSL
inline fun <T> TableRowElement.tdRow(
    vararg values: T, klass: String = "", id: String = "", tdKlass: String = "",
    crossinline builder: ElementBuilder<TableRowElement> = {},
    crossinline tdBuilder: StandardDisplayElement<HTMLTableCellElement>.(T) -> Unit
) = tr(klass, id) {
    values.forEach {
        td(tdKlass) {
            tdBuilder(it)
        }
    }
    builder()
}

@KFrameElementDSL
inline fun <T> TableRowElement.tdRow(
    vararg values: KProperty0<T>, klass: String = "", id: String = "", tdKlass: String = "",
    crossinline builder: ElementBuilder<TableRowElement> = {},
    crossinline tdBuilder: StandardDisplayElement<HTMLTableCellElement>.(T) -> Unit
) = tr(klass, id) {
    values.forEach {
        td(tdKlass).bound(it.binding()) {
            tdBuilder(it)
        }
    }
    builder()
}

@KFrameElementDSL
inline fun <T : DisplayView> TableRowElement.tdRow(
    vararg values: T, klass: String = "", id: String = "", tdKlass: String = "", dontBind: Boolean = false,
    crossinline tdBuilder: StandardDisplayElement<HTMLTableCellElement>.(T) -> Unit = {},
    crossinline builder: ElementBuilder<TableRowElement> = {}
) = tr(klass, id) {
    values.forEach {
        td(tdKlass) {
            if (dontBind)
                -it
            else
                +it
            tdBuilder(it)
        }
    }
    builder()
}

@KFrameElementDSL
inline fun <T : DisplayView> TableRowElement.tdRow(
    vararg values: KProperty0<T>, klass: String = "", id: String = "", tdKlass: String = "",
    crossinline tdBuilder: StandardDisplayElement<HTMLTableCellElement>.(T) -> Unit = {},
    crossinline builder: ElementBuilder<TableRowElement> = {}
) = tr(klass, id) {
    values.forEach {
        td(tdKlass) {
            +it
            tdBuilder(it.get())
        }
    }
    builder()
}


@KFrameElementDSL
inline fun TableRowElement.thRow(
    vararg values: String, klass: String = "", id: String = "", thKlass: String = "",
    crossinline thBuilder: StandardDisplayElement<HTMLTableCellElement>.(String) -> Unit = {},
    crossinline builder: ElementBuilder<TableRowElement> = {}
) = tr(klass, id) {
    values.forEach {
        th(thKlass) {
            +it
            thBuilder(it)
        }
    }
    builder()
}

@KFrameElementDSL
inline fun TableRowElement.thRow(
    vararg values: () -> String, klass: String = "", id: String = "", thKlass: String = "",
    crossinline thBuilder: StandardDisplayElement<HTMLTableCellElement>.(String) -> Unit = {},
    crossinline builder: ElementBuilder<TableRowElement> = {}
) = tr(klass, id) {
    values.forEach {
        th(thKlass) {
            +it
            thBuilder(it())
        }
    }
    builder()
}

@KFrameElementDSL
inline fun <T> TableRowElement.thRow(
    vararg values: T, klass: String = "", id: String = "", thKlass: String = "",
    crossinline builder: ElementBuilder<TableRowElement> = {},
    crossinline thBuilder: StandardDisplayElement<HTMLTableCellElement>.(T) -> Unit
) = tr(klass, id) {
    values.forEach {
        th(thKlass) {
            thBuilder(it)
        }
    }
    builder()
}

@KFrameElementDSL
inline fun <T> TableRowElement.thRow(
    vararg values: KProperty0<T>, klass: String = "", id: String = "", thKlass: String = "",
    crossinline builder: ElementBuilder<TableRowElement> = {},
    crossinline thBuilder: StandardDisplayElement<HTMLTableCellElement>.(T) -> Unit
) = tr(klass, id) {
    values.forEach {
        th(thKlass).bound(it.binding()) {
            thBuilder(it)
        }
    }
    builder()
}

@KFrameElementDSL
inline fun <T : DisplayView> TableRowElement.thRow(
    vararg values: T, klass: String = "", id: String = "", thKlass: String = "", dontBind: Boolean = false,
    crossinline thBuilder: StandardDisplayElement<HTMLTableCellElement>.(T) -> Unit = {},
    crossinline builder: ElementBuilder<TableRowElement> = {}
) = tr(klass, id) {
    values.forEach {
        th(thKlass) {
            if (dontBind)
                -it
            else
                +it
            thBuilder(it)
        }
    }
    builder()
}

@KFrameElementDSL
inline fun <T : DisplayView> TableRowElement.thRow(
    vararg values: KProperty0<T>, klass: String = "", id: String = "", thKlass: String = "",
    crossinline thBuilder: StandardDisplayElement<HTMLTableCellElement>.(T) -> Unit = {},
    crossinline builder: ElementBuilder<TableRowElement> = {}
) = tr(klass, id) {
    values.forEach {
        th(thKlass) {
            +it
            thBuilder(it.get())
        }
    }
    builder()
}