# Shinobi-AI: O Grimório Ninja Definitivo & Companheiro da Vontade do Fogo

O **Shinobi-AI** é um aplicativo Android imersivo construído em Jetpack Compose que conecta fãs do universo Naruto a um acervo vivo de dados (via Dattebayo API) potencializado por uma inteligência artificial que encarna um shinobi veterano de mais de 13 anos de experiência na Vontade do Fogo.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> **Decisões confirmadas pelo usuário na Fase 1:**
> - **Navegação Principal:** Abas dinâmicas divididas entre **Grimório Ninja** (Explorador de Personagens, Clãs e Técnicas) e **Shinobi-AI Chat** (Companheiro interativo).
> - **Estilo Visual & Atmosfera:** **Vontade do Fogo** — base em preto profundo/carvão Konoha, acentos em Laranja Uzumaki ardente (#FF7A00 / #FF5722), carmesim de bandana e pergaminhos dourados.
> - **Recurso de Destaque:** **Simulador de Duelos Shinobi** — duelo tático entre dois ninjas selecionados com análise profunda de jutsus, reservas de chakra, afinidade elemental e o veredito narrativo do Shinobi-AI.

---

### 1. Overview & Core Concept

- **O que faz:** Transforma dados brutos do Dattebayo API em pergaminhos vivos de história. Os usuários podem explorar personagens, clãs, vilas e jutsus com busca instantânea e filtros; abrir perfis detalhados com estatísticas, árvores genealógicas e linhagens; consultar o **Shinobi-AI** para explicações de lore, debates filosóficos (ex: ciclo de ódio do Pain, sacrifício do Itachi); e simular confrontos táticos hipotéticos no **Dojo de Duelos**.
- **Público-alvo:** Fãs dedicados e casuais de Naruto Clássico, Shippuden e Boruto que buscam uma enciclopédia interativa e um companheiro autêntico em português (BR) que fale a sua língua ("Dattebayo!", "Vontade do Fogo").
- **Valor central:** Uma experiência que não parece um mero banco de dados, mas sim um pergaminho sagrado de Konoha guiado por um mentor shinobi experiente e apaixonado.

---

### 2. User Experience & Visual Design

#### Fluxos Principais do Usuário
1. **Aba Grimório Ninja:**
   - Barra de busca dinâmica com filtros rápidos por Vilas (Konoha, Suna, Kumo, Kiri, Iwa), Clãs (Uchiha, Uzumaki, Hyūga, Senju) e Afiliações (Akatsuki, Sannin, Hokage).
   - Lista rica de cards ninjas com avatares/retratos oficiais, insígnias de vilas, kekkei genkai e naturezas de chakra.
   - Pressionar um ninja abre a **Visão de Pergaminho Detalhada**: estatísticas, jutsus assinaturas, linhagem familiar, estreia e um botão de ação imediata: *"Perguntar ao Shinobi-AI sobre este ninja"* ou *"Enviar para o Duelo"*.
2. **Aba Shinobi-AI (Chat do Companheiro):**
   - Diálogo contínuo com a persona veterana em português autêntico.
   - Pílulas de sugestões contextuais pré-definidas (ex: *"Por que o Itachi tomou aquela decisão?", "Explique o Hiraishin do Minato", "Quem venceria: Jiraiya no Modo Sábio vs Itachi?"*).
   - Respostas transmitidas com streaming fluido, citando jutsus e arcos com precisão.
3. **Aba / Modo Arena de Duelos:**
   - Seleção de dois ninjas lado a lado (ex: Kakashi vs Minato, Gaara vs Deidara).
   - Comparativo visual imediato de atributos e naturezas elementais.
   - Botão *"Simular Combate Ninja"*: O Shinobi-AI analisa as condições, jutsus, vantagens táticas e narra os momentos-chave da batalha até o veredito final.

#### Identidade Visual & Tema ("Vontade do Fogo")
- **Background:** Carvão e ardósia profunda (`#0D0D11`, `#15151E`) simulando a noite de Konoha.
- **Primária:** Laranja Uzumaki brilhante (`#FF6B00`) com gradiente para Âmbar do Rasengan (`#FFA000`).
- **Secundária / Acentos:** Vermelho Carmesim de Selamento (`#D32F2F`) e Dourado Pergaminho (`#F4C430`).
- **Superfícies:** Cartões elevados com bordas sutis brilhantes (`#2A2A38`) e detalhes estéticos inspirados em pergaminhos de jutsus e nuvens estilizadas.
- **Tipografia:** Hierarquia forte com títulos marcantes, tags nítidas de classificação ninja e corpo de texto com leitura confortável.

---

### 3. Key Product Decisions & Trade-Offs

- **Consumo da Dattebayo API + Cache Local Resiliente:**
  - *Decisão:* Consumir a REST API oficial do Dattebayo (`https://dattebayo-api.onrender.com/`) usando Retrofit + Moshi, acompanhado de dados essenciais embutidos em cache para garantir que o app continue 100% responsivo e funcional mesmo em caso de latência de cold-start do Render.
  - *Por quê:* Proporciona o acervo completo e atualizado da API sem comprometer a fluidez inicial da experiência.
- **Arquitetura de IA do Shinobi-AI (Firebase AI Logic + Gemini):**
  - *Decisão:* Utilizar o modelo `gemini-3.8-flash` via Firebase AI Logic com System Instructions estritas em português (BR), incorporando a persona do shinobi veterano e injetando o contexto do personagem atual quando acionado a partir de um cartão.
  - *Por quê:* Respostas rápidas, streaming natural e consistência inabalável no tom e conhecimento de lore.
- **Navegação com NavigationBar M3 e Gestos Fluidos:**
  - *Decisão:* `NavigationBar` Material 3 com suporte total a insets de tela (Edge-to-Edge) e `AnimatedContent` entre visões para trocas ágeis de contexto.

---

### 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                              MainActivity                              │
│         (Edge-to-Edge, Firebase AppCheck Init, Theme Controller)       │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
           ┌────────────────────────┴────────────────────────┐
           ▼                                                 ▼
┌────────────────────────┐                        ┌──────────────────────┐
│     GrimoireScreen     │                        │    ShinobiAiScreen   │
│  (Filtros, Pesquisa,   │                        │ (Chat, Pergaminhos,  │
│  Lista de Ninjas,      │                        │  Streaming, Duelos)  │
│  Detalhes do Personagem│                        └──────────┬───────────┘
└──────────┬─────────────┘                                   │
           │                                                 │
           ▼                                                 ▼
┌────────────────────────┐                        ┌──────────────────────┐
│  GrimoireViewModel     │                        │  ShinobiAiViewModel  │
│  - DattebayoRepository │                        │  - Firebase AI Logic │
│  - Busca, Filtros,     │                        │  - Gemini 3.8 Flash  │
│    Seleção de Duelos   │                        │  - Histórico de Chat │
└──────────┬─────────────┘                        └──────────┬───────────┘
           │                                                 │
           ▼                                                 ▼
┌────────────────────────┐                        ┌──────────────────────┐
│  DattebayoApiService   │                        │ System Instruction:  │
│ (characters, clans,    │                        │ Veterano de Konoha,  │
│  villages, jutsu)      │                        │ Vontade do Fogo (PT) │
└────────────────────────┘                        └──────────────────────┘
```

#### Modelo de Dados Principal
- `NinjaCharacter`: `id`, `name`, `images`, `debut`, `family`, `jutsu`, `natureType`, `personal` (clan, affiliation, rank, classification, status).
- `Clan`: `id`, `name`, `characters`.
- `ChatMessage`: `id`, `text`, `sender` (`USER` / `SHINOBI_AI`), `timestamp`, `referencedNinja`.
- `DuelMatchup`: `fighter1: NinjaCharacter`, `fighter2: NinjaCharacter`, `status: IDLE | ANALYZING | RESULT`.
