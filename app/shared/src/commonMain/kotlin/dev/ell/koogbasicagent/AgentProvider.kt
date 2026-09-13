package dev.ell.koogbasicagent

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.features.eventHandler.feature.EventHandler
import ai.koog.prompt.executor.ollama.client.OllamaModels


class AgentProvider {
    fun provideAgent(): AIAgent<String, String> {

        val toolRegistry = ToolRegistry {
            tools(MathsTool.all)
        }

        val agent = AIAgent(
            toolRegistry = toolRegistry,
            promptExecutor = getPlatform().promptExecutor,
            llmModel = OllamaModels.Meta.LLAMA_3_2_3B,
            systemPrompt = """
    You are a helpful assistant.

    TOOL USAGE RULES:
    - You MUST use the `add_numbers` tool for any addition and the `multiply_numbers` tool for any multiplication.
    - Do NOT calculate these yourself. Use the tools to ensure accuracy.
    - If a tool is not relevant to the request, answer the user directly.
    - Do not expose tool metadata, internal reasoning, or tool details in your final response.
            """.trimIndent()
        ) {
            install(EventHandler) {
                onToolCallStarting { ctx ->
                    println(">> Calling tool: ${ctx.toolName} with args ${ctx.toolArgs}")
                }
            }
        }

        return agent

    }
}