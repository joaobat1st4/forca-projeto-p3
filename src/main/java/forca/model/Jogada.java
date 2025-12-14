package forca.model;

import java.util.*;

public class Jogada {

    // --- DEPENDÊNCIAS (Quem participa do jogo) ---
    private Palavra bancoDePalavras;
    private Jogadores jogadores;

    // --- ESTADO ATUAL DA PARTIDA ---

    // A palavra mascarada que aparece na tela (ex: ['_', 'A', '_', 'A'])
    private char[] palavraMascarada;

    // Conjunto de letras já usadas (Set não permite repetidos!)
    private Set<Character> letrasUsadas;

    // Flags de controle
    private boolean jogoAcabou;
    private String mensagemResultado;

    public Jogada(Jogadores jogadores, Palavra bancoDePalavras) {
        this.jogadores = jogadores;
        this.bancoDePalavras = bancoDePalavras;
        this.letrasUsadas = new HashSet<>(); // HashSet é super rápido para buscas
    }

    // 1. PREPARA O TERRENO (Reset)
    public void iniciarNovaRodada(String categoria) {
        // Sorteia a palavra na classe Palavra
        bancoDePalavras.sortear(categoria);

        // --- BLINDAGEM CONTRA ERRO NULL ---
        if (bancoDePalavras.getPalavraSecreta() == null) {
            System.err.println("Erro crítico: Palavra nula. Reiniciando sorteio.");
            bancoDePalavras.sortear("GERAL"); // Fallback de segurança
        }

        // Limpa estados anteriores
        this.letrasUsadas.clear();
        this.jogoAcabou = false;
        this.mensagemResultado = "";

        // Zera erros da rodada nos jogadores
        jogadores.getJogador1().zerarErros();
        jogadores.getJogador2().zerarErros();

        // Cria a máscara: Transforma "JAVA" em ['_', '_', '_', '_']
        String secreta = bancoDePalavras.getPalavraSecreta();
        this.palavraMascarada = new char[secreta.length()];
        Arrays.fill(this.palavraMascarada, '_'); // Preenche tudo com underline
    }

    // 2. A LÓGICA PRINCIPAL (Onde a mágica acontece)
    public boolean tentarLetra(char letra) throws LetraJaTentadaException {
        if (jogoAcabou) return false;

        letra = Character.toUpperCase(letra);

        // Regra 1: Não pode repetir letra
        if (letrasUsadas.contains(letra)) {
            throw new LetraJaTentadaException("A letra '" + letra + "' já foi tentada!");
        }
        letrasUsadas.add(letra);

        boolean acertou = false;
        String secreta = bancoDePalavras.getPalavraSecreta();

        // Regra 2: Varre a palavra procurando a letra
        // Ex: Se a palavra é B A N A N A e digito A, ele preenche os índices 1, 3 e 5.
        for (int i = 0; i < secreta.length(); i++) {
            if (secreta.charAt(i) == letra) {
                palavraMascarada[i] = letra; // Revela a letra na máscara
                acertou = true;
            }
        }

        Jogador jogadorAtual = jogadores.getJogadorDaVez();

        // Regra 3: Atualiza placar
        if (acertou) {
            verificarVitoria(jogadorAtual);
        } else {
            jogadorAtual.registrarErro();
            verificarDerrota(jogadorAtual);
        }

        return acertou;
    }

    // 3. O "Tudo ou Nada" (Chutar a palavra inteira)
    public boolean arriscarPalavra(String chute) {
        if (jogoAcabou || chute == null) return false;

        String secreta = bancoDePalavras.getPalavraSecreta();
        Jogador jogadorAtual = jogadores.getJogadorDaVez();

        if (chute.equalsIgnoreCase(secreta)) {
            // Se acertou, revela tudo e dá vitória
            this.palavraMascarada = secreta.toCharArray();
            this.jogoAcabou = true;
            this.mensagemResultado = "VITÓRIA ÉPICA! " + jogadorAtual.getNome() + " acertou o chute!";
            jogadorAtual.registrarVitoria();
            return true;
        } else {
            // Se errou, conta como erro no boneco
            jogadorAtual.registrarErro();
            verificarDerrota(jogadorAtual);
            return false;
        }
    }

    // --- MÉTODOS AUXILIARES (Privados para organizar o código) ---

    private void verificarVitoria(Jogador j) {
        // Se não tiver mais nenhum underline ('_'), ganhou!
        boolean ganhou = true;
        for (char c : palavraMascarada) {
            if (c == '_') {
                ganhou = false;
                break;
            }
        }

        if (ganhou) {
            jogoAcabou = true;
            j.registrarVitoria();
            mensagemResultado = "PARABÉNS! O vencedor foi: " + j.getNome();
        }
    }

    private void verificarDerrota(Jogador j) {
        if (j.getErrosAtuais() >= 6) {
            jogoAcabou = true;
            mensagemResultado = j.getNome() + " foi enforcado. FIM DE JOGO!";
        }
    }

    // --- GETTERS (Para a interface gráfica) ---

    public String getPalavraOcultaFormatada() {
        // Transforma ['J', 'A', '_', 'A'] em "J A _ A" (com espaços)
        StringBuilder sb = new StringBuilder();
        for (char c : palavraMascarada) {
            sb.append(c).append(" ");
        }
        return sb.toString();
    }

    public String getLetrasUsadasFormatada() {
        if (letrasUsadas.isEmpty()) return "Nenhuma";
        List<Character> lista = new ArrayList<>(letrasUsadas);
        Collections.sort(lista); // Ordena alfabeticamente para ficar bonito
        return lista.toString();
    }

    public boolean isJogoAcabou() { return jogoAcabou; }
    public String getResultado() { return mensagemResultado; }
    public Palavra getPalavra() { return bancoDePalavras; }
}