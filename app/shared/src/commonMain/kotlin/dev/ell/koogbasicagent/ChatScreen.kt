package dev.ell.koogbasicagent

import ai.koog.agents.core.agent.AIAgent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

enum class Typer(
) {
    USER,
    BOT
}


data class ChatMessage(
    val text: String,
    val typer: Typer
)

@Composable
fun ChatScreen(tempChatMessage: List<ChatMessage>) {

    val scope = rememberCoroutineScope()


    var chatMessages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    "Hello! How can I help you with employee info?",
                    Typer.BOT
                )
            )
        )
    }
    var agent by remember { mutableStateOf<AIAgent<String, String>?>(null) }

    var isLoading by remember { mutableStateOf(false) }

    var isConnecting by remember { mutableStateOf(true) }
    var connectionError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        chatMessages = chatMessages + tempChatMessage
        try {
            agent = AgentProvider().provideAgent()
        } catch (e: Exception) {
            println("Error: ChatScreen: ${e.message}")
            connectionError = e.message
        } finally {
            isConnecting = false
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    ) {

        ConnectionBanner(
            isConnecting = isConnecting,
            error = connectionError,
            connected = agent != null
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            reverseLayout = true
        ) {
            items(
                items = chatMessages.reversed()
            ) { message ->
                ChatItem(message)
            }
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp))
        }
        ChatInputField(
            onSendMessage = { text ->
                val currentAgent = agent ?: return@ChatInputField

                chatMessages = chatMessages + (ChatMessage(text, Typer.USER))

                scope.launch {
                    try {
                        isLoading = true
                        val response = currentAgent.run(text)
                        chatMessages = chatMessages + ChatMessage(response, Typer.BOT)
                    } catch (e: Exception) {
                        chatMessages =
                            chatMessages + ChatMessage("Error: ${e.message}", Typer.USER)
                        println("Error: ChatScreen: ${e.message}")
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

    }

}

@Composable
private fun ConnectionBanner(isConnecting: Boolean, error: String?, connected: Boolean) {
    val (bg, text) = when {
        isConnecting -> MaterialTheme.colorScheme.surfaceVariant to "Connecting..."
        error != null -> Color(0xFFFFCDD2) to "Connection failed: $error"
        connected -> Color(0xFFC8E6C9) to "Connected to MCP Server"
        else -> MaterialTheme.colorScheme.surfaceVariant to ""
    }

    if (text.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(bg)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ChatInputField(
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Manage the input text state
    var textState by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message...") },

            // 2. Add the send button inside the text field
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (textState.isNotBlank()) {
                            onSendMessage(textState)
                            textState = "" // Clear input after sending
                        }
                    },
                    enabled = textState.isNotBlank() // Disable button if text is empty
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Message"
                    )
                }
            },

            // 3. Configure the software keyboard's "Send" action button
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (textState.isNotBlank()) {
                        onSendMessage(textState)
                        textState = ""
                    }
                }
            ),
            maxLines = 4
        )
    }
}

@Composable
fun ChatItem(chatMessage: ChatMessage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (chatMessage.typer == Typer.USER)
            Alignment.End
        else
            Alignment.Start
    ) {

        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = MaterialTheme.colorScheme.primary)
        ) {
            SelectionContainer {


                Text(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    text = chatMessage.text,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium

                )
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun ChatItemPreview() {
    MaterialTheme {
        ChatItem(ChatMessage("Hi!", Typer.USER))
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    MaterialTheme {
        ChatScreen(
            tempChatMessage = listOf(
                ChatMessage("Hi!", Typer.USER),
                ChatMessage("Hi!", Typer.BOT),
                ChatMessage("How are you?", Typer.USER),
                ChatMessage("I'm fine, thank you!", Typer.BOT)
            )
        )
    }
}


