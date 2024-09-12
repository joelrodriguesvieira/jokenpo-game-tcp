import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Enums.MensagemEnum;

public class TCPClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 80;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            Object serverMessage;
            while (true) {
                try {
                    serverMessage = input.readObject();
                    if (serverMessage == null) break;
                    System.out.println("Server: " + serverMessage);
                    
                    if (serverMessage.equals(MensagemEnum.COMANDOJOGADA.getMensagem())) {
                        String jogadaEscolha = consoleInput.readLine();
                        output.writeObject(jogadaEscolha);
                        output.flush();
                    } else if (serverMessage.equals(MensagemEnum.PERGUNTARJOGARNOVAMENTE.getMensagem())) {
                        String resposta = consoleInput.readLine();
                        output.writeObject(resposta);
                        output.flush();
                    }
                } catch (EOFException e) {
                    System.out.println("Conexão encerrada pelo servidor.");
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
