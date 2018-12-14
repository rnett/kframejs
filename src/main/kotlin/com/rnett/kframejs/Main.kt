package com.rnett.kframejs

import com.rnett.kframejs.dom.*
import com.rnett.kframejs.structure.page
import com.rnett.kframejs.structure.styles.Color

/*TODO List:
    inputs (check numeric)
    use HtmlElement classes as underlying types
    script via lambda?
    positioning of bindings doesn't work

    persisted data
    site/multipage stuff
*/

data class Box<T>(var value: T)

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
            br(2)

            val testText = Box("test")

            stringInput(testText::value)

            p {
                +{ testText.value + " - color: " + parent.style.backgroundColor }
            }


        }
        head.title {
            +"KFrame JS Test"
        }
    }
}
