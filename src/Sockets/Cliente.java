package Sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Cliente{
	
	
	//Main
	public static void main(String[] args) throws IOException {
		
		String[] nombresClientes = {"Vilma", "Pedro", "Betty", "Pablo"};
		ArrayList<HiloCliente> clientes = new ArrayList();
		
		//Recorro los clientes y voy lanzando cada uno de los hilos, que realizará las acciones correspondientes
		for(int i=0; i<nombresClientes.length; i++) {
			
			clientes.add(new HiloCliente(nombresClientes[i], nombresClientes));
			clientes.get(i).start();			
			
		}			
		
	}
	
	
	
	
}
