package com.rnett.kframejs.dom.classes

import com.rnett.kframejs.structure.element.CanHaveElement
import com.rnett.kframejs.structure.element.DisplayElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLTableElement
import org.w3c.dom.HTMLTableRowElement

class Table(parent: CanHaveElement<*>) : DisplayElement<Table, HTMLTableElement>("table", parent)

abstract class TableElement<T : TableElement<T, U>, U : HTMLElement>(tag: String, parent: CanHaveElement<*>) :
    DisplayElement<T, U>(tag, parent)

class TableSectionElement<U : HTMLElement>(tag: String, parent: CanHaveElement<*>) :
    TableElement<TableSectionElement<U>, U>(tag, parent)

class TableRowElement(parent: CanHaveElement<*>) : TableElement<TableRowElement, HTMLTableRowElement>("tr", parent)
