package forca.controller;

import forca.model.*;
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

    // VIEW (Elementos da Tela)

    // Paineis de Overlay (Fim de Jogo e Chute)
    @FXML
    private VBox paneGameOver;
    @FXML
    private Label lblResultadoGame, lblPalavraFinal;
    @FXML
    private VBox paneChutar;
    @FXML
    private TextField inputChute;

    // Elementos do Jogador
    @FXML
    private Label lblNomeJogador, lblCategoria, lblPalavraOculta, lblErros, lblMensagem, lblTentativas;
    @FXML
    private TextField inputLetra;

    // Imagens do Boneco
    @FXML
    private ImageView imgCabeca, imgTronco, imgBracoE, imgBracoD, imgPernaE, imgPernaD;

    // MODEL (Lógica)
    private Jogada jogo;
    private Jogadores jogadorUnico;

    // INICIALIZAÇÃO
    @FXML
    public void initialize() {
        // Esconde painéis extras
        if (paneGameOver != null) paneGameOver.setVisible(false);
        if (paneChutar != null) paneChutar.setVisible(false);

        // Limita o input a 1 letra apenas
        inputLetra.textProperty().addListener((obs, oldV, newV) -> {
            if (newV.length() > 1) inputLetra.setText(newV.substring(0, 1));
        });
    }

    // COMEÇANDO O JOGO
    public void iniciarPartida(String nome, String categoria) {
        // Criamos um Jogador 2 "Fantasma" (Computador) só para satisfazer a classe Jogadores
        this.jogadorUnico = new Jogadores(nome, "Computador");

        Palavra banco = new Palavra();

        // Inicia a lógica do jogo
        this.jogo = new Jogada(jogadorUnico, banco);
        this.jogo.iniciarNovaRodada(categoria);

        // Configura a tela inicial
        lblNomeJogador.setText("Jogador: " + nome);
        lblCategoria.setText("Categoria: " + jogo.getPalavra().getCategoriaAtual());

        atualizarInterface();
    }

    // AÇÃO PRINCIPAL (TENTAR LETRA)
    @FXML
    public void onBotaoTentar() {
        if (paneGameOver.isVisible() || paneChutar.isVisible()) return;

        String texto = inputLetra.getText();
        if (texto.isEmpty()) return;

        try {
            boolean acertou = jogo.tentarLetra(texto.charAt(0));

            if (acertou) {
                lblMensagem.setText("Acertou!");
                lblMensagem.setStyle("-fx-text-fill: green;");
            } else {
                lblMensagem.setText("Errou!");
                lblMensagem.setStyle("-fx-text-fill: red;");
            }

            verificarFimDeJogo();

        } catch (LetraJaTentadaException e) {
            lblMensagem.setText("Letra Repetida!");
            lblMensagem.setStyle("-fx-text-fill: orange;");
        }

        inputLetra.clear();
        inputLetra.requestFocus(); // Mantém o cursor no campo
        atualizarInterface();
    }

    // ATUALIZAÇÃO VISUAL
    private void atualizarInterface() {
        if (paneGameOver.isVisible()) return;

        lblPalavraOculta.setText(jogo.getPalavraOcultaFormatada());
        lblTentativas.setText("Usadas: " + jogo.getLetrasUsadasFormatada());

        // Atualiza erros e boneco
        int erros = jogadorUnico.getJogador1().getErrosAtuais();
        lblErros.setText("Erros: " + erros + "/6");

        // Usa o mesmo metodo inteligente do Multiplayer para ligar as imagens
        atualizarBoneco(erros, imgCabeca, imgTronco, imgBracoE, imgBracoD, imgPernaE, imgPernaD);
    }

    // LÓGICA DE FIM DE JOGO
    private void verificarFimDeJogo() {
        if (jogo.isJogoAcabou()) {
            boolean vitoria = jogo.getResultado().contains("PARABÉNS") ||
                    jogo.getResultado().contains("VITÓRIA");
            finalizarJogo(vitoria);
        }
    }

    // Metodo auxiliar para evitar 6 IFs
    private void atualizarBoneco(int erros, ImageView... partes) {
        for (int i = 0; i < partes.length; i++) {
            partes[i].setVisible(erros > i);
        }
    }

    // CHUTAR A PALAVRA
    @FXML
    public void onBotaoChutar() {
        paneChutar.setVisible(true);
        paneChutar.toFront();
        inputChute.requestFocus();
    }

    @FXML
    public void onCancelarChute() {
        paneChutar.setVisible(false);
    }

    @FXML
    public void onConfirmarChute() {
        String chute = inputChute.getText();
        if (chute.isEmpty()) return;
        paneChutar.setVisible(false);

        boolean acertou = jogo.arriscarPalavra(chute);
        if (acertou) {
            finalizarJogo(true);
        } else {
            lblMensagem.setText("Chute Errado! +1 Erro");
            lblMensagem.setStyle("-fx-text-fill: red;");
            verificarFimDeJogo();
        }
        atualizarInterface();
    }

    private void finalizarJogo(boolean vitoria) {
        paneGameOver.setVisible(true);
        paneGameOver.toFront();

        if (vitoria) {
            lblResultadoGame.setText("VITÓRIA!");
            lblResultadoGame.setStyle("-fx-text-fill: green;");
        } else {
            lblResultadoGame.setText("GAME OVER");
            lblResultadoGame.setStyle("-fx-text-fill: red;");
        }
        lblPalavraFinal.setText("A palavra era: " + jogo.getPalavra().getPalavraSecreta());
    }

    // NAVEGAÇÃO
    @FXML
    public void onBotaoVoltarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forca/inicio-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblNomeJogador.getScene().getWindow();
            stage.setScene(new Scene(root));
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