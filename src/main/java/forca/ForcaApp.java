package forca; // <--- Se sua pasta for "org.example", mude para "package org.example;"

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ForcaApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Carrega o arquivo visual da TELA DE INÍCIO
        // O caminho "/forca/inicio-view.fxml" deve bater com a pasta em resources
        FXMLLoader fxmlLoader = new FXMLLoader(ForcaApp.class.getResource("/forca/inicio-view.fxml"));

        // Cria a cena (Janela) com tamanho 600x450
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);

        stage.setTitle("Jogo da Forca - Projeto P3");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}