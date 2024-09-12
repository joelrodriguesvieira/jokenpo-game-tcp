import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

                String escolha = ((String) in.readObject()).toLowerCase();

                if (escolha.equals("pedra") || escolha.equals("papel") || escolha.equals("tesoura")) {
                    jogada.setJogador(jogador);
                    jogada.setEscolha(escolha);
                    jogadaValida = true;
                } else {
                    out.writeObject("Jogada inválida. Por favor, escolha entre Pedra, Papel ou Tesoura.");
                    out.flush();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
