package org.example.tree

import org.example.tokenizer.Token
import org.example.tokenizer.TokenType
import kotlin.math.exp

class TreeGen (
    tokens: List<Token>
) : AbstractTreeGen(
    tokens = tokens
) {
    private val statements: List<Statement> = arrayListOf()
    override fun gen(): Program {
        return parseProgram()
    }

    private fun parseProgram(): Program {
        val statements = mutableListOf<Statement>()
        while (canPeek()) {
            statements.add(parseStatement())
        }
        return Program(statements)
    }

    private fun parseStatement(): Statement {
        return when(peek().type) {
            TokenType.TYPE -> parseCreateVarStatement()
            TokenType.IDENTIFIER -> {
                if (canPeek(2) && peek(1).type == TokenType.EQUALS) {
                    return parseReAssignStatement()
                }
                if (canPeek(2) && peek(1).type == TokenType.OPEN_PAREN) {
                    return parseCallFunctionStatement()
                }
                return SetVariableStatement("Error", NumberLiteral(5.0))
            }
            TokenType.K_LOG -> parseLogStatement()
            TokenType.K_FUN -> parseFunctionStatement()
            TokenType.K_RETURN -> parseReturnStatement()
            TokenType.K_INLINE -> {
                expectAndConsume(TokenType.K_INLINE)
                parseFunctionStatement(true)
            }
            TokenType.K_RAW -> parseRawStatement()
            else -> parseExpressionStatement()
        }
    }

    private fun parseCallFunctionStatement(): CallFunctionStatement {
        val identifier = expectAndConsume(TokenType.IDENTIFIER).lexme
        expectAndConsume(TokenType.OPEN_PAREN)
        var beyondFirstArg = false
        val expressions = mutableListOf<Expression>()
        while (canPeek() && peek().type != TokenType.CLOSE_PAREN) {
            if (beyondFirstArg) {
                expectAndConsume(TokenType.COMMA)
            }
            beyondFirstArg = true
            expressions.add(parseExpression())
        }
        expectAndConsume(TokenType.CLOSE_PAREN)
        expectAndConsume(TokenType.SEMI_COLON)
        return CallFunctionStatement(identifier, expressions)
    }
    private fun parseRawStatement(): RawCodeStatement {
        expectAndConsume(TokenType.K_RAW)
        val statement = RawCodeStatement(parseExpression())
        expectAndConsume(TokenType.SEMI_COLON)
        return statement
    }
    private fun parseReturnStatement(): ReturnStatement {
        expectAndConsume(TokenType.K_RETURN)
        return ReturnStatement()
    }
    private fun parseFunctionStatement(isInline: Boolean = false): FunctionStatement {
        expectAndConsume(TokenType.K_FUN)
        val identifier = expectAndConsume(TokenType.IDENTIFIER).lexme
        expectAndConsume(TokenType.OPEN_PAREN)

        val params = mutableListOf<Parameter>()
        var postFirstParam = false
        while(canPeek() && peek().type != TokenType.CLOSE_PAREN) {
            if (postFirstParam) expectAndConsume(TokenType.COMMA)
            val isRef = if (peek().type == TokenType.AMPERSAND) {
                consume()
                true
            } else false
            val type = expectAndConsume(TokenType.TYPE).lexme
            val identifier = expectAndConsume(TokenType.IDENTIFIER).lexme
            params.add(Parameter(identifier = identifier, type = type, reference = isRef))
            postFirstParam = true
        }

        expectAndConsume(TokenType.CLOSE_PAREN)
        expectAndConsume(TokenType.OPEN_CURLY)
        val statements = mutableListOf<Statement>()
        while(canPeek()) {
            if (peek().type == TokenType.CLOSE_CURLY) {
                break
            }
            val statement = parseStatement()
            statements.add(statement)
        }
        expectAndConsume(TokenType.CLOSE_CURLY)
        return FunctionStatement(identifier = identifier, parameters = params, statements = statements,
                                inline = isInline)
    }

    private fun parseReAssignStatement(): SetVariableStatement {
        val identifier = expectAndConsume(TokenType.IDENTIFIER).lexme
        expectAndConsume(TokenType.EQUALS)
        val expression = parseExpression()
        expectAndConsume(TokenType.SEMI_COLON)
        return SetVariableStatement(identifier, expression)
    }

    private fun parseLogStatement(): LogStatement {
        expectAndConsume(TokenType.K_LOG)
        expectAndConsume(TokenType.OPEN_PAREN)
        val expression = parseExpression()
        expectAndConsume(TokenType.CLOSE_PAREN)
        expectAndConsume(TokenType.SEMI_COLON)
        return LogStatement(expression)
    }

    private fun parseCreateVarStatement(): CreateVariableStatement {
        val type = expectAndConsume(TokenType.TYPE).lexme
        val ident = expectAndConsume(TokenType.IDENTIFIER).lexme
        expectAndConsume(TokenType.EQUALS)
        val expression = parseExpression()
        expectAndConsume(TokenType.SEMI_COLON)
        return CreateVariableStatement(type, ident, expression)
    }

    private fun parseExpressionStatement(): Statement {
        val expression = parseExpression()
        expectAndConsume(TokenType.SEMI_COLON)
        return ExpressionStatement(expression)
    }

    private fun parseExpression(): Expression {
        var expression = parsePrimary()
        while (peek().type == TokenType.PLUS || peek().type == TokenType.MINUS) {
            val op = consume().lexme
            val right = parsePrimary()
            expression = BinaryExpression(expression, op, right)
        }

        return expression
    }

    private fun parsePrimary(): Expression {
        val token = consume()
        return when (token.type) {
            TokenType.IDENTIFIER -> Identifier(token.lexme)
            TokenType.NUM_LIT -> NumberLiteral(token.lexme.toDouble())
            TokenType.STRING_LIT -> StringLiteral(token.lexme)
            else -> error("Unexpected token: $token")
        }
    }
}