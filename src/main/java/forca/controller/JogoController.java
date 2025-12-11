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

public class JogoController {

    // --- PAINÉIS DE OVERLAY (JANELAS INTERNAS) ---
    @FXML private VBox paneGameOver;
    @FXML private Label lblResultadoGame;
    @FXML private Label lblSubtituloGame;
    @FXML private Label lblPalavraFinal;

    @FXML private VBox paneChutar; // Painel novo
    @FXML private TextField inputChute; // Campo de texto do chute

    // --- Label Central de Status ---
    @FXML private Label lblStatusTurno;

    // Elementos J1
    @FXML private Label lblNomeJogador; @FXML private Label lblCategoria;
    @FXML private Label lblPalavraOculta; @FXML private Label lblErros;
    @FXML private Label lblMensagem; @FXML private TextField inputLetra;
    @FXML private Label lblTentativas;
    @FXML private ImageView imgCabeca; @FXML private ImageView imgTronco;
    @FXML private ImageView imgBracoE; @FXML private ImageView imgBracoD;
    @FXML private ImageView imgPernaE; @FXML private ImageView imgPernaD;

    // Elementos J2
    @FXML private Label lblNomeJogador1; @FXML private Label lblCategoria1;
    @FXML private Label lblPalavraOculta1; @FXML private Label lblErros1;
    @FXML private Label lblMensagem2; @FXML private TextField inputLetra2;
    @FXML private Label lblTentativas1;
    @FXML private ImageView imgCabeca1; @FXML private ImageView imgTronco1;
    @FXML private ImageView imgBracoE1; @FXML private ImageView imgBracoD1;
    @FXML private ImageView imgPernaE1; @FXML private ImageView imgPernaD1;

    private Jogada jogoP1;
    private Jogada jogoP2;
    private Jogadores jogadores;

    @FXML
    public void initialize() {
        configurarCampoLimitado(inputLetra);
        configurarCampoLimitado(inputLetra2);

        // Garante que os painéis comecem escondidos
        if (paneGameOver != null) paneGameOver.setVisible(false);
        if (paneChutar != null) paneChutar.setVisible(false);
    }

    private void configurarCampoLimitado(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 1) {
                textField.setText(newValue.substring(newValue.length() - 1));
            }
        });
    }

    public void configurarPartida(Jogadores jogadores, String categoria) {
        this.jogadores = jogadores;

        Palavra bancoP1 = new Palavra();
        String catP1 = categoria.equals("ALEATORIO") ? bancoP1.getCategoriaAleatoria() : categoria;
        this.jogoP1 = new Jogada(jogadores, bancoP1);
        this.jogoP1.iniciarNovaRodada(catP1);

        Palavra bancoP2 = new Palavra();
        String catP2 = categoria.equals("ALEATORIO") ? bancoP2.getCategoriaAleatoria() : categoria;
        this.jogoP2 = new Jogada(jogadores, bancoP2);
        this.jogoP2.iniciarNovaRodada(catP2);

        lblNomeJogador.setText("Jogador: " + jogadores.getJogador1().getNome());
        lblNomeJogador1.setText("Jogador: " + jogadores.getJogador2().getNome());
        lblCategoria.setText("Cat: " + catP1);
        lblCategoria1.setText("Cat: " + catP2);

        resetarBonecosVisuais();
        atualizarInterface();
    }

    private Jogada getJogoAtual() {
        return (jogadores.getJogadorDaVez() == jogadores.getJogador1()) ? jogoP1 : jogoP2;
    }

    private void resetarBonecosVisuais() {
        imgCabeca.setVisible(false); imgTronco.setVisible(false); imgBracoE.setVisible(false); imgBracoD.setVisible(false); imgPernaE.setVisible(false); imgPernaD.setVisible(false);
        imgCabeca1.setVisible(false); imgTronco1.setVisible(false); imgBracoE1.setVisible(false); imgBracoD1.setVisible(false); imgPernaE1.setVisible(false); imgPernaD1.setVisible(false);
    }

    @FXML
    public void onBotaoTentar() {
        // Se algum painel estiver aberto, ignora cliques no fundo
        if (paneGameOver.isVisible() || paneChutar.isVisible()) return;

        boolean vezDoJ1 = (jogadores.getJogadorDaVez() == jogadores.getJogador1());
        TextField inputAtivo = vezDoJ1 ? inputLetra : inputLetra2;
        Label lblMsgAtiva = vezDoJ1 ? lblMensagem : lblMensagem2;

        String texto = inputAtivo.getText();
        if (texto.isEmpty()) return;

        char letra = texto.charAt(0);
        Jogada jogoAtual = getJogoAtual();

        try {
            boolean acertou = jogoAtual.tentarLetra(letra);
            if (acertou) {
                lblMsgAtiva.setText("Acertou!");
                lblMsgAtiva.setStyle("-fx-text-fill: green;");
            } else {
                lblMsgAtiva.setText("Errou!");
                lblMsgAtiva.setStyle("-fx-text-fill: red;");
            }
            verificarFimDeTurno();
        } catch (LetraJaTentadaException e) {
            lblMsgAtiva.setText("Letra repetida!");
            lblMsgAtiva.setStyle("-fx-text-fill: orange;");
            inputAtivo.clear();
            verificarFimDeTurno();
        }
        inputAtivo.clear();
        atualizarInterface();
    }

    // --- NOVA LÓGICA DE CHUTE (Sem Pop-up Nativo) ---

    @FXML
    public void onBotaoChutar() {
        // Abre o painel personalizado
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

        // Fecha o painel
        paneChutar.setVisible(false);

        Jogada jogoAtual = getJogoAtual();
        boolean acertou = jogoAtual.arriscarPalavra(chute);

        if (acertou) {
            finalizarJogo(true);
        } else {
            boolean vezDoJ1 = (jogadores.getJogadorDaVez() == jogadores.getJogador1());
            Label lblMsgAtiva = vezDoJ1 ? lblMensagem : lblMensagem2;
            lblMsgAtiva.setText("Chute errado!");
            lblMsgAtiva.setStyle("-fx-text-fill: red;");
            verificarFimDeTurno();
        }
        atualizarInterface();
    }

    private void verificarFimDeTurno() {
        Jogada jogoAtual = getJogoAtual();
        if (jogoAtual.isJogoAcabou()) {
            atualizarInterface();
            boolean vitoria = jogoAtual.getResultado().toUpperCase().contains("VENCEDOR") ||
                    jogoAtual.getResultado().toUpperCase().contains("PARABÉNS") ||
                    jogoAtual.getResultado().toUpperCase().contains("ACERTOU");
            finalizarJogo(vitoria);
        } else {
            jogadores.trocarTurno();
        }
    }

    private void atualizarInterface() {
        if (paneGameOver.isVisible()) return;

        String nomeVez = jogadores.getJogadorDaVez().getNome().toUpperCase();
        lblStatusTurno.setText("VEZ DE: " + nomeVez);

        // J1
        lblPalavraOculta.setText(jogoP1.getPalavraOcultaFormatada());
        int erros1 = jogadores.getJogador1().getErrosNaRodada();
        lblErros.setText("Erros: " + erros1 + "/6");
        lblTentativas.setText("Usadas: " + jogoP1.getLetrasTentadasFormatada());
        atualizarBonecoJ1(erros1);

        // J2
        lblPalavraOculta1.setText(jogoP2.getPalavraOcultaFormatada());
        int erros2 = jogadores.getJogador2().getErrosNaRodada();
        lblErros1.setText("Erros: " + erros2 + "/6");
        lblTentativas1.setText("Usadas: " + jogoP2.getLetrasTentadasFormatada());
        atualizarBonecoJ2(erros2);

        // Foco
        boolean vezDoJ1 = (jogadores.getJogadorDaVez() == jogadores.getJogador1());
        inputLetra.setDisable(!vezDoJ1);
        inputLetra2.setDisable(vezDoJ1);

        if (vezDoJ1) {
            inputLetra.requestFocus();
        } else {
            inputLetra2.requestFocus();
        }
    }

    private void atualizarBonecoJ1(int erros) {
        if(erros == 0) return;
        imgCabeca.setVisible(erros >= 1); imgTronco.setVisible(erros >= 2); imgBracoE.setVisible(erros >= 3); imgBracoD.setVisible(erros >= 4); imgPernaE.setVisible(erros >= 5); imgPernaD.setVisible(erros >= 6);
    }

    private void atualizarBonecoJ2(int erros) {
        if(erros == 0) return;
        imgCabeca1.setVisible(erros >= 1); imgTronco1.setVisible(erros >= 2); imgBracoE1.setVisible(erros >= 3); imgBracoD1.setVisible(erros >= 4); imgPernaE1.setVisible(erros >= 5); imgPernaD1.setVisible(erros >= 6);
    }

    private void finalizarJogo(boolean vitoria) {
        Jogada jogoAtual = getJogoAtual();

        if (vitoria) {
            lblResultadoGame.setText("VITÓRIA!");
            lblResultadoGame.setStyle("-fx-text-fill: #2ecc71; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        } else {
            lblResultadoGame.setText("DERROTA!");
            lblResultadoGame.setStyle("-fx-text-fill: #e74c3c; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        }

        lblSubtituloGame.setText(jogoAtual.getResultado());
        lblPalavraFinal.setText("Palavras: " + jogoP1.getPalavra().getPalavraSecreta() + " / " + jogoP2.getPalavra().getPalavraSecreta());

        paneGameOver.setVisible(true);
        paneGameOver.toFront();
    }

    @FXML
    public void onBotaoVoltarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forca/inicio-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblStatusTurno.getScene().getWindow();
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