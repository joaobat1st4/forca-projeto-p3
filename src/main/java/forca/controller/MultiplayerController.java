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

public class MultiplayerController {

    // --- VARIÁVEIS DO SCENEBUILDER (VIEW) ---
    // Não mude os nomes aqui, senão quebra o FXML!

    // Status e Paineis
    @FXML private Label lblStatusTurno;
    @FXML private VBox paneGameOver;
    @FXML private Label lblResultadoGame, lblSubtituloGame, lblPalavraFinal;
    @FXML private VBox paneChutar;
    @FXML private TextField inputChute;

    // Elementos do Jogador 1 (Esquerda)
    @FXML private Label lblNomeJogador, lblCategoria, lblPalavraOculta, lblErros, lblMensagem, lblTentativas;
    @FXML private TextField inputLetra;
    @FXML private ImageView imgCabeca, imgTronco, imgBracoE, imgBracoD, imgPernaE, imgPernaD;

    // Elementos do Jogador 2 (Direita)
    @FXML private Label lblNomeJogador1, lblCategoria1, lblPalavraOculta1, lblErros1, lblMensagem2, lblTentativas1;
    @FXML private TextField inputLetra2;
    @FXML private ImageView imgCabeca1, imgTronco1, imgBracoE1, imgBracoD1, imgPernaE1, imgPernaD1;

    // --- LÓGICA (MODEL) ---
    private Jogadores jogadores;
    private Jogada jogoP1;
    private Jogada jogoP2;

    // 1. INICIALIZAÇÃO VISUAL
    @FXML
    public void initialize() {
        // Esconde os painéis de "Game Over" e "Chutar" ao abrir
        if (paneGameOver != null) paneGameOver.setVisible(false);
        if (paneChutar != null) paneChutar.setVisible(false);

        // Configura os campos para aceitarem apenas 1 letra
        limitarCampo(inputLetra);
        limitarCampo(inputLetra2);
    }

    private void limitarCampo(TextField campo) {
        campo.textProperty().addListener((obs, velho, novo) -> {
            if (novo.length() > 1) campo.setText(novo.substring(0, 1));
        });
    }

    // 2. CONFIGURAÇÃO DA PARTIDA (Chamado pela tela anterior)
    public void configurarPartida(Jogadores jogadores, String categoria) {
        this.jogadores = jogadores;

        // Inicia a lógica para cada jogador
        this.jogoP1 = new Jogada(jogadores, new Palavra());
        this.jogoP1.iniciarNovaRodada(categoria);

        this.jogoP2 = new Jogada(jogadores, new Palavra());
        this.jogoP2.iniciarNovaRodada(categoria);

        // Preenche os nomes e categorias na tela
        lblNomeJogador.setText(jogadores.getJogador1().getNome());
        lblCategoria.setText(jogoP1.getPalavra().getCategoriaAtual());

        lblNomeJogador1.setText(jogadores.getJogador2().getNome());
        lblCategoria1.setText(jogoP2.getPalavra().getCategoriaAtual());

        atualizarInterface(); // Primeira pintura da tela
    }

    // 3. AÇÃO: CLICAR EM "TENTAR"
    @FXML
    public void onBotaoTentar() {
        if (paneGameOver.isVisible() || paneChutar.isVisible()) return;

        // Descobre quem é o jogador da vez
        boolean vezDoJ1 = (jogadores.getJogadorDaVez() == jogadores.getJogador1());

        // Seleciona os controles certos (input e label de mensagem)
        TextField inputAtual = vezDoJ1 ? inputLetra : inputLetra2;
        Label msgAtual = vezDoJ1 ? lblMensagem : lblMensagem2;
        Jogada jogoAtual = vezDoJ1 ? jogoP1 : jogoP2;

        String texto = inputAtual.getText();
        if (texto.isEmpty()) return;

        try {
            boolean acertou = jogoAtual.tentarLetra(texto.charAt(0));

            // Feedback Visual (Verde ou Vermelho)
            msgAtual.setText(acertou ? "Acertou!" : "Errou!");
            msgAtual.setStyle(acertou ? "-fx-text-fill: green;" : "-fx-text-fill: red;");

            verificarProximoPasso(jogoAtual);

        } catch (LetraJaTentadaException e) {
            msgAtual.setText("Repetida!");
            msgAtual.setStyle("-fx-text-fill: orange;");
        }

        inputAtual.clear();
        atualizarInterface();
    }

    // 4. LÓGICA DE PASSAGEM DE TURNO
    private void verificarProximoPasso(Jogada jogoAtual) {
        if (jogoAtual.isJogoAcabou()) {
            boolean vitoria = jogoAtual.getResultado().contains("VENCEDOR") ||
                    jogoAtual.getResultado().contains("PARABÉNS") ||
                    jogoAtual.getResultado().contains("VITÓRIA");
            finalizarJogo(vitoria, jogoAtual.getResultado());
        } else {
            jogadores.trocarTurno();
        }
    }

    // 5. ATUALIZAÇÃO DA TELA (Refresh)
    private void atualizarInterface() {
        if (paneGameOver.isVisible()) return;

        // Atualiza Jogador 1
        lblPalavraOculta.setText(jogoP1.getPalavraOcultaFormatada());
        lblErros.setText("Erros: " + jogadores.getJogador1().getErrosAtuais() + "/6");
        lblTentativas.setText("Usadas: " + jogoP1.getLetrasUsadasFormatada());
        atualizarBoneco(jogadores.getJogador1().getErrosAtuais(), imgCabeca, imgTronco, imgBracoE, imgBracoD, imgPernaE, imgPernaD);

        // Atualiza Jogador 2
        lblPalavraOculta1.setText(jogoP2.getPalavraOcultaFormatada());
        lblErros1.setText("Erros: " + jogadores.getJogador2().getErrosAtuais() + "/6");
        lblTentativas1.setText("Usadas: " + jogoP2.getLetrasUsadasFormatada());
        atualizarBoneco(jogadores.getJogador2().getErrosAtuais(), imgCabeca1, imgTronco1, imgBracoE1, imgBracoD1, imgPernaE1, imgPernaD1);

        // Atualiza Status Central e Bloqueia campos
        String nomeVez = jogadores.getJogadorDaVez().getNome().toUpperCase();
        lblStatusTurno.setText("VEZ DE: " + nomeVez);

        boolean vezDoJ1 = (jogadores.getJogadorDaVez() == jogadores.getJogador1());
        inputLetra.setDisable(!vezDoJ1);
        inputLetra2.setDisable(vezDoJ1);

        // Foca no campo certo
        if (vezDoJ1) inputLetra.requestFocus(); else inputLetra2.requestFocus();
    }

    // Método auxiliar para ligar as partes do corpo
    private void atualizarBoneco(int erros, ImageView... partes) {
        // partes[0] é cabeça, partes[1] é tronco, etc.
        partes[0].setVisible(erros >= 1);
        partes[1].setVisible(erros >= 2);
        partes[2].setVisible(erros >= 3);
        partes[3].setVisible(erros >= 4);
        partes[4].setVisible(erros >= 5);
        partes[5].setVisible(erros >= 6);
    }

    // --- RECURSO DE CHUTAR A PALAVRA ---
    @FXML public void onBotaoChutar() {
        paneChutar.setVisible(true);
        paneChutar.toFront();
        inputChute.requestFocus();
    }

    @FXML public void onCancelarChute() { paneChutar.setVisible(false); }

    @FXML public void onConfirmarChute() {
        String chute = inputChute.getText();
        if (chute.isEmpty()) return;
        paneChutar.setVisible(false);

        boolean vezDoJ1 = (jogadores.getJogadorDaVez() == jogadores.getJogador1());
        Jogada jogoAtual = vezDoJ1 ? jogoP1 : jogoP2;

        boolean ganhou = jogoAtual.arriscarPalavra(chute);

        if (ganhou) {
            finalizarJogo(true, jogoAtual.getResultado());
        } else {
            // Se errou o chute
            Label msg = vezDoJ1 ? lblMensagem : lblMensagem2;
            msg.setText("Chute Errado!");
            msg.setStyle("-fx-text-fill: red;");
            verificarProximoPasso(jogoAtual);
        }
        atualizarInterface();
    }

    // --- FIM DE JOGO E NAVEGAÇÃO ---
    private void finalizarJogo(boolean vitoria, String mensagem) {
        paneGameOver.setVisible(true);
        paneGameOver.toFront();

        lblResultadoGame.setText(vitoria ? "VITÓRIA!" : "DERROTA!");
        lblResultadoGame.setStyle(vitoria ? "-fx-text-fill: green;" : "-fx-text-fill: red;");

        lblSubtituloGame.setText(mensagem);
        lblPalavraFinal.setText("Era: " + jogoP1.getPalavra().getPalavraSecreta() + " / " + jogoP2.getPalavra().getPalavraSecreta());
    }

    @FXML
    public void onBotaoVoltarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forca/inicio-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblStatusTurno.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void onBotaoSair() {
        Platform.exit();
        System.exit(0);
    }
}