# ⚔️ Jogo da Forca - Medieval Edition 🛡️

> **Disciplina:** Programação III  
> [cite_start]**Instituição:** Instituto Federal de Goiás (IFG) - Campus Luziânia [cite: 4]  
> [cite_start]**Professor:** Lucas de Almeida Ribeiro [cite: 6]  
> [cite_start]**Data:** Outubro/2025 [cite: 7]

---

## 📜 Sobre o Projeto
Este projeto consiste na implementação de um **Jogo da Forca** interativo, desenvolvido em linguagem **Java** com interface gráfica **JavaFX**.

[cite_start]O sistema foi projetado estritamente sob a arquitetura **MVC (Model-View-Controller)** [cite: 33][cite_start], focando na lógica de **Multijogador Local**, onde dois jogadores competem em turnos alternados para resolver palavras distintas[cite: 11]. O projeto apresenta um **tema visual medieval** consistente e aplica conceitos avançados de Orientação a Objetos.

## 🎯 Objetivos e Regras
O objetivo é adivinhar a palavra secreta sorteada através de tentativas de letras ou do chute da palavra completa.
* [cite_start]A cada erro, uma parte do corpo do boneco é desenhada na forca[cite: 9].
* O jogador perde a rodada se o boneco for completado (6 erros).
* [cite_start]O jogador vence se descobrir a palavra antes do enforcamento[cite: 10].

---

## 📋 Requisitos Atendidos (Conformidade com o Projeto)

### 1. Requisitos Não Funcionais
* [cite_start]✅ **Linguagem:** Java (JDK 22)[cite: 16].
* [cite_start]✅ **Interface Gráfica:** JavaFX (Versão 22)[cite: 17].
* [cite_start]✅ **Tema:** Aparência "Medieval" bem definida, com cenários e assets personalizados[cite: 18].
* [cite_start]✅ **Jogabilidade:** Sistema exclusivo para 2 jogadores[cite: 19].
* [cite_start]✅ **Arquitetura:** Separação estrita de classes de domínio (`Jogada`, `Palavra`, `Jogadores`) das classes de controle (`Controlador`) e visualização (`FXML`)[cite: 20].

### 2. Requisitos Funcionais
* [cite_start]✅ **Cadastro:** Tela inicial para inserção dos nomes dos jogadores[cite: 22].
* [cite_start]✅ **Turnos:** Sistema de gerenciamento que alterna a vez e o foco entre os jogadores[cite: 23].
* [cite_start]✅ **Categorias:** Leitura de arquivo `palavras.txt` organizando o banco por categorias (Frutas, Animais, Países, Objetos)[cite: 24].
* [cite_start]✅ **Interface Visual:** Exibição dinâmica da palavra oculta, letras já tentadas e evolução do boneco[cite: 25].
* [cite_start]✅ **Placar:** Contagem individual de erros na rodada[cite: 27].
* [cite_start]✅ **Tratamento de Exceções:** Validação robusta para entradas inválidas (letras repetidas, campos vazios) e leitura de arquivos[cite: 28].

---

## 🏗️ Estrutura do Projeto (MVC)

O código foi organizado para demonstrar domínio sobre a separação de responsabilidades:

```text
src/main/java/forca
├── 📦 controller
│   ├── InicioController.java   // Controla a tela de login e seleção de categorias
│   └── JogoController.java     // Gerencia a partida, turnos e atualizações visuais
├── 📦 model
│   ├── Jogada.java             // Lógica principal: valida letras, verifica vitória/derrota
│   ├── Jogador.java            // Dados do jogador (nome, erros)
│   ├── Jogadores.java          // Gerencia a dupla e define de quem é a vez
│   ├── Palavra.java            // Leitura de arquivo e sorteio aleatório
│   ├── LetraJaTentadaException.java // Exceção personalizada
│   └── NomeInvalidoException.java   // Exceção personalizada
└── ForcaApp.java               // Classe principal (Inicialização JavaFX)
