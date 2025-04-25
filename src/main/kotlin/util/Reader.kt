package org.example.util

import org.example.CompileException

abstract class Reader<T> {
    abstract fun hasIndex(index: Int): Boolean
    abstract fun getValue(index: Int): T

    private var index = 0

    protected fun index() = index
    protected fun canPeek(ahead: Int = 0): Boolean {
        return hasIndex(index + ahead)
    }

    protected fun peek(ahead: Int = 0): T {
        return getValue(index + ahead)
    }

    protected fun consume(): T {
        return getValue(index++)
    }

    protected fun consume(amount: Int): List<T> {
        val out = mutableListOf<T>()
        for (i in 1..amount) {
            out.add(consume())
        }
        return out
    }
    protected fun sequence(seq: List<T>): Boolean {
        for (i in 0..<seq.size) {
            if (!canPeek(i)) return false
            val value: T = peek(i)!!
            val compare: T = seq[i]!!
            when {
                value == null -> return false
                compare == null -> return false
                value != compare -> return false
            }
        }
        return true
    }
    protected fun sequence(vararg seq: T) = sequence(seq.toList())


    protected fun consumeOrThrow(consume: T, err: String): T {
        if (canPeek() && peek() == consume) {
            return consume()
        }
        throw CompileException(err)
    }
}