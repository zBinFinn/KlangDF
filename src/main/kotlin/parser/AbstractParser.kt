package org.example.parser

import org.example.tree.Program
import org.example.tree.Statement
import org.example.util.Reader

abstract class AbstractParser (
    private val program: Program
) : Reader<Statement>() {
    protected val output = StringBuilder()

    override fun getValue(index: Int) = program.statements[index]
    override fun hasIndex(index: Int) = index < program.statements.size

    abstract fun parse(): String
}