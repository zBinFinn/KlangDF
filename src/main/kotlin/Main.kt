package org.example

fun main() {
    val code = """
        num i = 3;
        log(i);
        i = 5 + 3;
        log(i);
    """.trimIndent()

    val compiler = Compiler(code)
    val compiled = compiler.compile()
}

