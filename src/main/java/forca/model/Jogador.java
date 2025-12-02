package forca.model;

public class Jogador {
    private String nome;
    private int acertosGeral;
    private int errosNaRodada;

    public Jogador(String nome) {
        this.nome = nome;
        this.acertosGeral = 0;
        this.errosNaRodada = 0;
    }

    public void zerarErrosDaRodada() {
        this.errosNaRodada = 0;
    }

    public void adicionarAcertoGeral() {
        this.acertosGeral++;
    }

    public void adicionarErroNaRodada() {
        this.errosNaRodada++;
    }

    public String getNome() {
        return nome;
    }

    public int getErrosNaRodada() {
        return errosNaRodada;
    }

    public int getAcertosGeral() {
        return acertosGeral;
    }
}