# 🎮 Jogo da Forca - Programação III

> Projeto desenvolvido como requisito avaliativo para a disciplina de Programação III no **Instituto Federal de Goiás (IFG) - Campus Luziânia**.

## 📄 Sobre o Projeto
Este projeto consiste na implementação de um **Jogo da Forca** interativo, desenvolvido em linguagem **Java**. O sistema foca na lógica de turnos alternados (Multijogador) e na aplicação de conceitos avançados de Orientação a Objetos e arquitetura de software.

**Objetivo:** Adivinhar a palavra secreta através de tentativas de letras antes que o desenho do boneco na forca esteja completo.

## 🛠️ Tecnologias e Ferramentas
O projeto respeita estritamente os requisitos não funcionais definidos:
* **Linguagem:** Java (JDK atualizado)
* **Interface Gráfica:** JavaFX (Versão 22)
* **Arquitetura:** MVC (Model-View-Controller)
* **IDE Recomendada:** IntelliJ IDEA / Eclipse / VS Code

## 📋 Requisitos do Sistema

### 1. Funcionalidades Obrigatórias
* **Multijogador Local:** Suporte para 2 jogadores competindo em turnos alternados.
* **Cadastro:** Inserção dos nomes dos jogadores no início da partida.
* **Banco de Palavras:** Organização por categorias (ex: Frutas, Países, Animais).
* **Interface Visual:**
    * Exibição das letras já tentadas.
    * Palavra oculta representada por traços.
    * Desenho progressivo do boneco da forca a cada erro.
* **Placar:** Contagem de erros e acertos individualizada.
* **Tratamento de Exceções:** Validação de entradas inválidas e leitura segura de arquivos.

### 2. Estrutura de Classes (Obrigatória)
A arquitetura do projeto separa as classes de domínio das classes de visualização e inicialização:
* `Jogada`: Responsável pela lógica da tentativa atual.
* `Palavra`: Gerencia a palavra secreta e as categorias.
* `Jogadores`: Gerencia os nomes e pontuações.
* `Controlador`: Faz a ponte entre a interface (FXML) e as regras de negócio.

## 🚀 Funcionalidades Bônus (Opcionais)
* [ ] Modo Single Player (vs Computador) com níveis de dificuldade.
* [ ] Sistema de dicas limitadas.
* [ ] Animações e transições na interface.
* [ ] Temas visuais selecionáveis pelo usuário.

## 📦 Como Executar
1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/seu-repositorio.git](https://github.com/seu-usuario/seu-repositorio.git)
    ```
2.  **Configuração:**
    * Certifique-se de que as bibliotecas do **JavaFX** estão configuradas no `Module Path` da sua IDE.
    * Adicione os argumentos da VM se necessário: `--module-path "C:\caminho\para\javafx-sdk-22\lib" --add-modules javafx.controls,javafx.fxml`
3.  **Execução:**
    * Execute a classe principal que estende `Application`.

## 👨‍🏫 Informações da Disciplina
* **Instituição:** Instituto Federal de Goiás - Campus Luziânia
* **Professor:** Lucas de Almeida Ribeiro
* **Curso:** Bacharelado em Sistemas de Informação
* **Data:** Outubro/2025

---
*Desenvolvido por João Batista e Eduardo Inácio*
