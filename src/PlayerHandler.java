import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Enums.JogadaEnum;
import Enums.MensagemEnum;

class PlayerHandler implements Runnable {
    private Socket socket;
    private String jogador;
    private Jogada jogada;
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
            	out.writeObject(MensagemEnum.COMANDOJOGADA.getMensagem());
                out.flush();

                try {
                	JogadaEnum escolha = (JogadaEnum) in.readObject();

                    jogada.setJogador(jogador);
                    jogada.setEscolha(escolha);
                    jogadaValida = true;

                } catch (IllegalArgumentException e) {
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
