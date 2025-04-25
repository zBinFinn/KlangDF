package org.example.tokenizer

import org.example.util.StringReader

abstract class AbstractTokenizer (
    string: String
) : StringReader (
    string = string
) {
    protected val tokens = arrayListOf<Token>()
    protected fun add(token: Token) = tokens.add(token)
    protected fun add(string: String, type: TokenType) = add(Token(string, type))
    protected fun add(char: Char, type: TokenType) = add(char.toString(), type)
    abstract fun tokenize(): List<Token>
}