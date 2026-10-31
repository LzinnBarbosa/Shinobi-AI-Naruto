package com.example.data.repository

import android.util.Log
import com.example.data.api.DattebayoApiService
import com.example.data.model.DebutInfo
import com.example.data.model.NinjaCharacter
import com.example.data.model.PersonalInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NinjaRepository(
    private val api: DattebayoApiService = DattebayoApiService.create()
) {
    private val memoryCache = mutableListOf<NinjaCharacter>()

    init {
        // Pre-populate with iconic legends so the app has zero latency on launch
        memoryCache.addAll(getLegendaryShinobis())
    }

    suspend fun getCharacters(forceRefresh: Boolean = false): List<NinjaCharacter> = withContext(Dispatchers.IO) {
        if (!forceRefresh && memoryCache.size > 25) {
            return@withContext memoryCache
        }
        try {
            val response = api.getCharacters(page = 1, limit = 50)
            if (response.characters.isNotEmpty()) {
                val liveMap = response.characters.associateBy { it.id }.toMutableMap()
                // Keep legendary ones populated
                for (legend in getLegendaryShinobis()) {
                    if (!liveMap.containsKey(legend.id)) {
                        liveMap[legend.id] = legend
                    }
                }
                memoryCache.clear()
                memoryCache.addAll(liveMap.values.sortedBy { it.id })
                return@withContext memoryCache
            }
        } catch (e: Exception) {
            Log.w("NinjaRepo", "Dattebayo API unavailable or timed out, using ninja scrolls cache", e)
        }
        return@withContext memoryCache
    }

    suspend fun getCharacterById(id: Int): NinjaCharacter? = withContext(Dispatchers.IO) {
        memoryCache.firstOrNull { it.id == id } ?: try {
            val live = api.getCharacterById(id)
            memoryCache.add(live)
            live
        } catch (e: Exception) {
            null
        }
    }

    private fun getLegendaryShinobis(): List<NinjaCharacter> {
        return listOf(
            NinjaCharacter(
                id = 1,
                name = "Naruto Uzumaki",
                images = listOf("https://static.wikia.nocookie.net/naruto/images/4/42/Naruto_Part_III.png"),
                debut = DebutInfo(
                    manga = "Capítulo #1",
                    anime = "Episódio #1",
                    appearsIn = "Anime, Mangá, Filmes, Jogos"
                ),
                family = mapOf(
                    "Pai" to "Minato Namikaze",
                    "Mãe" to "Kushina Uzumaki",
                    "Esposa" to "Hinata Hyūga",
                    "Filho" to "Boruto Uzumaki",
                    "Filha" to "Himawari Uzumaki",
                    "Mestre" to "Jiraiya & Kakashi Hatake"
                ),
                jutsu = listOf(
                    "Rasengan",
                    "Fūton: Rasenshuriken",
                    "Modo Sábio dos Sapos (Senjutsu)",
                    "Modo Chakra da Kurama (KCM2)",
                    "Modo Bárion",
                    "Kage Bunshin no Jutsu (Multiclones das Sombras)",
                    "Oodama Rasengan"
                ),
                natureType = listOf("Vento (Fūton)", "Fogo (Katon)", "Relâmpago (Raiton)", "Terra (Doton)", "Água (Suiton)", "Lava", "Magnetismo"),
                personal = PersonalInfo(
                    birthdate = "10 de Outubro",
                    sex = "Masculino",
                    status = "Vivo",
                    classification = listOf("Jinchūriki", "Sábio", "Hokage"),
                    occupation = "7º Hokage de Konohagakure",
                    affiliation = listOf("Konohagakure", "Forças Aliadas Shinobi"),
                    team = listOf("Time 7 (Time Kakashi)", "Konoha 11"),
                    clan = "Clã Uzumaki",
                    kekkeiGenkai = listOf("Liberação de Lava", "Liberação de Magnetismo", "Liberação de Ebulição"),
                    titles = listOf("Herói da Folha", "Criança da Profecia", "Sétimo Hokage")
                )
            ),
            NinjaCharacter(
                id = 2,
                name = "Sasuke Uchiha",
                images = listOf("https://static.wikia.nocookie.net/naruto/images/2/21/Sasuke_Part_III.png"),
                debut = DebutInfo(
                    manga = "Capítulo #3",
                    anime = "Episódio #1",
                    appearsIn = "Anime, Mangá, Filmes, Jogos"
                ),
                family = mapOf(
                    "Pai" to "Fugaku Uchiha",
                    "Mãe" to "Mikoto Uchiha",
                    "Irmão" to "Itachi Uchiha",
                    "Esposa" to "Sakura Haruno",
                    "Filha" to "Sarada Uchiha"
                ),
                jutsu = listOf(
                    "Chidori",
                    "Kirin",
                    "Amaterasu",
                    "Susanoo Perfeito",
                    "Kagutsuchi (Controle de Chamas)",
                    "Amenotejikara (Teletransporte do Rinnegan)",
                    "Chibaku Tensei",
                    "Katon: Gōkakyū no Jutsu"
                ),
                natureType = listOf("Relâmpago (Raiton)", "Fogo (Katon)", "Vento (Fūton)", "Terra (Doton)", "Água (Suiton)", "Yin"),
                personal = PersonalInfo(
                    birthdate = "23 de Julho",
                    sex = "Masculino",
                    status = "Vivo",
                    classification = listOf("Ninja Renegado (Ex)", "Hokage das Sombras"),
                    occupation = "Protetor Andarilho de Konoha",
                    affiliation = listOf("Konohagakure", "Akatsuki (Ex)", "Taka"),
                    team = listOf("Time 7", "Taka / Hebi"),
                    clan = "Clã Uchiha",
                    kekkeiGenkai = listOf("Sharingan", "Mangekyō Sharingan Eterno", "Rinnegan Supremo"),
                    titles = listOf("O Outro Hokage", "Vingador")
                )
            ),
            NinjaCharacter(
                id = 3,
                name = "Kakashi Hatake",
                images = listOf("https://static.wikia.nocookie.net/naruto/images/2/27/Kakashi_Hatake.png"),
                debut = DebutInfo(
                    manga = "Capítulo #3",
                    anime = "Episódio #3",
                    appearsIn = "Anime, Mangá, Filmes, Jogos"
                ),
                family = mapOf(
                    "Pai" to "Sakumo Hatake (Presa Branca da Folha)",
                    "Mestre" to "Minato Namikaze"
                ),
                jutsu = listOf(
                    "Raikiri (Espada Relâmpago)",
                    "Kamui (Distorção Espaço-Temporal)",
                    "Chidori",
                    "Susanoo de Kamui",
                    "Doton: Doryūheki (Parede de Lama de Cães)",
                    "Rasengan",
                    "Técnica dos Mil Anos de Morte"
                ),
                natureType = listOf("Relâmpago (Raiton)", "Terra (Doton)", "Água (Suiton)", "Fogo (Katon)", "Vento (Fūton)", "Yin", "Yang"),
                personal = PersonalInfo(
                    birthdate = "15 de Setembro",
                    sex = "Masculino",
                    status = "Vivo",
                    classification = listOf("Ex-Capitão ANBU", "Jonin de Elite", "Hokage"),
                    occupation = "6º Hokage (Aposentado)",
                    affiliation = listOf("Konohagakure", "Forças Aliadas Shinobi"),
                    team = listOf("Time Minato", "Time 7 (Líder)"),
                    clan = "Clã Hatake",
                    kekkeiGenkai = listOf("Sharingan (Transplantado)", "Mangekyō Sharingan"),
                    titles = listOf("O Ninja Copiador dos Mil Jutsus", "Kakashi do Sharingan", "Sexto Hokage")
                )
            ),
            NinjaCharacter(
                id = 4,
                name = "Itachi Uchiha",
                images = listOf("https://static.wikia.nocookie.net/naruto/images/b/bb/Itachi_Uchiha.png"),
                debut = DebutInfo(
                    manga = "Capítulo #139",
                    anime = "Episódio #80",
                    appearsIn = "Anime, Mangá, Novelas, Jogos"
                ),
                family = mapOf(
                    "Pai" to "Fugaku Uchiha",
                    "Mãe" to "Mikoto Uchiha",
                    "Irmão" to "Sasuke Uchiha",
                    "Amigo & Mentor" to "Shisui Uchiha"
                ),
                jutsu = listOf(
                    "Tsukuyomi",
                    "Amaterasu",
                    "Susanoo (Espada Totsuka & Espelho de Yata)",
                    "Izanami",
                    "Katon: Hōsenka no Jutsu",
                    "Karasu Bunshin no Jutsu (Clone de Corvos)",
                    "Magen: Kasegui no Jutsu"
                ),
                natureType = listOf("Fogo (Katon)", "Água (Suiton)", "Vento (Fūton)", "Yin", "Yang"),
                personal = PersonalInfo(
                    birthdate = "9 de Junho",
                    sex = "Masculino",
                    status = "Falecido (Lenda Eterna)",
                    classification = listOf("Ninja Renegado Classe S", "Ex-Capitão ANBU"),
                    occupation = "Agente Duplo de Konoha na Akatsuki",
                    affiliation = listOf("Konohagakure", "Akatsuki"),
                    team = listOf("Dupla Akatsuki com Kisame Hoshigaki"),
                    clan = "Clã Uchiha",
                    kekkeiGenkai = listOf("Sharingan", "Mangekyō Sharingan"),
                    titles = listOf("Gênio dos Uchiha", "Herói nas Sombras")
                )
            ),
            NinjaCharacter(
                id = 5,
                name = "Minato Namikaze",
                images = listOf("https://static.wikia.nocookie.net/naruto/images/4/4b/Minato_Namikaze.png"),
                debut = DebutInfo(
                    manga = "Capítulo #1",
                    anime = "Episódio #1",
                    appearsIn = "Anime, Mangá, Filmes, Jogos"
                ),
                family = mapOf(
                    "Esposa" to "Kushina Uzumaki",
                    "Filho" to "Naruto Uzumaki",
                    "Neto" to "Boruto Uzumaki",
                    "Mestre" to "Jiraiya"
                ),
                jutsu = listOf(
                    "Hiraishin no Jutsu (Deus Voador do Trovão)",
                    "Rasengan (Criador da Técnica)",
                    "Modo Sábio",
                    "Shiki Fūjin (Selo Ceifador da Morte)",
                    "Hakke no Fūin Shiki (Selo de Oito Trigramas)",
                    "Hiraishin: Dōrai (Barreira Guiada do Trovão)"
                ),
                natureType = listOf("Relâmpago (Raiton)", "Vento (Fūton)", "Fogo (Katon)", "Yin", "Yang"),
                personal = PersonalInfo(
                    birthdate = "25 de Janeiro",
                    sex = "Masculino",
                    status = "Falecido (Edo Tensei Libertado)",
                    classification = listOf("Sábio", "Hokage"),
                    occupation = "4º Hokage de Konohagakure",
                    affiliation = listOf("Konohagakure"),
                    team = listOf("Time Jiraiya", "Time Minato (Líder)"),
                    clan = "Clã Namikaze",
                    kekkeiGenkai = emptyList<String>(),
                    titles = listOf("O Relâmpago Amarelo de Konoha", "Quarto Hokage")
                )
            ),
            NinjaCharacter(
                id = 6,
                name = "Jiraiya",
                images = listOf("https://static.wikia.nocookie.net/naruto/images/8/81/Jiraiya_portrait.png"),
                debut = DebutInfo(
                    manga = "Capítulo #90",
                    anime = "Episódio #52",
                    appearsIn = "Anime, Mangá, Filmes, Jogos"
                ),
                family = mapOf(
                    "Mestre" to "Hiruzen Sarutobi (3º Hokage)",
                    "Alunos" to "Minato Namikaze, Nagato, Yahiko, Konan, Naruto Uzumaki"
                ),
                jutsu = listOf(
                    "Senpō: Goemon (Banho de Óleo Fervente)",
                    "Cho Oodama Rasengan",
                    "Modo Sábio dos Sapos com Fukasaku e Shima",
                    "Yomi Numa (Pântano do Submundo)",
                    "Hari Jizō (Guarda de Agulhas de Cabelo)",
                    "Kuchiyose: Gamabunta"
                ),
                natureType = listOf("Fogo (Katon)", "Terra (Doton)", "Vento (Fūton)", "Água (Suiton)", "Yin", "Yang"),
                personal = PersonalInfo(
                    birthdate = "11 de Novembro",
                    sex = "Masculino",
                    status = "Falecido (Memória Imortal)",
                    classification = listOf("Sannin Lendário", "Sábio"),
                    occupation = "Escritor, Espião Mestre e Ninja Errante",
                    affiliation = listOf("Konohagakure", "Monte Myōboku"),
                    team = listOf("Os Três Sannin Lendários"),
                    clan = "Sem Clã Nobre",
                    kekkeiGenkai = emptyList<String>(),
                    titles = listOf("Gama Sennin (Sábio dos Sapos)", "Sannin Lendário")
                )
            ),
            NinjaCharacter(
                id = 7,
                name = "Pain (Nagato Uzumaki)",
                images = listOf("https://static.wikia.nocookie.net/naruto/images/4/4d/Pain_Deva.png"),
                debut = DebutInfo(
                    manga = "Capítulo #372",
                    anime = "Episódio #128",
                    appearsIn = "Anime, Mangá, Jogos"
                ),
                family = mapOf(
                    "Pai" to "Ise",
                    "Mãe" to "Fusō (Linhagem Uzumaki)",
                    "Companheiros" to "Yahiko e Konan",
                    "Mestre" to "Jiraiya"
                ),
                jutsu = listOf(
                    "Shinra Tensei (Empurrão Onipotente)",
                    "Banshō Ten'in (Atração Celestial)",
                    "Chibaku Tensei (Devastação Planetária)",
                    "Seis Caminhos de Pain",
                    "Gedo: Rinne Tensei no Jutsu (Ressurreição Samsara)",
                    "Kuchiyose: Estátua Gedō Mazō"
                ),
                natureType = listOf("Fogo (Katon)", "Vento (Fūton)", "Relâmpago (Raiton)", "Terra (Doton)", "Água (Suiton)", "Yang"),
                personal = PersonalInfo(
                    birthdate = "19 de Setembro",
                    sex = "Masculino",
                    status = "Falecido (Encontrou a Paz)",
                    classification = listOf("Líder da Akatsuki", "Ninja Renegado Classe S"),
                    occupation = "Líder Supremo de Amegakure",
                    affiliation = listOf("Amegakure (Vila Oculta da Chuva)", "Akatsuki"),
                    team = listOf("Órfãos de Ame", "Akatsuki Original"),
                    clan = "Clã Uzumaki",
                    kekkeiGenkai = listOf("Rinnegan"),
                    titles = listOf("Deus de Ame", "O Terceiro dos Seis Caminhos")
                )
            ),
            NinjaCharacter(
                id = 8,
                name = "Madara Uchiha",
                images = listOf("https://static.wikia.nocookie.net/naruto/images/c/c9/Madara_Uchiha.png"),
                debut = DebutInfo(
                    manga = "Capítulo #370 (Mencionado) / #559",
                    anime = "Episódio #125 / #322",
                    appearsIn = "Anime, Mangá, Jogos"
                ),
                family = mapOf(
                    "Irmão" to "Izuna Uchiha",
                    "Rival & Irmão de Armas" to "Hashirama Senju"
                ),
                jutsu = listOf(
                    "Tengai Shinsei (Queda de Meteoros)",
                    "Susanoo Perfeito Majestoso",
                    "Katon: Gōka Mekkyaku (Grande Aniquilação do Fogo)",
                    "Mokuton: Jukai Kōtan (Nascimento da Floresta Profunda)",
                    "Limbo: Hengoku (Mundo Fronteiriço)",
                    "Tsukuyomi Infinito",
                    "Gudōdama (Esferas da Busca da Verdade)"
                ),
                natureType = listOf("Fogo (Katon)", "Vento (Fūton)", "Relâmpago (Raiton)", "Terra (Doton)", "Água (Suiton)", "Madeira (Mokuton)", "Yin-Yang"),
                personal = PersonalInfo(
                    birthdate = "24 de Dezembro",
                    sex = "Masculino",
                    status = "Falecido (Lenda Primordial)",
                    classification = listOf("Co-fundador de Konoha", "Jinchūriki do Dez Caudas"),
                    occupation = "Líder Histórico do Clã Uchiha",
                    affiliation = listOf("Konohagakure (Fundador)", "Akatsuki"),
                    team = listOf("Fundadores de Konoha"),
                    clan = "Clã Uchiha",
                    kekkeiGenkai = listOf("Sharingan", "Mangekyō Sharingan Eterno", "Rinnegan", "Liberação de Madeira"),
                    titles = listOf("Fantasma dos Uchiha", "Lenda dos Shinobis")
                )
            ),
            NinjaCharacter(
                id = 9,
                name = "Hashirama Senju",
                images = listOf("https://static.wikia.nocookie.net/naruto/images/3/30/Hashirama_Senju.png"),
                debut = DebutInfo(
                    manga = "Capítulo #118",
                    anime = "Episódio #69",
                    appearsIn = "Anime, Mangá, Jogos"
                ),
                family = mapOf(
                    "Irmãos" to "Tobirama, Kawarama, Itama Senju",
                    "Esposa" to "Mito Uzumaki",
                    "Neta" to "Tsunade Senju"
                ),
                jutsu = listOf(
                    "Senpō Mokuton: Shin Sūsenju (Várias Milhares de Mãos Verdadeiras)",
                    "Mokuton: Mokuryū no Jutsu (Dragão de Madeira)",
                    "Mokuton: Mokujin no Jutsu (Homem de Madeira)",
                    "Modo Sábio de Shikkotsu",
                    "Kokuangyō no Jutsu (Jutsu da Escuridão)",
                    "Regeneração Celular Divina Sem Selos"
                ),
                natureType = listOf("Madeira (Mokuton)", "Terra (Doton)", "Água (Suiton)", "Fogo (Katon)", "Vento (Fūton)", "Relâmpago (Raiton)", "Yang"),
                personal = PersonalInfo(
                    birthdate = "23 de Outubro",
                    sex = "Masculino",
                    status = "Falecido (Fundador Sagrado)",
                    classification = listOf("Deus Shinobi", "Hokage"),
                    occupation = "1º Hokage de Konohagakure",
                    affiliation = listOf("Konohagakure"),
                    team = listOf("Clã Senju da Floresta"),
                    clan = "Clã Senju",
                    kekkeiGenkai = listOf("Liberação de Madeira (Mokuton)"),
                    titles = listOf("Deus dos Shinobis", "Primeiro Hokage", "Fundador da Paz")
                )
            ),
            NinjaCharacter(
                id = 10,
                name = "Gaara do Deserto",
                images = listOf("https://static.wikia.nocookie.net/naruto/images/0/0f/Gaara_Part_II.png"),
                debut = DebutInfo(
                    manga = "Capítulo #35",
                    anime = "Episódio #20",
                    appearsIn = "Anime, Mangá, Filmes, Jogos"
                ),
                family = mapOf(
                    "Pai" to "Rasa (4º Kazekage)",
                    "Irmãos" to "Temari e Kankurō",
                    "Amigo Fraterno" to "Naruto Uzumaki"
                ),
                jutsu = listOf(
                    "Sabaku Kyū (Caixão de Areia)",
                    "Sabaku Sōsō (Funeral do Deserto)",
                    "Ryūsa Bakuryū (Tsunami de Areia Movediça)",
                    "Suna no Tate (Escudo Supremo de Areia da Mãe)",
                    "Sabaku Taisō (Funeral Gigante do Deserto)",
                    "Terceiro Olho de Areia"
                ),
                natureType = listOf("Terra (Doton)", "Vento (Fūton)", "Relâmpago (Raiton)", "Magnetismo"),
                personal = PersonalInfo(
                    birthdate = "19 de Janeiro",
                    sex = "Masculino",
                    status = "Vivo",
                    classification = listOf("Kazekage", "Ex-Jinchūriki do Shukaku"),
                    occupation = "5º Kazekage de Sunagakure",
                    affiliation = listOf("Sunagakure", "Forças Aliadas Shinobi (Comandante Geral)"),
                    team = listOf("Três Irmãos da Areia"),
                    clan = "Clã Kazekage",
                    kekkeiGenkai = listOf("Liberação de Magnetismo"),
                    titles = listOf("Gaara da Cachoeira de Areia", "Quinto Kazekage")
                )
            ),
            NinjaCharacter(
                id = 11,
                name = "Obito Uchiha (Tobi)",
                images = listOf("https://static.wikia.nocookie.net/naruto/images/4/4a/Obito_Uchiha.png"),
                debut = DebutInfo(
                    manga = "Capítulo #239 (Gaiden) / #599",
                    anime = "Episódio #32 (Shippuden)",
                    appearsIn = "Anime, Mangá, Jogos"
                ),
                family = mapOf(
                    "Companheiros" to "Kakashi Hatake e Rin Nohara",
                    "Mestre" to "Minato Namikaze"
                ),
                jutsu = listOf(
                    "Kamui Intangível (Olho Direito)",
                    "Katon: Bakufū Ranbu (Dança Selvagem da Onda de Vento)",
                    "Izanagi",
                    "Modo Jinchūriki do Dez Caudas",
                    "Espada de Nunoboko",
                    "Kuchiyose: Gedō Mazō"
                ),
                natureType = listOf("Fogo (Katon)", "Vento (Fūton)", "Relâmpago (Raiton)", "Terra (Doton)", "Água (Suiton)", "Madeira (Mokuton)", "Yin-Yang"),
                personal = PersonalInfo(
                    birthdate = "10 de Fevereiro",
                    sex = "Masculino",
                    status = "Falecido (Redimido)",
                    classification = listOf("Mentor nas Sombras", "Ninja Renegado"),
                    occupation = "Artífice do Plano Olho da Lua",
                    affiliation = listOf("Konohagakure (Ex)", "Akatsuki"),
                    team = listOf("Time Minato"),
                    clan = "Clã Uchiha",
                    kekkeiGenkai = listOf("Sharingan", "Mangekyō Sharingan", "Rinnegan", "Liberação de Madeira"),
                    titles = listOf("Tobi", "O Homem Mascarado")
                )
            ),
            NinjaCharacter(
                id = 12,
                name = "Might Guy",
                images = listOf("https://static.wikia.nocookie.net/naruto/images/1/1a/Might_Guy.png"),
                debut = DebutInfo(
                    manga = "Capítulo #38",
                    anime = "Episódio #21",
                    appearsIn = "Anime, Mangá, Filmes, Jogos"
                ),
                family = mapOf(
                    "Pai" to "Might Duy (O Genin Eterno)",
                    "Eterno Rival" to "Kakashi Hatake",
                    "Discípulo Amado" to "Rock Lee"
                ),
                jutsu = listOf(
                    "Yagai (Guy Noturno - O Dragão Vermelho)",
                    "Asakujaku (Pavão do Amanhecer)",
                    "Hirudora (Tigre Diurno)",
                    "Sekizō (Elefante do Anoitecer)",
                    "Oito Portões Internos (Hachimon Tonkō)",
                    "Konoha Senpū"
                ),
                natureType = listOf("Fogo (Katon)", "Relâmpago (Raiton)"),
                personal = PersonalInfo(
                    birthdate = "1 de Janeiro",
                    sex = "Masculino",
                    status = "Vivo (Cadeira de Rodas / Alma Invicta)",
                    classification = listOf("Jonin Mestre em Taijutsu"),
                    occupation = "Instrutor Ninja",
                    affiliation = listOf("Konohagakure"),
                    team = listOf("Time Guy (Lee, Neji, Tenten)"),
                    clan = "Sem Clã Especial",
                    kekkeiGenkai = emptyList<String>(),
                    titles = listOf("A Besta Verde Nobre de Konoha", "O Mestre Supremo do Taijutsu")
                )
            )
        )
    }
}
