package forca.controller;

import forca.model.Jogadores;
import forca.model.NomeInvalidoException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class InicioController {

    @FXML private TextField inputNome1;
    @FXML private TextField inputNome2;
    @FXML private CheckBox chkModoSingle; // Nossa nova caixa de seleção

    // --- LÓGICA DE INICIALIZAÇÃO ---
    @FXML
    public void initialize() {
        // Ouve quando a caixa é marcada ou desmarcada
        chkModoSingle.selectedProperty().addListener((observable, oldValue, isSelected) -> {
            if (isSelected) {
                // MODO SINGLE: Desabilita e limpa o campo 2
                inputNome2.setDisable(true);
                inputNome2.setText("");
                inputNome2.setPromptText("(Desabilitado)");
            } else {
                // MODO MULTI: Habilita o campo 2
                inputNome2.setDisable(false);
                inputNome2.setPromptText("Nome do Jogador 2");
            }
        });
    }

    // --- AÇÕES DOS BOTÕES (Agora unificados!) ---

    @FXML
    public void onBotaoFrutas(ActionEvent event) {
        processarInicioJogo(event, "FRUTAS");
    }

    @FXML
    public void onBotaoAnimais(ActionEvent event) {
        processarInicioJogo(event, "ANIMAIS");
    }

    @FXML
    public void onBotaoPaises(ActionEvent event) {
        processarInicioJogo(event, "PAISES");
    }

    @FXML
    public void onBotaoObjetos(ActionEvent event) {
        processarInicioJogo(event, "OBJETOS");
    }

    @FXML
    public void onBotaoAleatorio(ActionEvent event) {
        processarInicioJogo(event, "ALEATORIO");
    }

    // --- LÓGICA CENTRAL DE DECISÃO ---
    private void processarInicioJogo(ActionEvent event, String categoria) {
        // Verifica se o checkbox está marcado
        if (chkModoSingle.isSelected()) {
            iniciarSinglePlayer(event, categoria);
        } else {
            iniciarMultiplayer(event, categoria);
        }
    }

    // Lógica para 2 Jogadores (Original)
    private void iniciarMultiplayer(ActionEvent event, String categoria) {
        String n1 = inputNome1.getText();
        String n2 = inputNome2.getText();

        try {
            if (n1 == null || n1.trim().isEmpty() || n2 == null || n2.trim().isEmpty()) {
                throw new NomeInvalidoException("Para 2 jogadores, preencha os dois nomes!");
            }

            Jogadores jogadores = new Jogadores(n1, n2);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forca/jogo-view.fxml"));
            Parent root = loader.load();

            JogoController controller = loader.getController();
            controller.configurarPartida(jogadores, categoria);

            trocarTela(event, root, "Jogo da Forca - Multiplayer");

        } catch (NomeInvalidoException | IOException e) {
            mostrarAlerta("Erro", e.getMessage());
            e.printStackTrace();
        }
    }

    // Lógica para 1 Jogador (Nova)
    private void iniciarSinglePlayer(ActionEvent event, String categoria) {
        String n1 = inputNome1.getText();

        try {
            if (n1 == null || n1.trim().isEmpty()) {
                throw new NomeInvalidoException("Preencha seu nome para jogar!");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forca/single-view.fxml"));
            Parent root = loader.load();

            SinglePlayerController controller = loader.getController();
            controller.iniciarPartida(n1, categoria);

            trocarTela(event, root, "Jogo da Forca - Single Player");

        } catch (NomeInvalidoException | IOException e) {
            mostrarAlerta("Erro", e.getMessage());
            e.printStackTrace();
        }
    }

    private void trocarTela(ActionEvent event, Parent root, String titulo) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        stage.centerOnScreen();
        stage.show();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}