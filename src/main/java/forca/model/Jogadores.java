package forca.model;

public class Jogadores {

    // Guarda os dois objetos criados (Jogador1 e Jogador2)
    private Jogador jogador1;
    private Jogador jogador2;

    // Aponta para quem é o jogador "ativo" no momento
    private Jogador jogadorDaVez;

    // Construtor: Recebe apenas os nomes (vindos da tela inicial) e cria os objetos
    public Jogadores(String nomeJ1, String nomeJ2) {
        this.jogador1 = new Jogador(nomeJ1);
        this.jogador2 = new Jogador(nomeJ2);

        // Regra do jogo: O Jogador 1 sempre começa
        this.jogadorDaVez = this.jogador1;
    }

    // Lógica de alternância (Toggle)
    public void trocarTurno() {
        if (this.jogadorDaVez == this.jogador1) {
            this.jogadorDaVez = this.jogador2;
        } else {
            this.jogadorDaVez = this.jogador1;
        }
    }

    // GETTERS (para o Controller saber quem desenhar na tela)

    public Jogador getJogadorDaVez() {
        return jogadorDaVez;
    }

    public Jogador getJogador1() {
        return jogador1;
    }

    public Jogador getJogador2() {
        return jogador2;
    }
}