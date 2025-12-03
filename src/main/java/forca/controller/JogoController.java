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

    // --- Label Central de Status ---
    @FXML private Label lblStatusTurno;

    // Elementos J1
    @FXML private Label lblNomeJogador;
    @FXML private Label lblCategoria;
    @FXML private Label lblPalavraOculta;
    @FXML private Label lblErros;
    @FXML private Label lblMensagem;
    @FXML private TextField inputLetra;
    @FXML private Label lblTentativas;

    // Boneco J1
    @FXML private ImageView imgCabeca; @FXML private ImageView imgTronco;
    @FXML private ImageView imgBracoE; @FXML private ImageView imgBracoD;
    @FXML private ImageView imgPernaE; @FXML private ImageView imgPernaD;

    // Elementos J2
    @FXML private Label lblNomeJogador1;
    @FXML private Label lblCategoria1;
    @FXML private Label lblPalavraOculta1;
    @FXML private Label lblErros1;
    @FXML private Label lblMensagem2;
    @FXML private TextField inputLetra2;
    @FXML private Label lblTentativas1;

    // Boneco J2
    @FXML private ImageView imgCabeca1; @FXML private ImageView imgTronco1;
    @FXML private ImageView imgBracoE1; @FXML private ImageView imgBracoD1;
    @FXML private ImageView imgPernaE1; @FXML private ImageView imgPernaD1;

    // Lógica
    private Jogada jogoP1;
    private Jogada jogoP2;
    private Jogadores jogadores;

    // --- NOVO: MÉTODO QUE RODA AUTOMATICAMENTE AO ABRIR A TELA ---
    @FXML
    public void initialize() {
        // Configura o limite de 1 caractere para os dois campos
        configurarCampoLimitado(inputLetra);
        configurarCampoLimitado(inputLetra2);
    }

    // Método auxiliar para limitar o texto a 1 letra
    private void configurarCampoLimitado(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Se o novo texto tiver mais de 1 caractere (ex: digitou "AB")
            if (newValue.length() > 1) {
                // Mantém apenas o último caractere digitado (fica "B")
                textField.setText(newValue.substring(newValue.length() - 1));
            }
        });
    }
    // -------------------------------------------------------------

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
        if (jogadores.getJogadorDaVez() == jogadores.getJogador1()) {
            return jogoP1;
        } else {
            return jogoP2;
        }
    }

    private void resetarBonecosVisuais() {
        imgCabeca.setVisible(false); imgTronco.setVisible(false);
        imgBracoE.setVisible(false); imgBracoD.setVisible(false);
        imgPernaE.setVisible(false); imgPernaD.setVisible(false);

        imgCabeca1.setVisible(false); imgTronco1.setVisible(false);
        imgBracoE1.setVisible(false); imgBracoD1.setVisible(false);
        imgPernaE1.setVisible(false); imgPernaD1.setVisible(false);
    }

    @FXML
    public void onBotaoTentar() {
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
            // --- AQUI ESTÁ A MENSAGEM QUE VOCÊ PEDIU ---
            lblMsgAtiva.setText("Letra já tentada!");
            lblMsgAtiva.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        }

        inputAtivo.clear();
        // Não precisamos de requestFocus aqui pois o atualizarInterface faz isso
        atualizarInterface();
    }

    @FXML
    public void onBotaoChutar() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Chutar Palavra");
        dialog.setHeaderText("Quem chuta é: " + jogadores.getJogadorDaVez().getNome());
        dialog.setContentText("Qual é a palavra?");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(chute -> {
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
        });
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
            // A interface atualiza o foco depois
        }
    }

    private void atualizarInterface() {
        String nomeVez = jogadores.getJogadorDaVez().getNome().toUpperCase();
        lblStatusTurno.setText("VEZ DE: " + nomeVez);

        // Atualiza J1
        lblPalavraOculta.setText(jogoP1.getPalavraOcultaFormatada());
        int erros1 = jogadores.getJogador1().getErrosNaRodada();
        lblErros.setText("Erros: " + erros1 + "/6");
        lblTentativas.setText("Usadas: " + jogoP1.getLetrasTentadasFormatada());
        atualizarBonecoJ1(erros1);

        // Atualiza J2
        lblPalavraOculta1.setText(jogoP2.getPalavraOcultaFormatada());
        int erros2 = jogadores.getJogador2().getErrosNaRodada();
        lblErros1.setText("Erros: " + erros2 + "/6");
        lblTentativas1.setText("Usadas: " + jogoP2.getLetrasTentadasFormatada());
        atualizarBonecoJ2(erros2);

        // Foco e Habilitação
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
        imgCabeca.setVisible(erros >= 1);
        imgTronco.setVisible(erros >= 2);
        imgBracoE.setVisible(erros >= 3);
        imgBracoD.setVisible(erros >= 4);
        imgPernaE.setVisible(erros >= 5);
        imgPernaD.setVisible(erros >= 6);
    }

    private void atualizarBonecoJ2(int erros) {
        if(erros == 0) return;
        imgCabeca1.setVisible(erros >= 1);
        imgTronco1.setVisible(erros >= 2);
        imgBracoE1.setVisible(erros >= 3);
        imgBracoD1.setVisible(erros >= 4);
        imgPernaE1.setVisible(erros >= 5);
        imgPernaD1.setVisible(erros >= 6);
    }

    private void finalizarJogo(boolean vitoria) {
        Jogada jogoAtual = getJogoAtual();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fim de Jogo");
        alert.setHeaderText(jogoAtual.getResultado());
        alert.setContentText("O jogo acabou!");
        alert.showAndWait();
        Stage stage = (Stage) lblNomeJogador.getScene().getWindow();
        stage.close();
    }
}