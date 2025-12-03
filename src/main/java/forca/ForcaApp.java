package forca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ForcaApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Carrega o arquivo visual da TELA DE INÍCIO
        FXMLLoader fxmlLoader = new FXMLLoader(ForcaApp.class.getResource("/forca/inicio-view.fxml"));

        // Cria a janela com tamanho 1920x1080
        Scene scene = new Scene(fxmlLoader.load(), 1336, 768);

        stage.setTitle("Jogo da Forca - Projeto P3");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}