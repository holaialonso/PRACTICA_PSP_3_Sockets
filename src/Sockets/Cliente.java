package Sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Cliente {
	
	private String serverIP;
	private int serverPort;
	private Socket socket;
	private static DataInputStream input;
	private static DataOutputStream output;
	
	//Constructor
	public Cliente(String serverIP, int serverPort) {
		
		this.serverIP = serverIP;
		this.serverPort = serverPort;
	}

		
	//Método para iniciar el cliente
	public void start () throws UnknownHostException, IOException {
		System.out.println( "(Cliente) Estableciendo conexión");
		
		socket = new Socket();		
		InetSocketAddress addr = new InetSocketAddress("localhost",8080);
		
		socket.connect(addr);
		output = new DataOutputStream(socket.getOutputStream());
		input = new DataInputStream(socket.getInputStream());
		System.out.println("(Cliente) Conexión establecida");
	}
		
		
	//Método para parar el cliente
	public void stop () throws IOException {
		System.out.println( "(Cliente) Cerrando conexión");
		input.close();
		output.close();
		socket.close();
		System.out.println( "(Cliente) Conexión cerrada");
	}
		
		
	//Main
	public static void main(String args[]) {		
		
		Cliente cliente = new Cliente("localhost", 8080);
		
		try {
			
			//Nombres de los clientes
			String[] clientes = {"Vilma", "Pedro", "Betty", "Pablo"};
			
			//Recorro los clientes y los arranco
			for(int i=0; i<clientes.length; i++) {
				
				String opcion="";
				Boolean seguir=true;
				
				//Arranco el cliente
				cliente.start();
				
					//ACCIONES
						//1. Me identifico con mi nombre en el servidor
						sendNombreCliente(clientes[i]);
						
						//2. El cliente queda a la espera de la instrucción que le de el servidor						
						
							while (seguir) {
								 // Esperar hasta que el servidor envíe una acción a realizar
					            String accion = input.readUTF();
					            
					            if (accion == null) {
					                System.out.println("SERVIDOR desconectado.");
					                break; //Si el servidor se desconecta -> salgo del bucle
					            }
					            
					            //Dependiendo de las acciones que nos vaya mandando realizar el servidor
					            switch(accion) {
					            
						            case "MENU":
						            	opcion=makeMenu();
						            break;
						            
						            case "NUM":
						            	sendNumero();
						            break;
						            
						            case "NAME":
						            	sendNombreCliente();
						            break;
						            
						            default:
						            	System.out.println(accion);
						            break;
					            }
					            
					            
					            if((opcion.equals("e"))&&(opcion.length()>0)) {
					            	System.out.println("dentro de aquí");
									seguir=false;
								}
					        }
						
				
				
					//FIN ACCIONES
									
				
				if(!seguir) {
					System.out.println("cliente cerrado");
					cliente.stop();
				}
				
				
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	//Método para enviar el saludo al servidor
	private static void sendNombreCliente(String nombreCliente) throws IOException {
		
		System.out.println("(Cliente) ¡Hola servidor! Me llamo "+nombreCliente);
		output.writeUTF(nombreCliente);
	}
	
	
	//Método para mostrar el menú y enviar la acción seleccionada
	private static String makeMenu() throws IOException {
		
		String opcion="";
		
		while((!opcion.equals("a")&&(!opcion.equals("b"))&&(!opcion.equals("c"))&&(!opcion.equals("d")&&!opcion.equals("e")))) {
		
			String menu="Las opciones que tienes disponibles son las siguientes:\n"
					+ "a) Guardar un número en un fichero\n"
					+ "b) Ver la cantidad de números almacenados (total) por todos los usuarios\n"
					+ "c) Listar los números almacenados\n"
					+ "d) Ver los números que ha almacenado otro cliente\n"
					+ "e) Para terminar la ejecución\n"					
					+ "¿Qué acción vas a realizar?";	
			
			System.out.println(menu);
			
			//Leo la opción elegida por el cliente
			Scanner scanner = new Scanner(System.in);
			opcion = scanner.nextLine().toLowerCase();
		
		}
		
		//Envío la opción seleccionada al servidor -> si no quiero parar
		if(!opcion.equals("e")) {
			output.writeUTF(opcion);
		}
		
		return opcion;
		
	}
	
	
	//Método para recoger por teclado un número y enviarlo al servidor
	private static void sendNumero() throws IOException {
		
		String numero="";
		
		while(!numero.matches("-?\\d+(\\.\\d+)?")) {
			System.out.println("El servidor solicita que introduzcas un número");		
			Scanner scanner = new Scanner(System.in);
			numero = scanner.nextLine().toLowerCase();
		}
		
		output.writeUTF(numero);
	}
	
	
	//Método para enviar un nombre al cliente
	private static void sendNombreCliente() throws IOException {
		
		String nombre="";
		
		while(nombre.length()==0) {
			System.out.println("El servidor solicita que introduzcas un nombre de cliente");		
			Scanner scanner = new Scanner(System.in);
			nombre = scanner.nextLine().toLowerCase();
		}
		
		output.writeUTF(nombre);
	}
}
