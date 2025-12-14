package forca.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Palavra {

    // MAPA: Funciona como gavetas.
    // A etiqueta da gaveta é a Categoria (String).
    // Dentro da gaveta tem uma Lista de palavras (List<String>).
    private Map<String, List<String>> bancoDePalavras;
    private Random random;

    // Guarda os dados da rodada atual
    private String palavraSecreta;
    private String categoriaAtual;

    public Palavra() {
        this.bancoDePalavras = new HashMap<>();
        this.random = new Random();

        carregarDoArquivo();

        // SEGURANÇA: Se o arquivo der erro ou estiver vazio, carregamos palavras na memória
        // Isso garante que o jogo nunca abra "quebrado" para o professor.
        if (bancoDePalavras.isEmpty()) {
            carregarPalavrasDeEmergencia();
        }
    }

    // 1. Tenta ler o arquivo 'palavras.txt' linha por linha
    private void carregarDoArquivo() {
        File arquivo = new File("palavras.txt");

        // Se o arquivo não existe, sai do método e deixa o backup atuar
        if (!arquivo.exists()) return;

        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                // A linha vem assim: "FRUTAS:BANANA"
                // O split corta no símbolo ":" criando duas partes
                String[] partes = linha.split(":");

                if (partes.length == 2) {
                    String categoria = partes[0].toUpperCase().trim(); // "FRUTAS"
                    String palavra = partes[1].toUpperCase().trim();   // "BANANA"

                    adicionarAoBanco(categoria, palavra);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    // 2. Palavras de Backup (Hardcoded) para caso o arquivo falhe
    private void carregarPalavrasDeEmergencia() {
        adicionarAoBanco("GERAL", "COMPUTADOR");
        adicionarAoBanco("GERAL", "JAVA");
        adicionarAoBanco("FRUTAS", "BANANA");
    }

    // Método auxiliar para evitar repetição de código no Map
    private void adicionarAoBanco(String categoria, String palavra) {
        // Se a gaveta (categoria) ainda não existe, cria uma lista vazia para ela
        bancoDePalavras.putIfAbsent(categoria, new ArrayList<>());
        // Adiciona a palavra na gaveta correta
        bancoDePalavras.get(categoria).add(palavra);
    }

    // 3. Escolhe uma palavra baseada na categoria
    public void sortear(String categoria) {
        categoria = (categoria == null) ? "GERAL" : categoria.toUpperCase();

        // Se a categoria pedida não existe, tenta pegar uma aleatória
        if (!bancoDePalavras.containsKey(categoria)) {
            categoria = getCategoriaAleatoria();
        }

        // Pega a lista de palavras daquela categoria
        List<String> listaDePalavras = bancoDePalavras.get(categoria);

        // Sorteia um número aleatório baseado no tamanho da lista
        int indiceSorteado = random.nextInt(listaDePalavras.size());

        this.palavraSecreta = listaDePalavras.get(indiceSorteado);
        this.categoriaAtual = categoria;
    }

    // 4. Sorteia uma categoria qualquer (usado no botão "Aleatório")
    public String getCategoriaAleatoria() {
        if (bancoDePalavras.isEmpty()) return "GERAL";

        // Transforma as chaves do mapa (as categorias) em uma lista para poder sortear
        List<String> categorias = new ArrayList<>(bancoDePalavras.keySet());
        return categorias.get(random.nextInt(categorias.size()));
    }

    public String getPalavraSecreta() {
        return palavraSecreta;
    }

    public String getCategoriaAtual() {
        return categoriaAtual;
    }
}