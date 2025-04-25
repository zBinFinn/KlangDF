package org.example.tree

sealed class ASTNode

data class Program(val statements: List<Statement>) : ASTNode() {
    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("Program:\n")
        var indent: Int
        for (statement in statements) {
            indent = 2
            builder.append(" ".repeat(indent) + statement.toString() + "\n")
        }
        return builder.toString()
    }
}

sealed class Statement : ASTNode()
data class CreateVariableStatement(val type: String, val identifier: String, val expression: Expression?) : Statement()
data class SetVariableStatement(val identifier: String, val expression: Expression) : Statement()
data class ExpressionStatement(val expression: Expression) : Statement()
data class LogStatement(val expression: Expression) : Statement()
data class FunctionStatement(val identifier: String, val parameters: List<Parameter>, val statements: List<Statement>,
                             val inline: Boolean) : Statement()

data class Parameter(val identifier: String, val type: String)

sealed class Expression : ASTNode()
data class Identifier(val name: String) : Expression()
data class NumberLiteral(val value: Double) : Expression()
data class StringLiteral(val value: String) : Expression()
data class BinaryExpression(
    val left: Expression,
    val operator: String,
    val right: Expression
) : Expression()