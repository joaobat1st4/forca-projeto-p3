import forca.model.Jogada;
import forca.model.Jogadores;
import forca.model.LetraJaTentadaException;
import forca.model.Palavra;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

public class ForcaTest {

    // TESTES BÁSICOS

    @Test
    public void deveIniciarJogoComPalavraValida() {
        Jogadores jogadores = new Jogadores("jogador1", "jogador2");
        Palavra palavra = new Palavra();
        Jogada jogada = new Jogada(jogadores, palavra);

        jogada.iniciarNovaRodada("GERAL");

        String secreta = palavra.getPalavraSecreta();
        // Remove espaços da máscara "_ _ _" para comparar tamanho
        String oculta = jogada.getPalavraOcultaFormatada().replace(" ", "");

        assertNotNull(secreta, "A palavra secreta não pode ser nula (Erro no arquivo ou sorteio)");
        assertEquals(secreta.length(), oculta.length(), "A quantidade de traços deve ser igual ao tamanho da palavra");
    }

    @Test
    public void deveAceitarLetraCorreta() throws LetraJaTentadaException {
        Jogadores jogadores = new Jogadores("jogador1", "jogador2");
        Palavra palavra = new Palavra();
        Jogada jogada = new Jogada(jogadores, palavra);
        jogada.iniciarNovaRodada("GERAL");

        // Descobre uma letra que realmente existe na palavra sorteada
        String secreta = palavra.getPalavraSecreta();
        char letraCerta = secreta.charAt(0);

        boolean result = jogada.tentarLetra(letraCerta);

        assertTrue(result, "O sistema deve retornar TRUE ao acertar uma letra");
        assertEquals(0, jogadores.getJogador1().getErrosNaRodada(), "Não deve contar erro se acertou");
    }

    @Test
    public void deveContarErroAoErrarLetra() throws LetraJaTentadaException {
        Jogadores jogadores = new Jogadores("jogador1", "jogador2");
        Palavra palavra = new Palavra();
        Jogada jogada = new Jogada(jogadores, palavra);
        jogada.iniciarNovaRodada("GERAL");

        // Tenta um número (que nunca existe em palavras) para garantir o erro
        boolean result = jogada.tentarLetra('1');

        assertFalse(result, "O sistema deve retornar FALSE ao errar");
        assertEquals(1, jogadores.getJogador1().getErrosNaRodada(), "O contador de erros deve subir para 1");
    }

    @Test
    public void deveLancarExcecaoSeRepetirLetra() {
        Jogadores jogadores = new Jogadores("jogador1", "jogador2");
        Palavra palavra = new Palavra();
        Jogada jogada = new Jogada(jogadores, palavra);
        jogada.iniciarNovaRodada("GERAL");

        try {
            jogada.tentarLetra('A'); // Primeira vez: OK
            jogada.tentarLetra('A'); // Segunda vez: DEVE FALHAR
            fail("O teste falhou: Deveria ter lançado LetraJaTentadaException");
        } catch (LetraJaTentadaException e) {
            // Se caiu aqui, o teste passou!
            assertEquals("A letra 'A' já foi tentada!", e.getMessage());
        }
    }

    // TESTES REGRAS DE NEGÓCIO E VITÓRIA

    @Test
    public void deveGanharAoChutarPalavraCerta() {
        Jogadores jogadores = new Jogadores("jogador1", "jogador2");
        Palavra palavra = new Palavra();
        Jogada jogada = new Jogada(jogadores, palavra);
        jogada.iniciarNovaRodada("GERAL");

        String segredo = palavra.getPalavraSecreta();

        boolean ganhou = jogada.arriscarPalavra(segredo);

        assertTrue(ganhou, "Deve retornar TRUE ao chutar a palavra correta");
        assertTrue(jogada.isJogoAcabou(), "O jogo deve acabar imediatamente após o chute certo");
        assertTrue(jogada.getResultado().contains("VITÓRIA") || jogada.getResultado().contains("PARABÉNS"),
                "Mensagem de vitória deve aparecer");
    }

    @Test
    public void deveErrarAoChutarPalavraIncorreta() {
        Jogadores jogadores = new Jogadores("jogador1", "jogador2");
        Palavra palavra = new Palavra();
        Jogada jogada = new Jogada(jogadores, palavra);
        jogada.iniciarNovaRodada("GERAL");

        boolean acertou = jogada.arriscarPalavra("PALAVRA_QUE_NAO_EXISTE_XYZ");

        assertFalse(acertou, "Chute errado deve retornar FALSE");
        assertEquals(1, jogadores.getJogador1().getErrosNaRodada(), "Chute errado deve contar como +1 erro");
        assertFalse(jogada.isJogoAcabou(), "O jogo NÃO deve acabar só com um erro (a menos que seja o 6º)");
    }

    @Test
    public void deveGanharAoCompletarPalavraLetraPorLetra() throws LetraJaTentadaException {
        Jogadores jogadores = new Jogadores("jogador1", "jogador2");
        Palavra palavra = new Palavra();
        Jogada jogada = new Jogada(jogadores, palavra);
        jogada.iniciarNovaRodada("GERAL");

        String secreta = palavra.getPalavraSecreta();

        // Simula um jogador perfeito que digita todas as letras da palavra
        for (char c : secreta.toCharArray()) {
            // Só tenta se ainda não tentou essa letra (para evitar a Exceção em palavras com letras repetidas como BANANA)
            if (!jogada.getLetrasTentadasFormatada().contains(String.valueOf(c).toUpperCase())) {
                jogada.tentarLetra(c);
            }
        }

        assertTrue(jogada.isJogoAcabou(), "O jogo deve acabar ao completar todas as letras");
        assertTrue(jogada.getResultado().contains("PARABÉNS"), "Deve exibir mensagem de vitória");
    }

    @Test
    public void devePerderAposSeisErros() throws LetraJaTentadaException {
        Jogadores jogadores = new Jogadores("jogador1", "jogador2");
        Palavra palavra = new Palavra();
        Jogada jogada = new Jogada(jogadores, palavra);
        jogada.iniciarNovaRodada("GERAL");

        // Erra 6 vezes de propósito
        char[] erros = {'1', '2', '3', '4', '5', '6'};
        for (char c : erros) {
            jogada.tentarLetra(c);
        }

        assertTrue(jogada.isJogoAcabou(), "O jogo deve encerrar após 6 erros");
        assertTrue(jogada.getResultado().contains("enforcado") || jogada.getResultado().contains("FIM"),
                "Mensagem deve ser de derrota/enforcamento");
    }

    // TESTES DE USABILIDADE

    @Test
    public void deveIgnorarMaiusculasEMinusculas() throws LetraJaTentadaException {
        Jogadores jogadores = new Jogadores("jogador1", "jogador2");
        Palavra palavra = new Palavra();
        Jogada jogada = new Jogada(jogadores, palavra);
        jogada.iniciarNovaRodada("GERAL");

        String secreta = palavra.getPalavraSecreta();
        char letraCerta = secreta.charAt(0);

        // Tenta a versão MINÚSCULA da letra. O sistema deve converter para MAIÚSCULA e aceitar.
        boolean resultado = jogada.tentarLetra(Character.toLowerCase(letraCerta));

        assertTrue(resultado, "O sistema deve aceitar letras minúsculas normalmente");
    }

    @Test
    public void naoDevePermitirJogarAposFimDeJogo() throws LetraJaTentadaException {
        Jogadores jogadores = new Jogadores("jogador1", "jogador2");
        Palavra palavra = new Palavra();
        Jogada jogada = new Jogada(jogadores, palavra);
        jogada.iniciarNovaRodada("GERAL");

        // Força o fim do jogo
        jogada.arriscarPalavra(palavra.getPalavraSecreta());

        // Tenta jogar mais uma vez
        boolean tentou = jogada.tentarLetra('Z');

        assertFalse(tentou, "Não deve ser possível tentar letras após o jogo acabar");
    }
}