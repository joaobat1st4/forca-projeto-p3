package forca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ForcaApp extends Application {

    @Override
    public void start(Stage palco) throws IOException {
        //FXML
        // O FXMLLoader vai na pasta resources ler o arquivo visual da tela inicial
        FXMLLoader carregarArquivo = new FXMLLoader(getClass().getResource("/forca/inicio-view.fxml"));

        // SCENE
        // Carrega o conteúdo visual e define o tamanho da janela (Largura x Altura)
        Scene cena = new Scene(carregarArquivo.load(), 1366, 768);

        // STAGE
        // Configurações da janela do sistema operacional
        palco.setTitle("Jogo da Forca");
        palco.setScene(cena);      // Coloca a cena dentro da janela
        palco.show();              // Mostra a janela
    }

    // O metodo main apenas dispara o metodo start() acima
    public static void main(String[] args) {
        launch();
    }
}