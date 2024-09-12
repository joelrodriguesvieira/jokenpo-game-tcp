import java.io.Serializable;

import Enums.JogadaEnum;

public class Jogada implements Serializable {
    private static final long serialVersionUID = 1L;
    private String jogador;
    private JogadaEnum escolha;

    // Construtor com parâmetros
    public Jogada(String jogador, JogadaEnum escolha) {
        this.jogador = jogador;
        this.escolha = escolha;
    }

    // Construtor padrão
    public Jogada() {
        // Construtor vazio caso precise de uma instância sem valores iniciais
    }

    // Getters e Setters
    public String getJogador() {
        return jogador;
    }

    public void setJogador(String jogador) {
        this.jogador = jogador;
    }

    public JogadaEnum getEscolha() {
        return escolha;
    }

    public void setEscolha(JogadaEnum escolha) {
        // Normaliza a escolha para minúsculas e armazena
        this.escolha = escolha;
    }

    // Método para validar se a escolha é válida
    public boolean isValid() {
        return "pedra".equals(escolha) || 
               "papel".equals(escolha) || 
               "tesoura".equals(escolha);
    }
}
