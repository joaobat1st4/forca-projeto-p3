import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Palavra banco = new Palavra();

        System.out.println("=========================================");
        System.out.println("      JOGO DA FORCA - PROJETO P3");
        System.out.println("=========================================\n");

        // 1. Configuração dos Jogadores
        System.out.print("Nome do Jogador 1: ");
        String n1 = scanner.nextLine();
        System.out.print("Nome do Jogador 2: ");
        String n2 = scanner.nextLine();

        Jogadores jogadores = new Jogadores(n1, n2);
        Jogada jogo = new Jogada(jogadores, banco);

        // 2. Menu de Seleção de Categoria
        System.out.println("\nEscolha a categoria:");
        System.out.println("[1] - FRUTAS");
        System.out.println("[2] - PAÍSES");
        System.out.println("[3] - OBJETOS");
        System.out.println("[4] - CORES");
        System.out.println("[0] - ALEATÓRIO");
        System.out.print("Opção: ");

        String escolha = scanner.nextLine();
        String categoriaEscolhida;

        switch (escolha) {
            case "1": categoriaEscolhida = "FRUTAS"; break;
            case "2": categoriaEscolhida = "PAÍSES"; break;
            case "3": categoriaEscolhida = "OBJETOS"; break;
            case "4": categoriaEscolhida = "CORES"; break;
            default:
                categoriaEscolhida = banco.getCategoriaAleatoria();
                System.out.println(">> Categoria Aleatória Selecionada <<");
                break;
        }

        jogo.iniciarNovaRodada(categoriaEscolhida);

        // 3. Loop do Jogo
        while (!jogo.isJogoAcabou()) {
            Jogador atual = jogadores.getJogadorDaVez();

            System.out.println("\n-----------------------------------------");
            System.out.println("VEZ DE: " + atual.getNome().toUpperCase());
            System.out.println("CATEGORIA: " + banco.getCategoria());
            System.out.println("SEUS ERROS: " + atual.getErrosNaRodada() + "/6");
            System.out.println("PALAVRA: " + jogo.getPalavraOcultaFormatada());
            System.out.println("-----------------------------------------");

            System.out.print("Digite uma letra (ou a palavra inteira para CHUTAR): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Digite algo válido!");
                continue;
            }

            // --- LÓGICA DE DECISÃO: LETRA OU CHUTE? ---

            if (input.length() > 1) {
                // SE DIGITOU MAIS DE UMA LETRA -> É CHUTE
                System.out.println(">> VOCÊ ARRISCOU UM CHUTE! <<");
                boolean acertouChute = jogo.arriscarPalavra(input);

                if (!acertouChute) {
                    System.out.println("XX CHUTE ERRADO! (Conta como erro) XX");
                }
                // Se não acabou o jogo, troca o turno
                if (!jogo.isJogoAcabou()) jogadores.trocarTurno();

            } else {
                // SE DIGITOU APENAS UMA LETRA -> É TENTATIVA NORMAL
                char letra = input.charAt(0);

                try {
                    boolean acertou = jogo.tentarLetra(letra);

                    if (acertou) System.out.println(">> ACERTOU A LETRA! <<");
                    else System.out.println("XX ERROU A LETRA! XX");

                    if (!jogo.isJogoAcabou()) jogadores.trocarTurno();

                } catch (LetraJaTentadaException e) {
                    System.out.println("!!! ERRO: " + e.getMessage());
                    System.out.println("Jogue novamente sem perder a vez.");
                }
            }
        }

        // 4. Fim de Jogo
        System.out.println("\n=========================================");
        System.out.println(jogo.getResultado());
        System.out.println("A palavra secreta era: " + banco.getPalavraSecreta());
        System.out.println("=========================================");

        scanner.close();
    }
}