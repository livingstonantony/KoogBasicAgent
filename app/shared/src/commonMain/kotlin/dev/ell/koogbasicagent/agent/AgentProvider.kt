package dev.ell.koogbasicagent.agent

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.features.eventHandler.feature.EventHandler
import ai.koog.http.client.ktor.KtorKoogHttpClient
import ai.koog.prompt.executor.llms.MultiLLMPromptExecutor
import ai.koog.prompt.executor.ollama.client.OllamaClient
import ai.koog.prompt.executor.ollama.client.OllamaModels
import dev.ell.koogbasicagent.tool.MathsTool
import dev.ell.koogbasicagent.logDebug


class AgentProvider {
    fun provideAgent(): AIAgent<String, String> {

        println("Agent connected")
        logDebug("KoogAgent", "Agent connected")
        val toolRegistry = ToolRegistry {
            tools(MathsTool.all)
        }

        val agent = AIAgent(
            toolRegistry = toolRegistry,
            promptExecutor = MultiLLMPromptExecutor(OllamaClient(httpClientFactory = KtorKoogHttpClient.Factory())),
            llmModel = OllamaModels.Meta.LLAMA_3_2_3B,
            systemPrompt = """
    You are a helpful calculator assistant.

    You must use the registered tools for arithmetic operations.
    Never calculate arithmetic operations yourself.

    Registered tools: ${toolRegistry.tools.joinToString(", ") { it.name }}
""".trimIndent()
        ) {
            install(EventHandler) {
                onToolCallStarting { ctx ->
                    logDebug(
                        "KoogAgent",
                        ">> Calling tool: ${ctx.toolName} with args ${ctx.toolArgs}"
                    )
                }
            }
        }

        return agent

    }
}