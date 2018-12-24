package com.rnett.kframejs.dom

import com.rnett.kframejs.structure.element.*
import org.w3c.dom.HTMLScriptElement
import org.w3c.dom.HTMLStyleElement
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

@KFrameElementDSL
inline fun <T> T.script(
    src: String,
    klass: String = "",
    crossinline builder: StandardMetaElementBuilder<HTMLScriptElement> = {}
)
        where T : AnyMetaElement, T : CanHaveElement<*> =
    script {
        attributes[src] = src
        builder()
    }

@KFrameElementDSL
inline fun <ParentType> ParentType.internalStylesheet(
    text: String,
    klass: String = "",
    id: String = "",
    crossinline builder: StandardMetaElementBuilder<HTMLStyleElement> = {}
)
        where ParentType : AnyMetaElement, ParentType : CanHaveElement<*> =
    StandardMetaElement<HTMLStyleElement>("style", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        +text
        builder()
    }


@KFrameElementDSL
inline fun <ParentType> ParentType.externalStylesheet(
    src: String,
    klass: String = "",
    id: String = "",
    crossinline builder: StandardMetaElementBuilder<HTMLStyleElement> = {}
)
        where ParentType : AnyMetaElement, ParentType : CanHaveElement<*> =
    StandardMetaElement<HTMLStyleElement>("link", this).invoke {
        if (klass != "")
            this.klass = klass
        if (id != "")
            this.id = id
        attributes["href"] = src
        attributes["rel"] = "stylesheet"
        attributes["type"] = "text/css"
        builder()
    }