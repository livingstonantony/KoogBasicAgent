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

<img src="https://github.com/livingstonantony/KoogBasicAgent/tree/master/doc/demo.png" width="600">

## 🛠 Tool Creation

Tools allow your agent to perform specific tasks. In Koog, you define tools using the `Tool` class and `typeToken` for type-safe arguments and results.

```kotlin

object MathsTool {
  val add = object : Tool<MathArgs, Double>(
    argsType = typeToken<MathArgs>(),
    resultType = typeToken<Double>(),
    name = "add_numbers",
    description = "Adds two numbers together. Use this for ANY addition."
  ) {

    override suspend fun execute(args: MathArgs): Double {
      println(">> MathsTool: Adding ${args.a} + ${args.b}")
      return args.a + args.b
    }
  }

  val multiply = object : Tool<MathArgs, Double>(
    argsType = typeToken<MathArgs>(),
    resultType = typeToken<Double>(),
    name = "multiply_numbers",
    description = "Multiplies two numbers together. Use this for ANY multiplication."
  ) {
    override suspend fun execute(args: MathArgs): Double {
      println(">> MathsTool: Multiplying ${args.a} * ${args.b}")
      return args.a * args.b
    }
  }

  val all = listOf(add, multiply)
}
```


---

## ⚙️ Agent Configuration

The `AIAgent` is the core component. You can install features like `EventHandler` to monitor agent activity.

```kotlin
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
```
## Log

```
>> Calling tool: add_numbers with args {"a":3, "b":5}
>> MathsTool: Adding 3.0 + 5.0
>> Calling tool: add_numbers with args {"b":10, "a":10}
>> MathsTool: Adding 10.0 + 10.0
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
