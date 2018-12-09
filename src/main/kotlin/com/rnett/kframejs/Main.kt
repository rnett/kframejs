package com.rnett.kframejs

import com.rnett.kframejs.dom.*
import com.rnett.kframejs.structure.StandardDisplayElement
import com.rnett.kframejs.structure.binding
import com.rnett.kframejs.structure.page
import com.rnett.kframejs.structure.styles.Color

/*TODO List:
    data
    better handling of dom navigation
    script via lambda?

    site/multipage stuff
*/

fun main(args: Array<String>) {
    console.log("Hello World!")

    page {
        var outer = body.div {
            style {
                display = "block"
                padding = "30px"
            }
            val para = p("Hello World")
            br(2)
            button {
                +"Click Me!"
                on.click {
                    parent<StandardDisplayElement>().style {
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
            p().bound(
                binding {
                    bind(para.style.backgroundColor.toString())
                }) {
                +parent<StandardDisplayElement>().style.backgroundColor.toString()
                console.log("Page Update")
            }
        }
        head.title {
            +"KFrame JS Test"
        }
    }
}
