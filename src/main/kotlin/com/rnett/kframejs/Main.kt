package com.rnett.kframejs

import com.rnett.kframejs.dom.button
import com.rnett.kframejs.dom.div
import com.rnett.kframejs.dom.input.booleanInput
import com.rnett.kframejs.dom.p
import com.rnett.kframejs.structure.element.page

/*TODO List:
    inputs (check numeric)
    timer/time offset
    `
    positioning of bindings doesn't work (add wrapper helper?)
    `
    transaction{ } style multiple style/attribute editing
    `
    https://github.com/eriwen/gradle-js-plugin
*/

/**
 * Notes:
 * Bound elements will be re-added as the last element in their parent on refresh
 * Wrap them in something TODO write helper for div, span wrappers
 */

data class Box<T>(var value: T)

fun main(args: Array<String>) {

    page {
        body {
            div {
                val data = Box(true)

                booleanInput(data::value)
                +"Checkbox"

                p {
                    +{
                        if (data.value)
                            "Checked"
                        else
                            "Unchecked"
                    }
                }
                button {
                    +"Switch"
                    on.click {
                        data.value = !data.value
                    }
                }
            }
        }
    }
}

