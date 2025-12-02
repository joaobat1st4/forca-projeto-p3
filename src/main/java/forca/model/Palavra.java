package forca.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Palavra {

    private Map<String, List<String>> bancoDePalavras;
    private Random random;

    private String palavraSecreta;
    private String categoriaDaPalavra;

    public Palavra() {
        this.bancoDePalavras = new HashMap<>();
        this.random = new Random();
        carregarPalavrasDoArquivo();
    }

    private void carregarPalavrasDoArquivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader("palavras.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(":");
                if (partes.length == 2) {
                    String categoria = partes[0].toUpperCase().trim();
                    String palavra = partes[1].toUpperCase().trim();

                    bancoDePalavras.putIfAbsent(categoria, new ArrayList<>());
                    bancoDePalavras.get(categoria).add(palavra);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar palavras: " + e.getMessage());
        }
    }

    public void sortear(String categoria) {
        categoria = categoria.toUpperCase();
        if (bancoDePalavras.containsKey(categoria)) {
            List<String> palavras = bancoDePalavras.get(categoria);
            this.palavraSecreta = palavras.get(random.nextInt(palavras.size()));
            this.categoriaDaPalavra = categoria;
        } else {
            this.palavraSecreta = "JAVA";
            this.categoriaDaPalavra = "PADRAO";
        }
    }

    // PERMITE ESCOLHA ALEATÓRIA
    public String getCategoriaAleatoria() {
        if (bancoDePalavras.isEmpty()) return "PADRAO";
        List<String> categorias = new ArrayList<>(bancoDePalavras.keySet());
        return categorias.get(random.nextInt(categorias.size()));
    }

    public String getPalavraSecreta() {
        return palavraSecreta;
    }

    public String getCategoria() {
        return categoriaDaPalavra;
    }
}