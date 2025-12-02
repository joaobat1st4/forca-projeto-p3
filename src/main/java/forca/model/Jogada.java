package forca.model;

import java.util.HashSet;
import java.util.Set;

public class Jogada {

    private Palavra palavra;
    private Jogadores jogadores;
    private char[] palavraOculta;
    private Set<Character> letrasTentadas;
    private boolean jogoAcabou;
    private String resultado;

    public Jogada(Jogadores jogadores, Palavra palavra) {
        this.jogadores = jogadores;
        this.palavra = palavra;
        this.letrasTentadas = new HashSet<>();
    }

    // Configura tudo para começar uma partida nova
    public void iniciarNovaRodada(String categoria) {
        palavra.sortear(categoria);
        this.letrasTentadas.clear();
        this.jogoAcabou = false;
        this.resultado = "";

        // Zera os erros apenas da rodada atual (mantém o histórico geral se quiser)
        jogadores.getJogador1().zerarErrosDaRodada();
        jogadores.getJogador2().zerarErrosDaRodada();

        // Cria a máscara da palavra (ex: _ _ _ _)
        this.palavraOculta = new char[palavra.getPalavraSecreta().length()];
        for (int i = 0; i < palavraOculta.length; i++) {
            palavraOculta[i] = '_';
        }
    }

    // Tenta uma única letra
    public boolean tentarLetra(char letra) throws LetraJaTentadaException {
        if (jogoAcabou) return false;

        letra = Character.toUpperCase(letra);

        // Verifica se a letra já foi usada
        if (letrasTentadas.contains(letra)) {
            throw new LetraJaTentadaException("A letra '" + letra + "' já foi tentada!");
        }

        letrasTentadas.add(letra);

        boolean acertou = false;
        String secreta = palavra.getPalavraSecreta();

        // Verifica se a letra existe na palavra secreta
        for (int i = 0; i < secreta.length(); i++) {
            if (secreta.charAt(i) == letra) {
                palavraOculta[i] = letra;
                acertou = true;
            }
        }

        Jogador jogadorAtual = jogadores.getJogadorDaVez();

        if (acertou) {
            verificarVitoria(jogadorAtual);
        } else {
            // Se errou, adiciona erro APENAS para o jogador atual
            jogadorAtual.adicionarErroNaRodada();
            verificarDerrota(jogadorAtual);
        }

        return acertou;
    }

    // Tenta chutar a palavra completa (Novo Recurso)
    public boolean arriscarPalavra(String chute) {
        if (jogoAcabou) return false;

        String secreta = palavra.getPalavraSecreta();
        Jogador jogadorAtual = jogadores.getJogadorDaVez();

        // Compara ignorando maiúsculas/minúsculas
        if (chute.equalsIgnoreCase(secreta)) {
            // Se acertou o chute, revela a palavra toda
            this.palavraOculta = secreta.toCharArray();
            // Dá a vitória
            this.jogoAcabou = true;
            this.resultado = "VITÓRIA INCRÍVEL! " + jogadorAtual.getNome() + " acertou o chute!";
            jogadorAtual.adicionarAcertoGeral();
            return true;
        } else {
            // Se errou o chute, conta como erro
            jogadorAtual.adicionarErroNaRodada();
            verificarDerrota(jogadorAtual);
            return false;
        }
    }

    private void verificarVitoria(Jogador j) {
        boolean completou = true;
        for (char c : palavraOculta) {
            if (c == '_') {
                completou = false;
                break;
            }
        }
        if (completou) {
            jogoAcabou = true;
            j.adicionarAcertoGeral();
            resultado = "PARABÉNS! O vencedor foi: " + j.getNome();
        }
    }

    private void verificarDerrota(Jogador j) {
        if (j.getErrosNaRodada() >= 6) {
            jogoAcabou = true;
            resultado = j.getNome() + " foi enforcado. FIM DE JOGO!";
        }
    }

    // Getters para a Interface
    public boolean isJogoAcabou() {
        return jogoAcabou;
    }

    public String getResultado() {
        return resultado;
    }

    public String getPalavraOcultaFormatada() {
        StringBuilder sb = new StringBuilder();
        for (char c : palavraOculta) {
            sb.append(c).append(" ");
        }
        return sb.toString();
    }
}