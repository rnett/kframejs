package com.rnett.kframejs.structure

interface View<in E : AnyElement> {
    fun E.display()
}

interface DisplayView : View<DisplayElement<*, *>>