package Sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {

	private ServerSocket serverSocket;
	private Socket socket;
		
	//Método para crear el servidor + arrancarlo
	public Servidor (int puerto) throws IOException {
		
		serverSocket = new ServerSocket(puerto);
		
		while(true) {
			System.out.println("(Servidor) Esperando conexión");	
			
			//Una vez recibida la petición de conexión se crea el objeto socket
			socket = serverSocket.accept();
						
			System.out.println("(Servidor) Conexión establecida");
			new GestorProcesos(socket).start();
		}
	}
	
	//Método para finalizar la ejecución del servidor
	public void stop() throws IOException {
		
		System.out.println("(Servidor) Cerrando conexión");		
		
		socket.close(); //socket
		serverSocket.close(); //servidor
		
		System.out.println("(Servidor) Conexión cerrada");
	}

		
	//Método main
	public static void main(String args[]) {
		
		try {
			
			Servidor servidor = new Servidor(8080);	
			
			
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
