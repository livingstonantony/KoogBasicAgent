package dev.ell.koogbasicagent

import ai.koog.http.client.ktor.KtorKoogHttpClient
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.executor.model.PromptExecutor
import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val promptExecutor: PromptExecutor = simpleOllamaAIExecutor("http://10.0.2.2:11434",httpClientFactory = KtorKoogHttpClient.Factory())

}

actual fun getPlatform(): Platform = AndroidPlatform()