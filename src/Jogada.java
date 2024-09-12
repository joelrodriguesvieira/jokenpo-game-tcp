import java.io.Serializable;

import Enums.JogadaEnum;

public class Jogada implements Serializable {
    private static final long serialVersionUID = 1L;
    private String jogador;
    private JogadaEnum escolha;

    public Jogada(String jogador, JogadaEnum escolha) {
        this.jogador = jogador;
        this.escolha = escolha;
    }

    public Jogada() {
    }

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
        this.escolha = escolha;
    }
}
