import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Enums.JogadaEnum;
import Enums.MensagemEnum;
import Enums.RespostaEnum;

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
                outPlayer1.writeObject(MensagemEnum.PERGUNTARJOGARNOVAMENTE);
                outPlayer2.writeObject(MensagemEnum.PERGUNTARJOGARNOVAMENTE);
                outPlayer1.flush();
                outPlayer2.flush();

                Object resposta1 = inPlayer1.readObject();
                Object resposta2 = inPlayer2.readObject();
                
                if(resposta1 instanceof RespostaEnum && resposta2 instanceof RespostaEnum) {
                	// Coletar respostas dos dois jogadores
                	RespostaEnum respostaEnum1 = (RespostaEnum) resposta1;
                	RespostaEnum respostaEnum2 = (RespostaEnum) resposta2; 
                	
                	jogarNovamente = respostaEnum1 == RespostaEnum.SIM && respostaEnum2 == RespostaEnum.SIM;

                    if (!jogarNovamente) {
                        if (respostaEnum1 == RespostaEnum.SIM && respostaEnum2 == RespostaEnum.NÃO) {
                            outPlayer1.writeObject(MensagemEnum.REJEITARJOGADA);
                            outPlayer1.flush();
                        } else if (respostaEnum2 == RespostaEnum.SIM && respostaEnum1 == RespostaEnum.NÃO) {
                            outPlayer2.writeObject(MensagemEnum.REJEITARJOGADA);
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
                

                // Verificar se ambos os jogadores querem continuar
                

            }

            // Enviar mensagem de encerramento
            outPlayer1.writeObject(MensagemEnum.MESSAGEMFINAL);
            outPlayer2.writeObject(MensagemEnum.MESSAGEMFINAL);
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
            return MensagemEnum.JOGADAINVALIDAJOGADORES.mensagem;
        }
        
        JogadaEnum escolha1 = jogada1.getEscolha();
        JogadaEnum escolha2 = jogada2.getEscolha();
        
        if (escolha1 == escolha2) {
            return "Empate!";
        }
        
        switch (escolha1) {
        case PEDRA:
            return (escolha2 == JogadaEnum.TESOURA) ? jogada1.getJogador() + " venceu!" : jogada2.getJogador() + " venceu!";
        case PAPEL:
            return (escolha2 == JogadaEnum.PEDRA) ? jogada1.getJogador() + " venceu!" : jogada2.getJogador() + " venceu!";
        case TESOURA:
            return (escolha2 == JogadaEnum.PAPEL) ? jogada1.getJogador() + " venceu!" : jogada2.getJogador() + " venceu!";
        default:
            return MensagemEnum.JOGADAINVALIDA.mensagem;
    }
    }

    private static void enviarResultado(ObjectOutputStream out, String resultado, Jogada jogada1, Jogada jogada2) throws IOException {
        if (jogada1 != null && jogada2 != null) {
            out.writeObject("Jogada do " + jogada1.getJogador() + ": " + jogada1.getEscolha());
            out.writeObject("Jogada do " + jogada2.getJogador() + ": " + jogada2.getEscolha());
            out.writeObject(resultado);
        } else {
            out.writeObject(MensagemEnum.JOGOINCOMPLETO);
        }
        out.flush();
    }
}
