package com.rnett.kframejs.dom

import com.rnett.kframejs.structure.IMetaElement
import com.rnett.kframejs.structure.KFrameElementDSL
import com.rnett.kframejs.structure.StandardMetaElementBuilder
import com.rnett.kframejs.structure.metaElement

@KFrameElementDSL
inline fun IMetaElement.title(klass: String = "", crossinline builder: StandardMetaElementBuilder = {}) =
    metaElement("title").invoke {
        this.klass = klass
        builder()
    }

@KFrameElementDSL
inline fun IMetaElement.script(klass: String = "", crossinline builder: StandardMetaElementBuilder = {}) =
    metaElement("script").invoke {
        this.klass = klass
        builder()
    }

