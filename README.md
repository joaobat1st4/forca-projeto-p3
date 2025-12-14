# 📚 Projeto: Jogo da Forca (Programação III)

**Instituto Federal de Goiás (IFG) - Campus Luziânia**
**Curso:** Bacharelado em Sistemas de Informação
**Professor:** Lucas de Almeida Ribeiro

---

## 🎯 Objetivo do Trabalho
Construir um **Jogo da Forca** interativo para **dois jogadores (multijogador local)**. 
O objetivo do jogo é adivinhar uma palavra secreta através de tentativas de letras. A cada erro, partes de um boneco são desenhadas na forca. O jogador ganha se adivinhar a palavra antes do boneco ser completamente enforcado.

---

## 🛠️ Requisitos Não Funcionais (Técnicos)
*Estas regras definem AS FERRAMENTAS que devem ser usadas. O não cumprimento pode afetar a nota.*

1.  **Linguagem:** O código DEVE ser escrito estritamente em **Java**.
2.  **Interface Gráfica:** O framework visual DEVE ser o **JavaFX** (preferencialmente versão 22).
3.  **Visual:** O sistema deve ter uma aparência e tema bem definidos.
4.  **Modo de Jogo:** O sistema deve possibilitar partidas para **2 jogadores**.
5.  **Arquitetura:** Obrigatório separar as classes de domínio/lógica das classes de interface e inicialização. As seguintes classes DEVEM ser criadas:
    * `Jogada`
    * `Palavra`
    * `Jogadores`
    * `Controlador`

---

## 📋 Requisitos Funcionais Implementados
*Funcionalidades presentes na aplicação:*

1.  **Cadastro Inicial:** Inserção dos nomes dos jogadores antes da partida.
2.  **Sistema de Turnos:** Gerenciamento de turnos alternados no modo Multiplayer.
3.  **Banco de Palavras:** Leitura de arquivo externo (`palavras.txt`) organizado por categorias (Frutas, Países, Animais, Objetos).
4.  **Interface Visual Completa:**
    * Exibição das letras já tentadas.
    * Palavra oculta representada por traços.
    * Desenho progressivo do boneco da forca a cada erro.
5.  **Placar:** Contador de erros (limite de 6) e feedback de vitória/derrota.
6.  **Tratamento de Exceções:**
    * `NomeInvalidoException`: Para validação de cadastro.
    * `LetraJaTentadaException`: Para evitar tentativas repetidas.
    * Tratamento de leitura de arquivos.

---

## 🎓 Critérios de Avaliação (Arguição)
*Pontos que o professor irá verificar durante a apresentação oral.*

* **Nota Individual:** A nota será atribuída individualmente, dependendo do domínio demonstrado sobre o código.
* **Apresentação Obrigatória:** A nota só será atribuída mediante apresentação do software rodando.
* **Conceitos Exigidos:** É necessário demonstrar no código o uso de:
    * Tratamento de Exceções (`Exceptions`).
    * Manipulação de Arquivos (Leitura/Escrita).
    * Annotations (ex: `@FXML`, `@Override`).
    * Manipulação de Strings.
    * Programação Orientada a Objetos (POO).
    * JavaFX e Arquitetura MVC.

---

## 🏗️ Estrutura de Arquivos (MVC)
*Organização atual dos arquivos no repositório:*

```text
forca-projeto-p3/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── forca/
│   │   │       ├── 📦 controller
│   │   │       │   ├── InicioController.java       // Lógica do Menu Principal
│   │   │       │   ├── MultiplayerController.java  // Lógica do Modo 2 Jogadores
│   │   │       │   └── SinglePlayerController.java // Lógica do Modo 1 Jogador
│   │   │       ├── 📦 model
│   │   │       │   ├── Jogada.java                 // Regras de negócio da rodada
│   │   │       │   ├── Jogador.java                // Objeto Jogador (nome, erros)
│   │   │       │   ├── Jogadores.java              // Gerenciamento da dupla/turno
│   │   │       │   ├── Palavra.java                // Manipulação do banco de palavras
│   │   │       │   ├── LetraJaTentadaException.java
│   │   │       │   └── NomeInvalidoException.java
│   │   │       ├── ForcaApp.java                   // Classe Application (JavaFX)
│   │   │       └── Launcher.java                   // Classe Inicializadora (Main)
│   │   └── resources/
│   │       └── forca/
│   │           ├── 📂 images                       // Assets (Fundo, Boneco, Ícones)
│   │           ├── inicio-view.fxml                // Tela de Menu
│   │           ├── multiplayer-view.fxml           // Tela de Jogo (2 Jogadores)
│   │           └── single-view.fxml                // Tela de Jogo (1 Jogador)
│   └── test/
│       └── java/
│           └── ForcaTest.java                      // Testes Unitários
├── palavras.txt                                    // Banco de dados de palavras (Raiz)
├── pom.xml                                         // Dependências Maven
└── README.md                                       // Documentação
---
*Este documento foi gerado com base nas especificações do Projeto de Programação III - IFG Luziânia.*
