public class Jogadores {

    private Jogador jogador1;
    private Jogador jogador2;
    private Jogador jogadorDaVez;

    public Jogadores(String nomeJogador1, String nomeJogador2) {
        this.jogador1 = new Jogador(nomeJogador1);
        this.jogador2 = new Jogador(nomeJogador2);
        this.jogadorDaVez = jogador1;
    }

    public Jogador getJogadorDaVez() {
        return jogadorDaVez;
    }

    public void trocarTurno() {
        if (jogadorDaVez == jogador1) {
            jogadorDaVez = jogador2;
        } else {
            jogadorDaVez = jogador1;
        }
    }

    // Métodos adicionados para acessar os jogadores individualmente
    public Jogador getJogador1() {
        return jogador1;
    }

    public Jogador getJogador2() {
        return jogador2;
    }
}