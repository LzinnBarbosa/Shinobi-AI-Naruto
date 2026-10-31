package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.ShinobiAiService
import com.example.data.model.NinjaCharacter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

enum class MessageSender {
    USER, SHINOBI_AI
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val sender: MessageSender,
    val timestamp: Long = System.currentTimeMillis(),
    val referencedNinjaName: String? = null
)

data class ShinobiAiUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isStreaming: Boolean = false,
    val currentStreamingResponse: String = "",
    val activeContextNinja: NinjaCharacter? = null,
    val duelAnalysis: String? = null,
    val isDuelAnalyzing: Boolean = false
)

class ShinobiAiViewModel(
    private val aiService: ShinobiAiService = ShinobiAiService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ShinobiAiUiState(
            messages = listOf(
                ChatMessage(
                    text = "Saudações, meu caro companheiro shinobi! Aqui é o Shinobi-AI. Há mais de 13 anos acompanho cada arco, cada lágrima e cada grande batalha desse mundo ninja — desde o exame Chūnin até o legado de Boruto! Consulte o Grimório dos ninjas, me faça perguntas sobre o lore ou traga dois guerreiros para nosso Simulador de Duelos. Qual é o seu caminho ninja hoje, Dattebayo?!",
                    sender = MessageSender.SHINOBI_AI
                )
            )
        )
    )
    val uiState: StateFlow<ShinobiAiUiState> = _uiState.asStateFlow()

    fun sendMessage(userText: String, contextNinja: NinjaCharacter? = _uiState.value.activeContextNinja) {
        if (userText.isBlank() || _uiState.value.isStreaming) return

        val userMsg = ChatMessage(
            text = userText,
            sender = MessageSender.USER,
            referencedNinjaName = contextNinja?.name
        )

        val updatedMessages = _uiState.value.messages + userMsg
        _uiState.value = _uiState.value.copy(
            messages = updatedMessages,
            isStreaming = true,
            currentStreamingResponse = ""
        )

        viewModelScope.launch {
            val responseAccumulator = StringBuilder()
            try {
                aiService.streamAsk(userText, contextNinja).collect { chunk ->
                    responseAccumulator.append(chunk)
                    _uiState.value = _uiState.value.copy(
                        currentStreamingResponse = responseAccumulator.toString()
                    )
                }

                val aiMsg = ChatMessage(
                    text = responseAccumulator.toString(),
                    sender = MessageSender.SHINOBI_AI,
                    referencedNinjaName = contextNinja?.name
                )
                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + aiMsg,
                    isStreaming = false,
                    currentStreamingResponse = ""
                )
            } catch (e: Exception) {
                val errorMsg = ChatMessage(
                    text = "Perdão, jovem ninja. Houve uma interferência no fluxo de chakra: ${e.localizedMessage ?: "Tente novamente"}.",
                    sender = MessageSender.SHINOBI_AI
                )
                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + errorMsg,
                    isStreaming = false,
                    currentStreamingResponse = ""
                )
            }
        }
    }

    fun askAboutNinja(ninja: NinjaCharacter, customQuestion: String? = null) {
        _uiState.value = _uiState.value.copy(activeContextNinja = ninja)
        val question = customQuestion ?: "Conte-me sobre a trajetória, segredos, jutsus e o peso emocional de ${ninja.name} na história de Naruto!"
        sendMessage(question, ninja)
    }

    fun startDuelAnalysis(fighter1: NinjaCharacter, fighter2: NinjaCharacter) {
        if (_uiState.value.isDuelAnalyzing) return

        _uiState.value = _uiState.value.copy(
            isDuelAnalyzing = true,
            duelAnalysis = "Invocando o conhecimento dos pergaminhos sagrados e analisando o confronto entre ${fighter1.name} e ${fighter2.name}..."
        )

        viewModelScope.launch {
            val duelAccumulator = StringBuilder()
            try {
                aiService.streamDuelAnalysis(fighter1, fighter2).collect { chunk ->
                    duelAccumulator.append(chunk)
                    _uiState.value = _uiState.value.copy(
                        duelAnalysis = duelAccumulator.toString()
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    duelAnalysis = "A análise tática foi interrompida por turbulência de chakra: ${e.localizedMessage}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isDuelAnalyzing = false)
            }
        }
    }

    fun clearDuelAnalysis() {
        _uiState.value = _uiState.value.copy(duelAnalysis = null, isDuelAnalyzing = false)
    }

    fun clearChat() {
        _uiState.value = _uiState.value.copy(
            messages = listOf(
                ChatMessage(
                    text = "Pergaminho de conversa renovado! Sobre qual ninja ou técnica lendária vamos debater agora, companheiro?",
                    sender = MessageSender.SHINOBI_AI
                )
            ),
            isStreaming = false,
            currentStreamingResponse = "",
            activeContextNinja = null
        )
    }
}
