package dev.ell.koogbasicagent

import ai.koog.prompt.executor.model.PromptExecutor

interface Platform {
    val name: String
    val promptExecutor: PromptExecutor
}

expect fun getPlatform(): Platform