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
            	out.writeObject(MensagemEnum.COMANDOJOGADA);
                out.flush();
                
                Object object = in.readObject();
//                System.out.println(object);
                
                if(object instanceof JogadaEnum) {
                	try {
                		JogadaEnum escolha = (JogadaEnum) object;                	
                		
                		jogada.setJogador(jogador);
                		jogada.setEscolha(escolha);
//                		System.out.println("A jogada foi válida!");
                		jogadaValida = true;
                		
                	} catch (IllegalArgumentException e) {
                		out.writeObject(MensagemEnum.JOGADAINVALIDA);
                		out.writeObject(MensagemEnum.COMANDOJOGADA);
                		out.flush();
                	}                	
                } 
//                else {
//                	System.out.println("Não chegou como jogadaEnum");
//                }
                
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
