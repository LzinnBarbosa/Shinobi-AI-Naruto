package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.FlameAmber
import com.example.ui.theme.KonohaBackground
import com.example.ui.theme.KonohaBorder
import com.example.ui.theme.KonohaCard
import com.example.ui.theme.KonohaCardElevated
import com.example.ui.theme.KonohaSurface
import com.example.ui.theme.ScrollGold
import com.example.ui.theme.TextNinjaMuted
import com.example.ui.theme.TextNinjaPrimary
import com.example.ui.theme.TextNinjaSecondary
import com.example.ui.theme.UzumakiOrange
import com.example.ui.viewmodel.ChatMessage
import com.example.ui.viewmodel.MessageSender
import com.example.ui.viewmodel.ShinobiAiViewModel

@Composable
fun ShinobiAiScreen(
    viewModel: ShinobiAiViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val suggestedQuestions = listOf(
        "Por que Itachi tomou aquela decisão extrema com o clã?",
        "Qual a filosofia por trás do ciclo de ódio do Pain?",
        "Como funciona exatamente o Hiraishin do Minato?",
        "Quem venceria: Jiraiya no Modo Sábio vs Itachi?",
        "Qual o verdadeiro poder do Modo Bárion de Naruto?"
    )

    // Scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size, uiState.currentStreamingResponse) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KonohaBackground)
            .padding(bottom = 80.dp) // Leave space for bottom nav
    ) {
        // Chat Header
        Surface(
            color = KonohaSurface,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(FlameAmber, UzumakiOrange)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "忍", color = KonohaBackground, fontWeight = FontWeight.Black, fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Shinobi-AI",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = UzumakiOrange
                        )
                        Text(
                            text = "Companheiro Veterano • Vontade do Fogo",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextNinjaSecondary
                        )
                    }
                }

                IconButton(
                    onClick = { viewModel.clearChat() },
                    modifier = Modifier.testTag("clear_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Limpar Pergaminho de Conversa",
                        tint = TextNinjaSecondary
                    )
                }
            }
        }

        // Suggestions pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestedQuestions.forEach { question ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = KonohaCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, KonohaBorder),
                    modifier = Modifier.padding(vertical = 2.dp),
                    onClick = { viewModel.sendMessage(question) }
                ) {
                    Text(
                        text = question,
                        style = MaterialTheme.typography.labelSmall,
                        color = ScrollGold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = uiState.messages,
                key = { it.id }
            ) { msg ->
                ChatBubble(message = msg)
            }

            // Streaming active bubble
            if (uiState.isStreaming && uiState.currentStreamingResponse.isNotEmpty()) {
                item {
                    ChatBubble(
                        message = ChatMessage(
                            text = uiState.currentStreamingResponse,
                            sender = MessageSender.SHINOBI_AI,
                            referencedNinjaName = uiState.activeContextNinja?.name
                        ),
                        isStreamingActive = true
                    )
                }
            } else if (uiState.isStreaming) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = UzumakiOrange,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Shinobi-AI consultando os pergaminhos...",
                            style = MaterialTheme.typography.bodySmall,
                            color = UzumakiOrange
                        )
                    }
                }
            }
        }

        // Input Area
        Surface(
            color = KonohaSurface,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input"),
                    placeholder = {
                        Text(
                            text = "Converse com o Shinobi-AI...",
                            color = TextNinjaMuted,
                            fontSize = 14.sp
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = KonohaCard,
                        unfocusedContainerColor = KonohaCard,
                        focusedBorderColor = UzumakiOrange,
                        unfocusedBorderColor = KonohaBorder,
                        focusedTextColor = TextNinjaPrimary,
                        unfocusedTextColor = TextNinjaPrimary
                    ),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank() && !uiState.isStreaming) {
                            val msg = inputText
                            inputText = ""
                            viewModel.sendMessage(msg)
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (inputText.isNotBlank()) UzumakiOrange else KonohaCardElevated)
                        .testTag("send_message_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar Mensagem",
                        tint = if (inputText.isNotBlank()) KonohaBackground else TextNinjaMuted
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    isStreamingActive: Boolean = false
) {
    val isUser = message.sender == MessageSender.USER

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(UzumakiOrange),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "忍", color = KonohaBackground, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) KonohaCardElevated else KonohaCard,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isUser) FlameAmber.copy(alpha = 0.5f) else KonohaBorder
            ),
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!message.referencedNinjaName.isNullOrBlank()) {
                    Text(
                        text = "📜 Registro: ${message.referencedNinjaName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = FlameAmber,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                Text(
                    text = message.text + if (isStreamingActive) " ❚" else "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextNinjaPrimary,
                    lineHeight = 21.sp
                )
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(FlameAmber),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = KonohaBackground,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
