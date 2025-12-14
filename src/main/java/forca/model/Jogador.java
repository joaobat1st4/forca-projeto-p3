package forca.model;

public class Jogador {

    // --- ATRIBUTOS (O que o jogador TEM) ---
    private String nome;

    // Placar geral (não zera quando começa nova rodada)
    private int totalVitorias;

    // Erros da rodada atual (Zera a cada novo jogo. Se chegar a 6, perde)
    private int errosAtuais;

    // Construtor: Cria o jogador do zero
    public Jogador(String nome) {
        this.nome = nome;
        this.totalVitorias = 0;
        this.errosAtuais = 0;
    }

    // --- MÉTODOS DE AÇÃO (O que acontece com o jogador) ---

    // Chamado quando o jogador erra uma letra ou chuta errado
    public void registrarErro() {
        this.errosAtuais++;
    }

    // Chamado quando o jogo começa de novo, para limpar o boneco
    public void zerarErros() {
        this.errosAtuais = 0;
    }

    // Chamado apenas quando ele ganha o jogo inteiro
    public void registrarVitoria() {
        this.totalVitorias++;
    }

    // --- GETTERS (Para a tela pegar os dados e mostrar) ---

    public String getNome() {
        return nome;
    }

    public int getErrosAtuais() {
        return errosAtuais;
    }

    public int getTotalVitorias() {
        return totalVitorias;
    }
}