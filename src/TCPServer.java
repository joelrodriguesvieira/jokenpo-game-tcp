import java.io.*;
import java.net.*;

import Enums.MensagemEnum;

public class TCPServer {
    private static final int PORT = 80;
   
    private static Jogada jogada1 = new Jogada();
    private static Jogada jogada2 = new Jogada();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Aguardando jogadores...");

            // Aceitar conexões dos dois jogadores de forma independente
            Socket player1Socket = serverSocket.accept();
            System.out.println("Jogador 1 conectado.");
            ObjectOutputStream outPlayer1 = new ObjectOutputStream(player1Socket.getOutputStream());
            ObjectInputStream inPlayer1 = new ObjectInputStream(player1Socket.getInputStream());

            // Iniciar a interação com o Jogador 1
            Thread player1Thread = new Thread(new PlayerHandler(player1Socket, "Jogador 1", jogada1, outPlayer1, inPlayer1));
            player1Thread.start();

            // Aceitar conexão do segundo jogador em paralelo
            Socket player2Socket = serverSocket.accept();
            System.out.println("Jogador 2 conectado.");
            ObjectOutputStream outPlayer2 = new ObjectOutputStream(player2Socket.getOutputStream());
            ObjectInputStream inPlayer2 = new ObjectInputStream(player2Socket.getInputStream());

            // Iniciar a interação com o Jogador 2
            Thread player2Thread = new Thread(new PlayerHandler(player2Socket, "Jogador 2", jogada2, outPlayer2, inPlayer2));
            player2Thread.start();

            boolean jogarNovamente = true;

            while (jogarNovamente) {
                // Esperar as threads dos jogadores terminarem
                player1Thread.join();
                player2Thread.join();

                // Determinar o vencedor e enviar o resultado
                String resultado = determinarVencedor(jogada1, jogada2);
                enviarResultado(outPlayer1, resultado, jogada1, jogada2);
                enviarResultado(outPlayer2, resultado, jogada1, jogada2);

                // Perguntar aos jogadores se querem jogar novamente
                outPlayer1.writeObject(MensagemEnum.PERGUNTARJOGARNOVAMENTE.getMensagem());
                outPlayer2.writeObject(MensagemEnum.PERGUNTARJOGARNOVAMENTE.getMensagem());
                outPlayer1.flush();
                outPlayer2.flush();

                // Coletar respostas dos dois jogadores
                String resposta1 = (String) inPlayer1.readObject();
                String resposta2 = (String) inPlayer2.readObject();

                // Verificar se ambos os jogadores querem continuar
                jogarNovamente = resposta1.equalsIgnoreCase("sim") && resposta2.equalsIgnoreCase("sim");

                if (!jogarNovamente) {
                    if (resposta1.equalsIgnoreCase("sim") && resposta2.equalsIgnoreCase("não")) {
                    	outPlayer1.writeObject(MensagemEnum.REJEITARJOGADA.getMensagem());
                        outPlayer1.flush();
                    } else if (resposta2.equalsIgnoreCase("sim") && resposta1.equalsIgnoreCase("não")) {
                        outPlayer2.writeObject(MensagemEnum.REJEITARJOGADA.getMensagem());
                        outPlayer2.flush();
                    }

                } else {
                	// Reiniciar estado das jogadas
                    jogada1 = new Jogada();
                    jogada2 = new Jogada();

                    // Reiniciar as threads para capturar novas jogadas
                    player1Thread = new Thread(new PlayerHandler(player1Socket, "Jogador 1", jogada1, outPlayer1, inPlayer1));
                    player2Thread = new Thread(new PlayerHandler(player2Socket, "Jogador 2", jogada2, outPlayer2, inPlayer2));

                    player1Thread.start();
                    player2Thread.start();
                	
                }

            }

            // Enviar mensagem de encerramento
            outPlayer1.writeObject(MensagemEnum.MESSAGEMFINAL.getMensagem());
            outPlayer2.writeObject(MensagemEnum.MESSAGEMFINAL.getMensagem());
            outPlayer1.flush();
            outPlayer2.flush();

            // Fechar conexões
            player1Socket.close();
            player2Socket.close();

        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String determinarVencedor(Jogada jogada1, Jogada jogada2) {
        if (jogada1 == null || jogada2 == null) {
            return MensagemEnum.JOGADAINVALIDAJOGADORES.getMensagem();
        }
        if (jogada1.getEscolha().equalsIgnoreCase(jogada2.getEscolha())) {
        	// enum jogada aqui
            return "Empate!";
        }
        switch (jogada1.getEscolha().toLowerCase()) {
        // enum jogada aqui
            case "pedra":
                return jogada2.getEscolha().equalsIgnoreCase("tesoura") ? jogada1.getJogador() + " venceu!" : jogada2.getJogador() + " venceu!";
             // enum jogada aqui
            case "papel":
                return jogada2.getEscolha().equalsIgnoreCase("pedra") ? jogada1.getJogador() + " venceu!" : jogada2.getJogador() + " venceu!";
             // enum jogada aqui
            case "tesoura":
                return jogada2.getEscolha().equalsIgnoreCase("papel") ? jogada1.getJogador() + " venceu!" : jogada2.getJogador() + " venceu!";
            default:
                return MensagemEnum.JOGADAINVALIDA.getMensagem();
        }
    }

    private static void enviarResultado(ObjectOutputStream out, String resultado, Jogada jogada1, Jogada jogada2) throws IOException {
        if (jogada1 != null && jogada2 != null) {
            out.writeObject("Jogada do " + jogada1.getJogador() + ": " + jogada1.getEscolha());
            out.writeObject("Jogada do " + jogada2.getJogador() + ": " + jogada2.getEscolha());
            out.writeObject(resultado);
        } else {
            out.writeObject(MensagemEnum.JOGOINCOMPLETO.getMensagem());
        }
        out.flush();
    }
}
