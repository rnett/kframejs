package com.rnett.kframejs.structure

interface IDisplayElement<U : W3Element> : ICanHaveElement<U>
interface IMetaElement<U : W3Element> : ICanHaveElement<U>

typealias AnyDisplayElement = IDisplayElement<*>
typealias AnyMetaElement = IMetaElement<*>