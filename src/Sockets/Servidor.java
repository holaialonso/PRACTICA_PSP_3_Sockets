package Sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	
	private static ServerSocket serverSocket;	
			
		
	//Método para finalizar la ejecución del servidor
	public void stop() throws IOException {		
		serverSocket.close();		
	}

	//Main
	public static void main(String[] args) {
		try {
			
			System.out.println("(Servidor) Servidor esperando conexiones");
			
			//Creo el socket del servidor
			serverSocket = new ServerSocket(8080);			
						
			//Bucle infinito para aceptar todas las conexión que entren
			while(true) {
				
				//Socket de la conexión
				Socket socket = serverSocket.accept();
				
				//Transmisión de datos
				DataInputStream input = new DataInputStream(socket.getInputStream());
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				
				
				//Una vez que tengo establecida la conexión le digo al cliente que me diga su nombre				
				output.writeUTF("(Servidor) Indica tu nombre");
				String nombreCliente = input.readUTF();				
				
				
				//Creo el hilo
				HiloServidor hilo = new HiloServidor(socket, input, output, nombreCliente);
				hilo.start();
				
			}
		} catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        // Cerrar el socket del servidor si es necesario
	        if (serverSocket != null && !serverSocket.isClosed()) {
	            try {
	                serverSocket.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        
	       //join de los hilos
	    }

	}

}
