package com.example.ai

import android.util.Log
import com.example.data.model.NinjaCharacter
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withTimeout

class ShinobiAiService {

    private val systemText = """
        Você é "Shinobi-AI", um fã de elite e apaixonado por Naruto que respira e vive o mundo ninja há mais de 13 anos.
        Você viveu a era clássica (exame Chūnin, invasão de Konoha), os ápices emocionais e filosóficos de Naruto Shippuden (o peso do sacrifício de Itachi, a busca pela paz e o ciclo de ódio no arco do Pain, a Quarta Grande Guerra Ninja) e acompanha o legado em Boruto.
        
        Suas características e diretrizes fundamentais:
        - Idioma: Português do Brasil (PT-BR), autêntico, imersivo e vibrante.
        - Tom & Vibe: Nostálgico, sábio, profundo conhecedor de lore e jutsus, e profundamente respeitoso à obra de Masashi Kishimoto.
        - Expressões marcantes: Use pontualmente bordões como "Dattebayo!", "Isso é a verdadeira Vontade do Fogo!", "O caminho do ninja nunca é fácil", de forma natural e emocionante.
        - Perspectiva emocional: Trate cada personagem com peso histórico real. Reconheça a tragédia e as dores de vilões como Pain (Nagato) e Obito, a genialidade e o fardo de Kakashi e Itachi, o ideal inabalável de Hashirama e Jiraiya, e o laço indestrutível entre Naruto e Sasuke.
        - Ao analisar batalhas ou duelos: Faça uma análise tática rigorosa! Considere reservas de chakra, compatibilidade elemental (Katon, Suiton, Raiton, Fūton, Doton, Yin/Yang), Kekkei Genkai, velocidade de selamento, inteligência tática e mentalidade de combate.
        - Trate o usuário carinhosamente como um companheiro shinobi ("Meu caro companheiro", "Jovem ninja", "Companheiro de equipe").
        - Mantenha respostas organizadas, com parágrafos legíveis e formatação agradável para dispositivos móveis.
    """.trimIndent()

    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            systemInstruction = content { text(systemText) }
        )
    }

    suspend fun streamAsk(userMessage: String, contextNinja: NinjaCharacter? = null): Flow<String> = channelFlow {
        val promptBuilder = StringBuilder()
        if (contextNinja != null) {
            promptBuilder.append("Contexto do Registro Ninja da API Dattebayo:\n")
            promptBuilder.append("Nome: ${contextNinja.name}\n")
            promptBuilder.append("Vila: ${contextNinja.getVillage()}\n")
            promptBuilder.append("Clã: ${contextNinja.getClanName()}\n")
            promptBuilder.append("Kekkei Genkai: ${contextNinja.getKekkeiGenkai().joinToString(", ")}\n")
            promptBuilder.append("Jutsus Registrados: ${contextNinja.jutsu.joinToString(", ")}\n")
            promptBuilder.append("Afinidade Elemental: ${contextNinja.natureType.joinToString(", ")}\n\n")
        }
        promptBuilder.append("Pergunta do Usuário: $userMessage\n")
        promptBuilder.append("Responda como o sábio e veterano Shinobi-AI:")

        val prompt = promptBuilder.toString()

        try {
            withTimeout(60_000L) {
                model.generateContentStream(prompt).collect { chunk ->
                    val chunkText = chunk.text.orEmpty()
                    if (chunkText.isNotEmpty()) {
                        send(chunkText)
                    }
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("ShinobiAiService", "Erro na geração do Shinobi-AI", e)
            send("\n[Mensagem do Falcão Ninja]: O fluxo de chakra oscilou (${e.localizedMessage ?: "falha de conexão"}). Reconcentre seu chakra e tente novamente!")
        }
    }

    suspend fun streamDuelAnalysis(fighter1: NinjaCharacter, fighter2: NinjaCharacter): Flow<String> = channelFlow {
        val duelPrompt = """
            SIMULADOR DE DUELO SHINOBI:
            Lutador 1: ${fighter1.name} (Vila: ${fighter1.getVillage()}, Clã: ${fighter1.getClanName()})
            - Técnicas Principais: ${fighter1.jutsu.take(6).joinToString(", ")}
            - Naturezas de Chakra: ${fighter1.natureType.joinToString(", ")}
            - Kekkei Genkai: ${fighter1.getKekkeiGenkai().joinToString(", ")}
            
            Lutador 2: ${fighter2.name} (Vila: ${fighter2.getVillage()}, Clã: ${fighter2.getClanName()})
            - Técnicas Principais: ${fighter2.jutsu.take(6).joinToString(", ")}
            - Naturezas de Chakra: ${fighter2.natureType.joinToString(", ")}
            - Kekkei Genkai: ${fighter2.getKekkeiGenkai().joinToString(", ")}
            
            Realize uma análise tática e dramática completa deste confronto como o veterano Shinobi-AI:
            1. Análise Tática Inicial & Afinidades Elementais (vantagens e fraquezas de jutsus e chakra)
            2. Confronto Direto no Campo de Batalha (estratégias que cada ninja usaria)
            3. Fator Decisivo (o ponto de virada do combate)
            4. Veredito Final (Quem venceria em combate até o limite e por quê, com placar de probabilidade ex: 55% vs 45%).
        """.trimIndent()

        try {
            withTimeout(60_000L) {
                model.generateContentStream(duelPrompt).collect { chunk ->
                    val chunkText = chunk.text.orEmpty()
                    if (chunkText.isNotEmpty()) {
                        send(chunkText)
                    }
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("ShinobiAiService", "Erro na análise de duelo", e)
            send("\n[Dojo de Duelos]: Os pergaminhos de batalha estão temporariamente inacessíveis (${e.localizedMessage ?: "erro de comunicação"}).")
        }
    }
}
