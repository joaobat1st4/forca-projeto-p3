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

    // --- ELEMENTOS DA TELA (View) ---
    @FXML private TextField inputNome1;
    @FXML private TextField inputNome2;
    @FXML private CheckBox chkModoSingle; // Checkbox "Jogar Sozinho"

    // --- 1. INICIALIZAÇÃO (Configura a tela antes de aparecer) ---
    @FXML
    public void initialize() {
        // LISTENER: Fica "ouvindo" se o usuário marcou ou desmarcou a caixinha.
        // Se mudou, ele executa o código abaixo automaticamente.
        chkModoSingle.selectedProperty().addListener((observable, oldValue, isSelected) -> {
            atualizarCampos(isSelected);
        });
    }

    // Método auxiliar para travar/destravar o campo do Jogador 2
    private void atualizarCampos(boolean isSinglePlayer) {
        if (isSinglePlayer) {
            inputNome2.setDisable(true); // Trava o campo
            inputNome2.setText("");      // Limpa o texto
            inputNome2.setPromptText("(Modo 1 Jogador)");
        } else {
            inputNome2.setDisable(false); // Destrava
            inputNome2.setPromptText("Nome do Jogador 2");
        }
    }

    // --- 2. AÇÕES DOS BOTÕES (Categorias) ---

    // Cada botão chama o método central passando sua categoria específica
    @FXML public void onBotaoFrutas(ActionEvent e)   { processarEntrada(e, "FRUTAS"); }
    @FXML public void onBotaoAnimais(ActionEvent e)  { processarEntrada(e, "ANIMAIS"); }
    @FXML public void onBotaoPaises(ActionEvent e)   { processarEntrada(e, "PAISES"); }
    @FXML public void onBotaoObjetos(ActionEvent e)  { processarEntrada(e, "OBJETOS"); }
    @FXML public void onBotaoAleatorio(ActionEvent e){ processarEntrada(e, "ALEATORIO"); }

    // --- 3. LÓGICA CENTRAL (O "Cérebro" da entrada) ---

    private void processarEntrada(ActionEvent event, String categoria) {
        try {
            // Verifica qual modo de jogo foi escolhido
            if (chkModoSingle.isSelected()) {
                iniciarSinglePlayer(event, categoria);
            } else {
                iniciarMultiplayer(event, categoria);
            }
        } catch (NomeInvalidoException e) {
            mostrarAlerta("Nome Inválido", e.getMessage());
        } catch (IOException e) {
            mostrarAlerta("Erro Crítico", "Não foi possível carregar a tela do jogo.");
            e.printStackTrace(); // Útil para você ver o erro no console
        }
    }

    // --- 4. FLUXO MULTIPLAYER (2 Jogadores) ---
    private void iniciarMultiplayer(ActionEvent event, String categoria) throws NomeInvalidoException, IOException {
        String n1 = inputNome1.getText().trim();
        String n2 = inputNome2.getText().trim();

        // Validação: Nomes não podem ser vazios
        if (n1.isEmpty() || n2.isEmpty()) {
            throw new NomeInvalidoException("Por favor, preencha os DOIS nomes!");
        }

        // Carrega a tela de Multiplayer
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/forca/multiplayer-view.fxml"));
        Parent root = loader.load();

        // Passa os dados para o próximo Controller
        MultiplayerController controller = loader.getController();
        controller.configurarPartida(new Jogadores(n1, n2), categoria);

        trocarCena(event, root);
    }

    // --- 5. FLUXO SINGLE PLAYER (1 Jogador) ---
    private void iniciarSinglePlayer(ActionEvent event, String categoria) throws NomeInvalidoException, IOException {
        String n1 = inputNome1.getText().trim();

        if (n1.isEmpty()) {
            throw new NomeInvalidoException("Por favor, digite seu nome!");
        }

        // Carrega a tela de Single Player (assumindo que você criou esse FXML)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/forca/single-view.fxml"));
        Parent root = loader.load();

        // Passa os dados para o próximo Controller
        SinglePlayerController controller = loader.getController();
        controller.iniciarPartida(n1, categoria);

        trocarCena(event, root);
    }

    // --- MÉTODOS AUXILIARES (Para não repetir código) ---

    private void trocarCena(ActionEvent event, Parent root) {
        // Pega a janela atual (Stage) baseada no botão que foi clicado
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Troca a cena (Scene) antiga pela nova
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING); // Ícone de alerta amarelo
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}