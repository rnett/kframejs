package com.rnett.kframejs.structure.element

interface View<in E : AnyElement> {
    fun E.display()
}

interface DisplayView : View<DisplayElement<*, *>>