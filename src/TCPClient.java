import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Enums.JogadaEnum;
import Enums.MensagemEnum;
import Enums.RespostaEnum;

public class TCPClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 80;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                try {
                    Object serverMessage = input.readObject();
                    if (serverMessage == null) break;
                    
                    if(serverMessage instanceof MensagemEnum) {
                    	MensagemEnum mensagem = (MensagemEnum) serverMessage;
                    	System.out.println("Server: " + mensagem.mensagem);
                    	
                    	if (serverMessage.equals(MensagemEnum.COMANDOJOGADA)) {
                        	String jogadaEscolha;
                            JogadaEnum jogadaEnum = null;
                            
                            while (jogadaEnum == null) {
                                jogadaEscolha = consoleInput.readLine().toUpperCase();
//                                System.out.println(jogadaEscolha);
                                
                                try {
                                    jogadaEnum = JogadaEnum.valueOf(jogadaEscolha);
                                    
                                    if(jogadaEnum instanceof JogadaEnum) {
//                                    	System.out.println("jogadaEnum é objeto");
                                    }
                                    
                                    output.writeObject(jogadaEnum);
                                    output.flush();
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Jogada inválida. Escolha entre PEDRA, PAPEL ou TESOURA.");
                                }
                            }
                        } else if (serverMessage.equals(MensagemEnum.PERGUNTARJOGARNOVAMENTE)) {
                        	String respostaString;
                        	RespostaEnum respostaEnum = null;
                        	while(respostaEnum == null) {
                        		respostaString = consoleInput.readLine().toUpperCase();
                        		
                        		try {
                        			respostaEnum = RespostaEnum.valueOf(respostaString);
                        			output.writeObject(respostaEnum);
                        			output.flush();
    								
    							} catch (IllegalArgumentException e) {
    								System.out.println("Resposta inválida. Escolha entre SIM ou NÃO.");
    							}
                        	}    
                        }
                    } else {
                    	System.out.println(serverMessage);
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
