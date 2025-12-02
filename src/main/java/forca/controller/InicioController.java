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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class InicioController {

    @FXML private TextField inputNome1;
    @FXML private TextField inputNome2;

    @FXML
    public void onBotaoFrutas(ActionEvent event) {
        iniciarJogo(event, "FRUTAS");
    }

    @FXML
    public void onBotaoAnimais(ActionEvent event) {
        iniciarJogo(event, "ANIMAIS");
    }

    @FXML
    public void onBotaoPaises(ActionEvent event) {
        iniciarJogo(event, "PAISES");
    }

    @FXML
    public void onBotaoObjetos(ActionEvent event) {
        iniciarJogo(event, "OBJETOS");
    }

    @FXML
    public void onBotaoAleatorio(ActionEvent event) {
        iniciarJogo(event, "ALEATORIO");
    }

    private void iniciarJogo(ActionEvent event, String categoria) {
        String n1 = inputNome1.getText();
        String n2 = inputNome2.getText();

        try {
            validarNomes(n1, n2);

            // Cria os jogadores e carrega a próxima tela
            Jogadores jogadores = new Jogadores(n1, n2);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forca/jogo-view.fxml"));
            Parent root = loader.load();

            // Passa os dados para o JogoController
            JogoController jogoController = loader.getController();
            jogoController.configurarPartida(jogadores, categoria);

            // Troca a cena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Jogo da Forca - Partida em Andamento");
            stage.centerOnScreen();
            stage.show();

        } catch (NomeInvalidoException e) {
            mostrarAlerta("Atenção aos Nomes", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro Fatal", "Não foi possível carregar a tela do jogo.");
        }
    }

    private void validarNomes(String n1, String n2) throws NomeInvalidoException {
        if (n1 == null || n1.trim().isEmpty() || n2 == null || n2.trim().isEmpty()) {
            throw new NomeInvalidoException("Os nomes dos jogadores são obrigatórios!");
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}