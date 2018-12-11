package com.rnett.kframejs

import com.rnett.kframejs.dom.*
import com.rnett.kframejs.structure.*
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

            /*p().bound( {style.backgroundColor}.binding() ) {
                +parent<StandardDisplayElement>().style.backgroundColor.toString()
                console.log("Page Update")
            }*/
            val color = p()
            //TODO shouldn't work b/c it is on para
            watch(binding {
                bind(style.backgroundColor)
            }) {
                color.innerHTML = style.backgroundColor.toString()
            }

            bindAll({ style.backgroundColor }.binding()) {
                val s = style.backgroundColor.toString()
                p {
                    +"1: "
                    +s
                }
                div {
                    p {
                        +"2: "
                        +s
                    }
                }
            }
        }
        head.title {
            +"KFrame JS Test"
        }
    }
}
