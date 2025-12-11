package forca.controller;

import forca.model.Jogada;
import forca.model.Jogadores;
import forca.model.LetraJaTentadaException;
import forca.model.Palavra;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SinglePlayerController {

    // --- PAINÉIS OVERLAY ---
    @FXML private VBox paneGameOver;
    @FXML private Label lblResultadoGame;
    @FXML private Label lblPalavraFinal;

    @FXML private VBox paneChutar;
    @FXML private TextField inputChute;

    // --- ELEMENTOS DO JOGO ---
    @FXML private Label lblNomeJogador;
    @FXML private Label lblCategoria;
    @FXML private Label lblPalavraOculta;
    @FXML private Label lblErros;
    @FXML private Label lblMensagem;
    @FXML private Label lblTentativas;
    @FXML private TextField inputLetra;

    // --- BONECO ---
    @FXML private ImageView imgCabeca; @FXML private ImageView imgTronco;
    @FXML private ImageView imgBracoE; @FXML private ImageView imgBracoD;
    @FXML private ImageView imgPernaE; @FXML private ImageView imgPernaD;

    private Jogada jogo;
    private Jogadores jogadorUnico;

    @FXML
    public void initialize() {
        // Limita o input a 1 letra
        inputLetra.textProperty().addListener((obs, oldV, newV) -> {
            if (newV.length() > 1) inputLetra.setText(newV.substring(newV.length() - 1));
        });

        // Esconde painéis ao iniciar
        if (paneGameOver != null) paneGameOver.setVisible(false);
        if (paneChutar != null) paneChutar.setVisible(false);
    }

    public void iniciarPartida(String nome, String categoria) {
        this.jogadorUnico = new Jogadores(nome, "Computador");

        Palavra banco = new Palavra();
        if (categoria.equals("ALEATORIO")) {
            categoria = banco.getCategoriaAleatoria();
        }

        this.jogo = new Jogada(jogadorUnico, banco);
        this.jogo.iniciarNovaRodada(categoria);

        lblNomeJogador.setText("Jogador: " + nome);
        lblCategoria.setText("Categoria: " + categoria);

        resetarBoneco();
        atualizarInterface();
    }

    @FXML
    public void onBotaoTentar() {
        if (paneGameOver.isVisible() || paneChutar.isVisible()) return;

        String texto = inputLetra.getText();
        if (texto.isEmpty()) return;
        char letra = texto.charAt(0);

        try {
            boolean acertou = jogo.tentarLetra(letra);
            if (acertou) {
                lblMensagem.setText("Acertou!");
                lblMensagem.setStyle("-fx-text-fill: lightgreen; -fx-font-weight: bold;");
            } else {
                lblMensagem.setText("Errou!");
                lblMensagem.setStyle("-fx-text-fill: #ff6b6b; -fx-font-weight: bold;");
            }
            verificarFimDeJogo();
        } catch (LetraJaTentadaException e) {
            lblMensagem.setText("Letra repetida!");
            lblMensagem.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        }
        inputLetra.clear();
        inputLetra.requestFocus();
        atualizarInterface();
    }

    // --- LÓGICA DE CHUTE PERSONALIZADO ---

    @FXML
    public void onBotaoChutar() {
        paneChutar.setVisible(true);
        paneChutar.toFront();
        inputChute.clear();
        inputChute.requestFocus();
    }

    @FXML
    public void onCancelarChute() {
        paneChutar.setVisible(false);
    }

    @FXML
    public void onConfirmarChute() {
        String chute = inputChute.getText();
        if (chute == null || chute.trim().isEmpty()) return;

        paneChutar.setVisible(false);

        boolean acertou = jogo.arriscarPalavra(chute);
        if (acertou) {
            finalizarJogo(true);
        } else {
            lblMensagem.setText("Chute errado! +1 Erro.");
            lblMensagem.setStyle("-fx-text-fill: red;");
            verificarFimDeJogo();
        }
        atualizarInterface();
    }

    private void verificarFimDeJogo() {
        if (jogo.isJogoAcabou()) {
            atualizarInterface();
            boolean vitoria = jogo.getResultado().toUpperCase().contains("VENCEDOR") ||
                    jogo.getResultado().toUpperCase().contains("PARABÉNS");
            finalizarJogo(vitoria);
        }
    }

    private void finalizarJogo(boolean vitoria) {
        if (vitoria) {
            lblResultadoGame.setText("VITÓRIA!");
            lblResultadoGame.setStyle("-fx-text-fill: #2ecc71; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        } else {
            lblResultadoGame.setText("GAME OVER");
            lblResultadoGame.setStyle("-fx-text-fill: #e74c3c; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        }

        lblPalavraFinal.setText("A palavra era: " + jogo.getPalavra().getPalavraSecreta());

        paneGameOver.setVisible(true);
        paneGameOver.toFront();
    }

    private void atualizarInterface() {
        if (paneGameOver.isVisible()) return;

        lblPalavraOculta.setText(jogo.getPalavraOcultaFormatada());
        lblTentativas.setText("Usadas: " + jogo.getLetrasTentadasFormatada());

        int erros = jogadorUnico.getJogador1().getErrosNaRodada();
        lblErros.setText("Erros: " + erros + "/6");

        if(erros == 0) return;
        imgCabeca.setVisible(erros >= 1); imgTronco.setVisible(erros >= 2); imgBracoE.setVisible(erros >= 3); imgBracoD.setVisible(erros >= 4); imgPernaE.setVisible(erros >= 5); imgPernaD.setVisible(erros >= 6);
    }

    private void resetarBoneco() {
        imgCabeca.setVisible(false); imgTronco.setVisible(false); imgBracoE.setVisible(false); imgBracoD.setVisible(false); imgPernaE.setVisible(false); imgPernaD.setVisible(false);
    }

    @FXML
    public void onBotaoVoltarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forca/inicio-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblNomeJogador.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onBotaoSair() {
        Platform.exit();
        System.exit(0);
    }
}