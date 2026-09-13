package dev.ell.koogbasicagent

import ai.koog.agents.core.tools.Tool
import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.serialization.typeToken
import kotlinx.serialization.Serializable

@Serializable
data class MathArgs(
    @param:LLMDescription("The first number")
    val a: Double,
    @param:LLMDescription("The second number")
    val b: Double
)

object MathsTool {
    val add = object : Tool<MathArgs, Double>(
        argsType = typeToken<MathArgs>(),
        resultType = typeToken<Double>(),
        name = "add_numbers",
        description = "Adds two numbers together. Use this for ANY addition."
    ) {

        override suspend fun execute(args: MathArgs): Double {
            println(">> MathsTool: Adding ${args.a} + ${args.b}")
            return args.a + args.b
        }
    }

    val multiply = object : Tool<MathArgs, Double>(
        argsType = typeToken<MathArgs>(),
        resultType = typeToken<Double>(),
        name = "multiply_numbers",
        description = "Multiplies two numbers together. Use this for ANY multiplication."
    ) {
        override suspend fun execute(args: MathArgs): Double {
            println(">> MathsTool: Multiplying ${args.a} * ${args.b}")
            return args.a * args.b
        }
    }

    val all = listOf(add, multiply)
}
