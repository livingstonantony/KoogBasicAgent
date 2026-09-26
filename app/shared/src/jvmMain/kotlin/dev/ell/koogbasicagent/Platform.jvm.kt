package dev.ell.koogbasicagent

import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.executor.model.PromptExecutor

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val promptExecutor: PromptExecutor = simpleOllamaAIExecutor("http://localhost:11434")

}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun logDebug(tag: String, message: String) {
    println("[$tag] $message")
}