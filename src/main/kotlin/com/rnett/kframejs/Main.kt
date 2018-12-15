package com.rnett.kframejs

import com.rnett.kframejs.dom.*
import com.rnett.kframejs.structure.DisplayElement
import com.rnett.kframejs.structure.DisplayView
import com.rnett.kframejs.structure.page
import com.rnett.kframejs.structure.styles.Color
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable

/*TODO List:
    inputs (check numeric)
    script via lambda?

    positioning of bindings doesn't work

    transaction{ } style multiple style/attribute editing

    site/multipage stuff
*/

/**
 * Notes:
 * Bound elements will be re-added as the last element in their parent on refresh
 * Wrap them in something TODO write helper for div, span wrappers
 */

data class Box<T>(var value: T)

data class Tester(val text: String) : DisplayView {
    override fun DisplayElement<*, *>.display() {
        p {
            +myText
        }
    }

    val myText = "$text!!!"
}

@Serializable
data class Test2(val value: Int) {
    fun valueFunc() = value
}

fun listTest(vararg v: Int) = v.map { Test2(it) }

@ImplicitReflectionSerializer
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

            p("Generic Input test")

            val test2 = Box(Tester("Hello"))

            input(test2::value, { Tester(it) }, { it.text })

            +test2::value

            br

            div {
                p {
                    +"Known: "
                    +{ test2.value.myText }
                }
            }

        }
        head.title {
            +"KFrame JS Test"
        }
    }
}
