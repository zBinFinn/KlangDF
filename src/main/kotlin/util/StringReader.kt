package org.example.util

open class StringReader(
    private val string: String
) : Reader<Char>() {
    override fun hasIndex(index: Int) = index < string.length
    override fun getValue(index: Int) = string[index]

    protected fun word(word: String) = string.substring(index()).startsWith(word)
}