package forca.model;

// Exceção usada na Tela Inicial.
// Serve para bloquear o começo do jogo se o usuário não digitar os nomes.
public class NomeInvalidoException extends Exception {

    public NomeInvalidoException(String mensagem) {
        // Repassa a mensagem (ex: "O nome não pode ser vazio!") para a classe mãe
        super(mensagem);
    }
}