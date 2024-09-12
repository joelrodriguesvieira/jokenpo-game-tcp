import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Enums.JogadaEnum;
import Enums.MensagemEnum;

class PlayerHandler implements Runnable {
    private Socket socket;
    private String jogador;
    private Jogada jogada;  // Jogada sem AtomicReference
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public PlayerHandler(Socket socket, String jogador, Jogada jogada, ObjectOutputStream out, ObjectInputStream in) {
        this.socket = socket;
        this.jogador = jogador;
        this.jogada = jogada;
        this.out = out;
        this.in = in;
    }

    @Override
    public void run() {
        try {
            boolean jogadaValida = false;

            while (!jogadaValida) {
                out.writeObject("Faça sua jogada (Pedra, Papel ou Tesoura):");
                out.flush();

                try {
                	JogadaEnum escolha = (JogadaEnum) in.readObject();  // Converter para JogadaEnum

                    // Se a conversão foi bem-sucedida, configurar a jogada
                    jogada.setJogador(jogador);
                    jogada.setEscolha(escolha);  // Usar JogadaEnum diretamente
                    jogadaValida = true;

                } catch (IllegalArgumentException e) {
                    // Caso o jogador digite algo inválido, reenviar mensagem de erro
                	out.writeObject(MensagemEnum.JOGADAINVALIDA.getMensagem());
                    out.writeObject(MensagemEnum.COMANDOJOGADA.getMensagem());
                    out.flush();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
