package org.example.parser

import org.example.tree.Program
import org.example.tree.Statement

class Parser(
    program: Program
) : AbstractParser(
    program = program
) {
    data class Scope(
        val identTypes: MutableMap<String, String> = mutableMapOf()
    )
    data class State(
        val scopes: MutableMap<String, Parser.Scope> = mutableMapOf(),
        var currScope: String = "\$main",
    )

    var state = State()

    override fun parse(): String {
        state.scopes.put("\$main", Scope())
        while (canPeek()) {
            process(consume())
        }
        return output.toString()
    }

    private fun process(statement: Statement) {

    }
}