package com.rnett.kframejs

import com.rnett.kframejs.dom.*
import com.rnett.kframejs.structure.page
import com.rnett.kframejs.structure.styles.Color

/*TODO List:
    inputs
    script via lambda?
    positioning of bindings doesn't work

    site/multipage stuff
*/

fun main(args: Array<String>) {

    page {
        var outer = body.div {
            id = "test"
            style {
                display = "block"
                padding = "30px"
            }
            div {
                id = "test2"
                div {
                    +"Green Test"
                }
            }
            val para = p("Hello World")
            br(2)
            button {
                +"Click Me!"
                on.click {
                    parent.style {
                        if (backgroundColor == Color.Orange)
                            backgroundColor = Color.Lightblue
                        else
                            backgroundColor = Color.Orange
                    }
                }
                on.click {
                    para.innerHTML += "!"
                }
            }
            br(2)
            button {
                +"Reset !s (Double Click)"
                on.dblclick {
                    para.innerHTML = "Hello World"
                }
            }
            p().bound(style::backgroundColor) {
                +it.toString()
            }


        }
        head.title {
            +"KFrame JS Test"
        }
    }
}
