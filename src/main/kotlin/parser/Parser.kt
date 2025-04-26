package org.example.parser

import org.example.tree.Expression
import org.example.tree.FunctionStatement
import org.example.tree.NumberLiteral
import org.example.tree.Parameter
import org.example.tree.Program
import org.example.tree.RawCodeStatement
import org.example.tree.Statement
import org.example.tree.StringLiteral

class Parser(
    program: Program
) : AbstractParser(
    program = program
) {
    data class Scope(
        val function: FunctionStatement,
        val identTypes: MutableMap<String, String> = mutableMapOf()
    )
    data class State(
        val scopes: MutableMap<String, Parser.Scope> = mutableMapOf(),
        var currScope: String = "\$main",
    )

    var state = State()

    override fun parse(): String {
        while (canPeek()) {
            process(consume())
        }
        return getOut()
    }

    private fun process(statement: Statement) {
        if (statement is FunctionStatement) {
            state.scopes.put(statement.identifier, Scope(statement))
            for (param in statement.parameters) {
                state.scopes[statement.identifier]?.identTypes?.put(param.identifier, param.type)
            }

            processFunction(statement)
            return
        }
    }

    private fun processFunction(function: FunctionStatement) {
        if (function.inline) return
        addLine(function.parse())
        for (statement in function.statements) {
            addLine(statement.parse())
        }
        addLine()
    }
    private fun Expression.evaluate() =
        "[Failed to evaluate expression]"

    private fun StringLiteral.evaluate() =
        "STRING(${this.value})"

    private fun NumberLiteral.evaluate() =
        "NUMBER(${this.value})"

    private fun Parameter.parse(): String {
        return "PARAM(${this.identifier}, ${
            if (this.reference) "var"
            else this.type
        })"
    }
    private fun RawCodeStatement.parse(): String {
        addLine(this.expression.evaluate())
        return ""
    }
    private fun FunctionStatement.parse(): String {
        var out = "FN '${this.identifier}' ("
        var notFirst = false
        for (param in this.parameters) {
            if (notFirst) {
                out += ", "
            }
            out += param.parse()
            notFirst = true
        }
        out += ")"
        return out
    }
    private fun Statement.parse(): String {
        return "[What The Hell]"
    }
}