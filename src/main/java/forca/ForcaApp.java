package forca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ForcaApp extends Application {

    @Override
    public void start(Stage palco) throws IOException {
        // 1. O ROTEIRO (FXML)
        // O FXMLLoader vai na pasta resources ler o arquivo visual da tela inicial
        FXMLLoader carregarArquivo = new FXMLLoader(getClass().getResource("/forca/inicio-view.fxml"));

        // 2. A CENA (Scene)
        // Carrega o conteúdo visual e define o tamanho da janela (Largura x Altura)
        // 1336x768 é uma resolução segura para a maioria dos notebooks
        Scene cena = new Scene(carregarArquivo.load(), 1366, 768);

        // 3. O PALCO (Stage)
        // Configurações da janela do sistema operacional
        palco.setTitle("Jogo da Forca - Projeto Final");
        palco.setResizable(false); // DICA: Bloqueia para o usuário não esticar e quebrar o layout
        palco.setScene(cena);      // Coloca a cena dentro da janela
        palco.show();              // "Abre as cortinas" (Mostra a janela)
    }

    // O metodo main apenas dispara o metodo start() acima
    public static void main(String[] args) {
        launch();
    }
}