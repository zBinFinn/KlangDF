package org.example.tokenizer

data class Token(
    val lexme: String,
    val type: TokenType,
)

enum class TokenType {
    UNKNOWN,

    /** Keywords **/
    K_FOR, K_RAW, K_LOG,
    K_FUN, K_RETURN,

    SEMI_COLON, COLON, COMMA,

    OPEN_PAREN, CLOSE_PAREN,
    OPEN_CURLY, CLOSE_CURLY,

    LESS_THAN, GREATER_THAN,
    EQUALS,

    EXCLAM, TYPE,
    /** Literals **/
    NUM_LIT, STRING_LIT,
    /** Operators **/
    PLUS, MINUS,

    IDENTIFIER,
}