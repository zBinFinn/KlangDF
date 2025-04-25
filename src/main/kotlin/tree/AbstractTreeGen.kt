package org.example.tree

import org.example.CompileException
import org.example.tokenizer.Token
import org.example.tokenizer.TokenType
import org.example.util.Reader

abstract class AbstractTreeGen(
    private val tokens: List<Token>
) : Reader<Token>() {
    override fun getValue(index: Int) = tokens[index]
    override fun hasIndex(index: Int) = index < tokens.size

    abstract fun gen(): Program
    protected fun expectAndConsume(type: TokenType): Token {
        if (!canPeek()) throw CompileException("Expected $type but got EOF")
        if (peek().type != type) throw CompileException("Expected $type but got ${peek()}")
        return consume()
    }
}