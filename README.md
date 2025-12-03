# 📚 Projeto: Jogo da Forca (Programação III)

**Instituto Federal de Goiás (IFG) - Campus Luziânia** **Curso:** Bacharelado em Sistemas de Informação  
**Professor:** Lucas de Almeida Ribeiro  
**Data de Referência:** 14 de outubro de 2025  

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

## 📋 Requisitos Funcionais (O que o sistema deve fazer)
*Funcionalidades obrigatórias que precisam estar rodando na apresentação.*

1.  **Cadastro Inicial:** No início do jogo, deve ser possível inserir o **nome dos jogadores**.
2.  **Sistema de Turnos:** O jogo deve gerenciar turnos **alternados** entre os jogadores (competindo para resolver palavras diferentes).
3.  **Banco de Palavras:** As palavras devem ser organizadas por **categorias** (ex: Frutas, Países, Animais, etc.).
4.  **Interface Visual Completa:**
    * Mostrar as **letras já tentadas**.
    * Mostrar a **palavra oculta** representada por traços.
    * Desenhar o **boneco da forca** progressivamente em diferentes estágios de erro.
5.  **Placar:** Deve haver um contador de **erros e acertos** individual por jogador.
6.  **Tratamento de Erros:** Implementar tratamento de exceções (`try-catch`) para:
    * Entradas inválidas do usuário.
    * Arquivos não encontrados (leitura do banco de palavras).

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

## ⭐ Funcionalidades Bônus (Extras)
*Implementações opcionais que podem valorizar a nota.*

* [ ] **Single Player:** Modo contra o computador com níveis de dificuldade.
* [ ] **Dicas:** Sistema de dicas limitadas durante a partida.
* [ ] **Animações:** Efeitos visuais na interface.
* [ ] **Temas:** Possibilidade do usuário selecionar diferentes temas visuais (skins/cores).

---

## 🚀 Guia de Implementação (Sugestão baseada no PDF)

### 1. Model (Lógica)
* **`Palavra.java`**: Ler o arquivo de texto, separar por categoria, sortear palavra.
* **`Jogadores.java`**: Guardar nomes, controlar de quem é a vez, contar pontos.
* **`Jogada.java`**: Verificar se a letra existe na palavra, atualizar estado do boneco.

### 2. View (Interface)
* Arquivos `.fxml` (Tela de Início e Tela de Jogo).
* Uso de CSS para o "Tema bem definido".

### 3. Controller (Conexão)
* **`Controlador.java`**: Receber os cliques dos botões, chamar a `Jogada`, atualizar a tela (boneco/texto).

## 🏗️ Estrutura de Arquivos (MVC)
*Organização recomendada para atender ao requisito de separação de classes.*

```text
Projeto-Forca/
├── src/
│   └── main/
│       ├── java/forca/
│       │   ├── 📦 controller
│       │   │   ├── InicioController.java   // Controla o menu inicial
│       │   │   └── JogoController.java     // Controla a partida e o boneco
│       │   ├── 📦 model
│       │   │   ├── Jogada.java             // Lógica de acerto/erro
│       │   │   ├── Jogador.java            // Dados (nome, erros)
│       │   │   ├── Jogadores.java          // Gerencia a dupla e o turno
│       │   │   ├── Palavra.java            // Sorteio e leitura do arquivo
│       │   │   ├── LetraJaTentadaException.java
│       │   │   └── NomeInvalidoException.java
│       │   └── ForcaApp.java               // Classe principal (Start)
│       └── resources/forca/
│           ├── 📂 images                   // Imagens (Fundo, Boneco, Forca)
│           ├── inicio-view.fxml            // Tela de Login
│           └── jogo-view.fxml              // Tela do Jogo
├── palavras.txt                            // Banco de palavras (Na raiz)
├── pom.xml                                 // Dependências Maven
└── README.md

---
*Este documento foi gerado com base nas especificações do Projeto de Programação III - IFG Luziânia.*
