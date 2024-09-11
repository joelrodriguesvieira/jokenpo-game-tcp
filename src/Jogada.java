// Jogada.java
import java.io.Serializable;

public class Jogada implements Serializable {
    private static final long serialVersionUID = 1L;
    private String jogador;
    private String escolha;

    public Jogada(String jogador, String escolha) {
        this.jogador = jogador;
        this.escolha = escolha;
    }

    public String getJogador() {
        return jogador;
    }

    public String getEscolha() {
        return escolha;
    }

    public boolean isValid() {
        return escolha.equalsIgnoreCase("pedra") ||
               escolha.equalsIgnoreCase("papel") ||
               escolha.equalsIgnoreCase("tesoura");
    }
}
