package dev.ell.koogbasicagent

import ai.koog.agents.core.tools.Tool
import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.serialization.typeToken
import kotlinx.serialization.Serializable

@Serializable
data class MathArgs(
    @param:LLMDescription("The first number for the operation")
    val firstNumber: Int,
    @param:LLMDescription("The second number for the operation")
    val secondNumber: Int
)

object MathsTool {
    val add = object : Tool<MathArgs, Int>(
        argsType = typeToken<MathArgs>(),
        resultType = typeToken<Int>(),
        name = "add",
        description = "Adds two numbers together and returns the sum."
    ) {

        override suspend fun execute(args: MathArgs): Int {

            println("Adding ${args.firstNumber} and ${args.secondNumber}")
            return args.firstNumber + args.secondNumber
        }
    }

    val multiply = object : Tool<MathArgs, Int>(
        argsType = typeToken<MathArgs>(),
        resultType = typeToken<Int>(),
        name = "multiply",
        description = "Multiplies two numbers together and returns the product."
    ) {
        override suspend fun execute(args: MathArgs): Int{

            println("Multiplying ${args.firstNumber} and ${args.secondNumber}")

            return args.firstNumber * args.secondNumber
        }
    }

    val all = listOf(add, multiply)
}
