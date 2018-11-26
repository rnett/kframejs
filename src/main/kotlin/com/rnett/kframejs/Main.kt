package com.rnett.kframejs

import com.rnett.kframejs.dom.div
import com.rnett.kframejs.dom.p
import com.rnett.kframejs.dom.title
import com.rnett.kframejs.structure.Body
import com.rnett.kframejs.structure.Head
import com.rnett.kframejs.structure.styles.Color
import com.rnett.kframejs.structure.styles.percent

inline fun main(args: Array<String>) {
    console.log("Hello World!")

    page {
        Body.div {
            style {
                display = "block"
                padding = "30px"
                marginLeft = 20.percent
                marginTop = 20.percent

                backgroundColor = Color.Orange
            }
            p("Hello World")
        }
        Head.title {
            +"KFrame JS Test"
        }
    }
}
