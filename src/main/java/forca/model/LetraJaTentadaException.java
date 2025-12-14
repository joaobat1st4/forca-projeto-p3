package forca.model;

// EXTENDS EXCEPTION:
// Isso transforma essa classe comum em um "Erro Oficial" do Java.
// Ao herdar de 'Exception', ela vira uma "Checked Exception" (Exceção Checada).
// Isso obriga o programador a tratar esse erro com try-catch.
public class LetraJaTentadaException extends Exception {

    // Construtor: Recebe a mensagem (ex: "A letra A já foi usada")
    public LetraJaTentadaException(String mensagem) {
        // SUPER: Chama o construtor da classe pai (Exception)
        // Isso guarda a mensagem dentro do erro para podermos ler depois com .getMessage()
        super(mensagem);
    }
}