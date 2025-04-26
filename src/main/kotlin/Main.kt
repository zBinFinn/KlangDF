package org.example

import java.io.File

fun main() {
    val code = File("src/main/resources/examples/me_when_I_loop.df").readText()

    val compiler = Compiler(code)
    val compiled = compiler.compile()
}

