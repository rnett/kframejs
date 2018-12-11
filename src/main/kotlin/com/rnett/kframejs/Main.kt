package com.rnett.kframejs

import com.rnett.kframejs.dom.*
import com.rnett.kframejs.structure.*
import com.rnett.kframejs.structure.styles.Color

/*TODO List:
    better syntax for jquery selectors (and test them)
    some differentiation between or and and (which is default?)  anyClass/allClasses, etc?
    data bound text
    script via lambda?

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

                    `$` { id("test").child().tag("div") }() { style.backgroundColor = Color.Green }

                }
            }

            /*p().bound( {style.backgroundColor}.binding() ) {
                +parent<StandardDisplayElement>().style.backgroundColor.toString()
                console.log("Page Update")
            }*/
            val color = p()

            binding { style.backgroundColor } watch {
                color.innerHTML = style.backgroundColor.toString()
            }

            binding { style.backgroundColor } bindAll {
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
