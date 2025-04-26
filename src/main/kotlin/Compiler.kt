package org.example

import org.example.parser.Parser
import org.example.tokenizer.Tokenizer
import org.example.tree.TreeGen

class Compiler(
    private val source: String
) {
    fun compile(): String {
        println("Source Code:")
        println(source)
        println()

        val tokenizer = Tokenizer(source)
        val tokens = tokenizer.tokenize()
        println("Tokenized:")
        tokens.forEach { element ->
            println(element)
        }
        println()

        val treegen = TreeGen(tokens)
        val program = treegen.gen()
        println("AST:")
        println(program)
        println()

        val parser = Parser(program)
        val compiled = parser.parse()
        println("Compiled Code:")
        println(compiled)
        return compiled
    }
}