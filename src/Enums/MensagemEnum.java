package Enums;

public enum MensagemEnum {
	MESSAGEMFINAL			("O jogo terminou. Até a próxima!"),
	PERGUNTARJOGARNOVAMENTE ("Você gostaria de jogar novamente? (Sim/Não)"),
	REJEITARJOGADA			("O outro jogador não quis jogar novamente."),
	JOGADAINVALIDAJOGADORES ("Jogada inválida. Ambos os jogadores devem fazer uma jogada."),
	JOGOINCOMPLETO			("Jogo não pôde ser completado."),
	JOGADAINVALIDA			("Jogada inválida"),
	COMANDOJOGADA			("Faça sua jogada (Pedra, Papel ou Tesoura):");
	
	private final String mensagem;

	MensagemEnum(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public String getMensagem() {
        return mensagem;
    }
}
