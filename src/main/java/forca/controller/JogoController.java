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

    // --- IMAGENS DO BONECO (Separadas) ---
    @FXML private ImageView imgCabeca;
    @FXML private ImageView imgTronco;
    @FXML private ImageView imgBracoE; // BracoBoneco
    @FXML private ImageView imgBracoD; // BracoDireitoBoneco
    @FXML private ImageView imgPernaE; // PernaBoneco
    @FXML private ImageView imgPernaD; // PernaDireitaBoneco

    private Jogada jogo;
    private Palavra bancoDePalavras;

    public void configurarPartida(Jogadores jogadores, String categoria) {
        this.bancoDePalavras = new Palavra();
        if (categoria.equals("ALEATORIO")) {
            categoria = bancoDePalavras.getCategoriaAleatoria();
        }
        this.jogo = new Jogada(jogadores, bancoDePalavras);
        this.jogo.iniciarNovaRodada(categoria);

        // Garante que o boneco comece invisível
        resetarBoneco();
        atualizarInterface();
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

        try {
            boolean acertou = jogo.tentarLetra(letra);
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
            boolean acertou = jogo.arriscarPalavra(chute);
            if (acertou) {
                finalizarJogo();
            } else {
                lblMensagem.setText("Chute errado! Ponto para o boneco.");
                lblMensagem.setStyle("-fx-text-fill: red;");
                verificarFimDeTurno();
            }
            atualizarInterface();
        });
    }

    private void verificarFimDeTurno() {
        if (jogo.isJogoAcabou()) {
            // Atualiza interface uma última vez para mostrar o boneco completo se perdeu
            atualizarInterface();
            finalizarJogo();
        } else {
            jogo.getJogadores().trocarTurno();
        }
    }

    private void atualizarInterface() {
        if (jogo == null) return;

        lblNomeJogador.setText("Vez de: " + jogo.getJogadores().getJogadorDaVez().getNome());
        lblCategoria.setText("Categoria: " + bancoDePalavras.getCategoria());
        lblPalavraOculta.setText(jogo.getPalavraOcultaFormatada());

        int erros = jogo.getJogadores().getJogadorDaVez().getErrosNaRodada();
        lblErros.setText("Erros: " + erros + "/6");

        // --- LÓGICA DE MOSTRAR PARTES DO CORPO ---
        // Se mudou de jogador (erros == 0), esconde tudo.
        // Se é o mesmo jogador acumulando erros, vai mostrando progressivamente.

        if (erros == 0) resetarBoneco();
        if (erros >= 1) imgCabeca.setVisible(true);
        if (erros >= 2) imgTronco.setVisible(true);
        if (erros >= 3) imgBracoE.setVisible(true);
        if (erros >= 4) imgBracoD.setVisible(true);
        if (erros >= 5) imgPernaE.setVisible(true);
        if (erros >= 6) imgPernaD.setVisible(true);
    }

    private void finalizarJogo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fim de Jogo");
        alert.setHeaderText(jogo.getResultado());
        alert.setContentText("A palavra era: " + bancoDePalavras.getPalavraSecreta());
        alert.showAndWait();
        Stage stage = (Stage) lblNomeJogador.getScene().getWindow();
        stage.close();
    }
}