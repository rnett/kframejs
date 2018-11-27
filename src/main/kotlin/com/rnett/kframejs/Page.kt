package com.rnett.kframejs

import com.rnett.kframejs.structure.KFrameElementDSL
import kotlin.browser.window
import org.w3c.dom.Element as W3Element

@KFrameElementDSL
inline fun page(crossinline builder: () -> Unit) {
    if (window.onload != null)
        window.onload = {
            window.onload!!(it)
            builder()
        }
    else
        window.onload = {
            builder()
        }
}

class Page {

}