package com.rnett.kframejs.dom

import com.rnett.kframejs.structure.*
import org.w3c.dom.HTMLScriptElement
import org.w3c.dom.HTMLTitleElement

@KFrameElementDSL
inline fun <T> T.title(crossinline builder: StandardMetaElementBuilder<HTMLTitleElement> = {})
        where T : AnyMetaElement, T : CanHaveElement<*> =
    StandardMetaElement<HTMLTitleElement>("title", this).invoke {
        builder()
    }

@KFrameElementDSL
inline fun <T> T.script(klass: String = "", crossinline builder: StandardMetaElementBuilder<HTMLScriptElement> = {})
        where T : AnyMetaElement, T : CanHaveElement<*> =
    StandardMetaElement<HTMLScriptElement>("script", this).invoke {
        this.klass = klass
        builder()
    }
