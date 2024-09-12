import java.io.Serializable;

public class Jogada implements Serializable {
    private static final long serialVersionUID = 1L;
    private String jogador;
    private String escolha;

    // Construtor com parâmetros
    public Jogada(String jogador, String escolha) {
        this.jogador = jogador;
        setEscolha(escolha); // Use o setter para aplicar validação
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

    public String getEscolha() {
        return escolha;
    }

    public void setEscolha(String escolha) {
        // Normaliza a escolha para minúsculas e armazena
        this.escolha = escolha != null ? escolha.toLowerCase() : null;
    }

    // Método para validar se a escolha é válida
    public boolean isValid() {
        return "pedra".equals(escolha) || 
               "papel".equals(escolha) || 
               "tesoura".equals(escolha);
    }
}
