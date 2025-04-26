package org.example.tokenizer

class Tokenizer(
    source: String
) : AbstractTokenizer(
    string = source
) {
    override fun tokenize(): List<Token> {
        while (canPeek()) {
            process()
        }

        return tokens
    }

    private fun process() {
        if (singleConsumes.containsKey(peek())) {
            val type = singleConsumes[peek()]!!
            add(consume(), type)
            return
        }

        for (pair in keywords) {
            val keyword = pair.key
            if (word(keyword) &&
                (!canPeek(keyword.length) || peek(keyword.length).isDelimiter())) {

                consume(keyword.length)
                add(keyword, pair.value)
                return
            }
        }

        if (peek().isDigit()) {
            val buf = StringBuilder()
            var hasDigit = false
            while (canPeek()) {
                if (peek() == '.') {
                    if (hasDigit) {
                        break
                    }
                    hasDigit = true
                    buf.append(consume())
                    continue
                }
                if (!peek().isDigit()) {
                    break
                }
                buf.append(consume())
            }
            add(buf.toString(), TokenType.NUM_LIT)
            return
        }

        if (peek().isLetter()) {
            val buf = StringBuilder()
            while (canPeek() && peek().isLetterOrDigit()) {
                buf.append(consume())
            }
            if (buf.toString() in types) {
                add(buf.toString(), TokenType.TYPE)
                return
            }
            add(buf.toString(), TokenType.IDENTIFIER)
            return
        }

        if (peek().isWhitespace()) {
            consume()
            return
        }

        if (peek() == '"') {
            consume()
            val buf = StringBuilder()
            while (canPeek() && peek() != '"') {
                buf.append(consume())
            }
            add(buf.toString(), TokenType.STRING_LIT)
            consumeOrThrow('"', "Expected '\"'")
            return
        }

        add(consume(), TokenType.UNKNOWN)
    }

    companion object {
        val singleConsumes = mapOf<Char, TokenType>(
            '(' to TokenType.OPEN_PAREN,
            ')' to TokenType.CLOSE_PAREN,
            '{' to TokenType.OPEN_CURLY,
            '}' to TokenType.CLOSE_CURLY,
            '+' to TokenType.PLUS,
            ';' to TokenType.SEMI_COLON,
            '=' to TokenType.EQUALS,
            '<' to TokenType.LESS_THAN,
            '>' to TokenType.GREATER_THAN,
            '!' to TokenType.EXCLAM,
            ':' to TokenType.COLON,
            ',' to TokenType.COMMA,
            '&' to TokenType.AMPERSAND,
        )
        val keywords = mapOf<String, TokenType>(
            "for" to TokenType.K_FOR,
            "raw" to TokenType.K_RAW,
            "log" to TokenType.K_LOG,
            "fun" to TokenType.K_FUN,
            "return" to TokenType.K_RETURN,
            "inline" to TokenType.K_INLINE,
        )
        val types = setOf<String>(
            "num", "vec", "string", "text"
        )
        fun Char.isDelimiter() = singleConsumes.containsKey(this) || isWhitespace()

    }
}