package dev.ell.koogbasicagent

import ai.koog.http.client.ktor.KtorKoogHttpClient
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.executor.model.PromptExecutor
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val promptExecutor: PromptExecutor = simpleOllamaAIExecutor("http://YOUR_IP_ADDRESS:11434",httpClientFactory = KtorKoogHttpClient.Factory())

}

actual fun getPlatform(): Platform = IOSPlatform()