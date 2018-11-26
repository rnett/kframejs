package com.rnett.kframejs

import com.rnett.kframejs.structure.KFrameElementDSL
import kotlin.browser.window

@KFrameElementDSL
fun page(builder: () -> Unit) {
    if (window.onload != null)
        window.onload = {
            window.onload!!(it)
            builder()
        }
    else
        window.onload = {
            builder
        }
}