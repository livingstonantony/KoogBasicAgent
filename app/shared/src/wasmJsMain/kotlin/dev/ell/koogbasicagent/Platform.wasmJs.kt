package dev.ell.koogbasicagent

import ai.koog.http.client.ktor.KtorKoogHttpClient
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.executor.model.PromptExecutor

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val promptExecutor: PromptExecutor = simpleOllamaAIExecutor("http://localhost:11434",httpClientFactory = KtorKoogHttpClient.Factory())

}

actual fun getPlatform(): Platform = WasmPlatform()