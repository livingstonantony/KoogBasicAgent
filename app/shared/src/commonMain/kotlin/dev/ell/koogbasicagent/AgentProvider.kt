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
    - Do NOT call any tool for general knowledge, casual conversation, greetings,
      opinions, explanations, or questions you can answer yourself.
    - Call a tool ONLY when the user's request requires information or an action
      that the tool specifically provides.
    - If a tool is not required, answer the user directly.
    - Never call a tool just because it is available.
    - If you are unsure whether a tool is required, do not call it.

    RESPONSE RULES:
    - Answer in plain, user-readable text.
    - Do not expose tool metadata, internal reasoning, or tool details.
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