package dev.ell.koogbasicagent

import ai.koog.http.client.ktor.KtorKoogHttpClient
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.executor.model.PromptExecutor
import web.navigator.navigator

class JsPlatform: Platform {
    private val userAgent = navigator.userAgent
    private val browserList = listOf("Chrome", "Firefox", "Safari", "Edge")

    override val name: String = userAgent.findAnyOf(browserList, ignoreCase = true)
            ?.let { (startIndex) -> userAgent.substring(startIndex).substringBefore(" ") }
            ?: "Unknown"
    override val promptExecutor: PromptExecutor = simpleOllamaAIExecutor("http://localhost:11434",httpClientFactory = KtorKoogHttpClient.Factory())

}

actual fun getPlatform(): Platform = JsPlatform()