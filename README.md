# KoogBasicAgent 🤖

A comprehensive **Kotlin Multiplatform (KMP)** project demonstrating the power of the **Koog AI Framework**. This project showcases how to build and deploy AI agents across Android, iOS, Web (Wasm/JS), Desktop (JVM), and Server environments.

## 🚀 Overview

`KoogBasicAgent` is a template and learning resource for developers looking to integrate LLM capabilities into their multiplatform applications. It leverages the Koog AI Framework to facilitate agent creation, tool registration, and seamless communication with local models like **Ollama**.

### Key Learning Objectives
- [x] **Basic Agent Setup**: Configure `AIAgent` within a multiplatform context.
- [x] **Tool Registration**: Define and register custom tools for the agent to use.
- [x] **Local LLM Integration**: Connect with local models (e.g., Llama 3.2) using Ollama.
- [x] **Multiplatform Deployment**: Share core agent logic across mobile, web, desktop, and server.

---

## 🛠 Tool Creation

Tools allow your agent to perform specific tasks. In Koog, you define tools using the `Tool` class and `typeToken` for type-safe arguments and results.

```kotlin
@Serializable
data class MathArgs(
    @param:LLMDescription("The first number for the operation")
    val firstNumber: Int,
    @param:LLMDescription("The second number for the operation")
    val secondNumber: Int
)

object MathsTool {
    val add = object : Tool<MathArgs, Int>(
        argsType = typeToken<MathArgs>(),
        resultType = typeToken<Int>(),
        name = "add",
        description = "Adds two numbers together and returns the sum."
    ) {
        override suspend fun execute(args: MathArgs): Int = args.firstNumber + args.secondNumber
    }

    val multiply = object : Tool<MathArgs, Int>(
        argsType = typeToken<MathArgs>(),
        resultType = typeToken<Int>(),
        name = "multiply",
        description = "Multiplies two numbers together and returns the product."
    ) {
        override suspend fun execute(args: MathArgs): Int = args.firstNumber * args.secondNumber
    }

    val all = listOf(add, multiply)
}
```

---

## ⚙️ Agent Configuration

The `AIAgent` is the core component. You can install features like `EventHandler` to monitor agent activity.

```kotlin
val agent = AIAgent(
    toolRegistry = toolRegistry,
    promptExecutor = getPlatform().promptExecutor,
    llmModel = OllamaModels.Meta.LLAMA_3_2_3B,
    systemPrompt = """
             You are a helpful assistant.
             
             Note: Call tools only when it's needed, answer user questions.
    """.trimIndent()
) {
    install(EventHandler) {
        onToolCallStarting { ctx ->
            println(">> Calling tool: ${ctx.toolName} with args ${ctx.toolArgs}")
        }
    }
}
```

---

## 📁 Project Structure

The project is organized into several modules to maximize code reuse:

- **`/:core`**: Contains the core logic and Koog AI integrations shared between **all** targets.
- **`/:app:shared`**: Shared UI and business logic for Compose Multiplatform applications (Android, iOS, Desktop, Web).
    - `commonMain`: Shared logic for all targets.
    - `iosMain`, `jvmMain`, etc.: Platform-specific implementations.
- **`/:app:androidApp`**: Android-specific entry point and resources.
- **`/:app:desktopApp`**: Desktop-specific entry point.
- **`/:app:iosApp`**: iOS-specific entry point (SwiftUI).
- **`/:server`**: Ktor server application.

---

## 🏃 Running the Applications

Ensure you have **Ollama** running locally with the required model (e.g., `llama3.2`) before starting the apps.

| Platform | Command |
| :--- | :--- |
| **Android** | `./gradlew :app:androidApp:assembleDebug` |
| **Desktop** | `./gradlew :app:desktopApp:run` (or `hotRun --auto`) |
| **Server** | `./gradlew :server:run` |
| **Web (Wasm)** | `./gradlew :app:webApp:wasmJsBrowserDevelopmentRun` |
| **Web (JS)** | `./gradlew :app:webApp:jsBrowserDevelopmentRun` |
| **iOS** | Open `/app/iosApp` in Xcode and run. |

---

## 🧪 Running Tests

Validate your shared logic and server implementation across different environments:

- **Android**: `./gradlew :app:shared:testAndroidHostTest`
- **Desktop**: `./gradlew :app:shared:jvmTest`
- **Server**: `./gradlew :server:test`
- **Web (Wasm)**: `./gradlew :app:shared:wasmJsTest`
- **iOS**: `./gradlew :app:shared:iosSimulatorArm64Test`

---

## 📚 Resources

- [Koog AI Framework Documentation](https://koog.ai)
- [Kotlin Multiplatform Docs](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform Docs](https://kotlinlang.org/compose-multiplatform/)
- [Ollama](https://docs.ollama.com/quickstart)


```
>> Calling tool: add_numbers with args {"a":3, "b":5}
>> MathsTool: Adding 3.0 + 5.0
>> Calling tool: add_numbers with args {"b":10, "a":10}
>> MathsTool: Adding 10.0 + 10.0
```