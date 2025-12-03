package forca.controller;

import forca.model.Jogada;
import forca.model.Jogadores;
import forca.model.LetraJaTentadaException;
import forca.model.Palavra;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Optional;

public class JogoController {

    @FXML private Label lblNomeJogador;
    @FXML private Label lblCategoria;
    @FXML private Label lblPalavraOculta;
    @FXML private Label lblErros;
    @FXML private Label lblMensagem;
    @FXML private TextField inputLetra;

    // Imagens do Boneco
    @FXML private ImageView imgCabeca;
    @FXML private ImageView imgTronco;
    @FXML private ImageView imgBracoE;
    @FXML private ImageView imgBracoD;
    @FXML private ImageView imgPernaE;
    @FXML private ImageView imgPernaD;

    // --- MUDANÇA 1: AGORA TEMOS DOIS JOGOS SEPARADOS ---
    private Jogada jogoP1; // O jogo do Jogador 1 (Palavra X)
    private Jogada jogoP2; // O jogo do Jogador 2 (Palavra Y)

    // Objeto que gerencia os turnos (compartilhado)
    private Jogadores jogadores;

    public void configurarPartida(Jogadores jogadores, String categoria) {
        this.jogadores = jogadores;

        // --- MUDANÇA 2: SORTEIA DUAS PALAVRAS DIFERENTES ---

        // Configura o jogo do Jogador 1
        Palavra bancoP1 = new Palavra();
        String catP1 = categoria.equals("ALEATORIO") ? bancoP1.getCategoriaAleatoria() : categoria;
        this.jogoP1 = new Jogada(jogadores, bancoP1);
        this.jogoP1.iniciarNovaRodada(catP1);

        // Configura o jogo do Jogador 2
        Palavra bancoP2 = new Palavra();
        String catP2 = categoria.equals("ALEATORIO") ? bancoP2.getCategoriaAleatoria() : categoria;
        this.jogoP2 = new Jogada(jogadores, bancoP2);
        this.jogoP2.iniciarNovaRodada(catP2);

        // Garante que o boneco comece invisível
        resetarBoneco();
        atualizarInterface();
    }

    // --- MUDANÇA 3: METODO PARA PEGAR O JOGO DA VEZ ---
    private Jogada getJogoAtual() {
        // Se o jogador da vez for o Jogador 1, retorna o jogoP1
        if (jogadores.getJogadorDaVez() == jogadores.getJogador1()) {
            return jogoP1;
        } else {
            return jogoP2;
        }
    }

    private void resetarBoneco() {
        imgCabeca.setVisible(false);
        imgTronco.setVisible(false);
        imgBracoE.setVisible(false);
        imgBracoD.setVisible(false);
        imgPernaE.setVisible(false);
        imgPernaD.setVisible(false);
    }

    @FXML
    public void onBotaoTentar() {
        String texto = inputLetra.getText();
        if (texto.isEmpty()) return;
        char letra = texto.charAt(0);

        // Pega o jogo de quem está jogando agora
        Jogada jogoAtual = getJogoAtual();

        try {
            boolean acertou = jogoAtual.tentarLetra(letra);
            if (acertou) {
                lblMensagem.setText("Muito bem! Acertou.");
                lblMensagem.setStyle("-fx-text-fill: green;");
            } else {
                lblMensagem.setText("Errou!");
                lblMensagem.setStyle("-fx-text-fill: red;");
            }
            verificarFimDeTurno();
        } catch (LetraJaTentadaException e) {
            lblMensagem.setText(e.getMessage());
            lblMensagem.setStyle("-fx-text-fill: orange;");
        }
        inputLetra.clear();
        inputLetra.requestFocus();
        atualizarInterface();
    }

    @FXML
    public void onBotaoChutar() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Chutar Palavra");
        dialog.setHeaderText("Valendo tudo ou nada!");
        dialog.setContentText("Qual é a palavra?");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(chute -> {
            Jogada jogoAtual = getJogoAtual();
            boolean acertou = jogoAtual.arriscarPalavra(chute);
            if (acertou) {
                finalizarJogo(true); // Vitória
            } else {
                lblMensagem.setText("Chute errado! Ponto para o boneco.");
                lblMensagem.setStyle("-fx-text-fill: red;");
                verificarFimDeTurno();
            }
            atualizarInterface();
        });
    }

    private void verificarFimDeTurno() {
        Jogada jogoAtual = getJogoAtual();

        if (jogoAtual.isJogoAcabou()) {
            atualizarInterface();
            // Se alguém ganhou ou perdeu, o jogo acaba
            // Aqui verificamos se foi vitória ou derrota baseada no resultado da string
            boolean vitoria = jogoAtual.getResultado().contains("VENCEU") || jogoAtual.getResultado().contains("VITÓRIA") || jogoAtual.getResultado().contains("PARABÉNS");
            finalizarJogo(vitoria);
        } else {
            // Se o jogo continua, troca o turno para o outro jogador
            jogadores.trocarTurno();

            // IMPORTANTE: Ao trocar o turno, precisamos atualizar a tela
            // para mostrar a palavra do OUTRO jogador imediatamente
            atualizarInterface();
        }
    }

    private void atualizarInterface() {
        Jogada jogoAtual = getJogoAtual();
        if (jogoAtual == null) return;

        // Pega a palavra (banco) correspondente a este jogo específico
        // Nota: Precisamos acessar a Palavra de dentro da Jogada.
        // Se você não tiver um getter para isso, vamos usar a categoria salva na Jogada ou ajustar.
        // Como a classe Jogada não expõe o objeto Palavra facilmente, vamos assumir que
        // a categoria exibida é a que passamos no início.

        lblNomeJogador.setText("Vez de: " + jogadores.getJogadorDaVez().getNome());

        // Mostra a palavra oculta DESTE jogador (ex: J1 vê "_ _ A", J2 vê "B _ _")
        lblPalavraOculta.setText(jogoAtual.getPalavraOcultaFormatada());

        int erros = jogadores.getJogadorDaVez().getErrosNaRodada();
        lblErros.setText("Erros: " + erros + "/6");

        // Atualiza boneco baseado nos erros DESTE jogador
        if (erros == 0) resetarBoneco();
        imgCabeca.setVisible(erros >= 1);
        imgTronco.setVisible(erros >= 2);
        imgBracoE.setVisible(erros >= 3);
        imgBracoD.setVisible(erros >= 4);
        imgPernaE.setVisible(erros >= 5);
        imgPernaD.setVisible(erros >= 6);
    }

    private void finalizarJogo(boolean vitoria) {
        Jogada jogoAtual = getJogoAtual();
        // Precisamos pegar a palavra secreta correta.
        // Como o acesso à Palavra está protegido na Jogada, vamos confiar na mensagem de resultado.

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fim de Jogo");
        alert.setHeaderText(jogoAtual.getResultado());

        // Mensagem final
        alert.setContentText("O jogo acabou!");

        alert.showAndWait();
        Stage stage = (Stage) lblNomeJogador.getScene().getWindow();
        stage.close();
    }
}