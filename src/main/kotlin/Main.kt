package org.example

fun main() {
    val code = """
        fun sum(num num1, num num2): num {
            return num1 + num2
        }
    """.trimIndent()

    val compiler = Compiler(code)
    val compiled = compiler.compile()
}

