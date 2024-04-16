package Sockets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

public class GestorProcesos extends Thread{
	
	private Socket socket;
	private DataOutputStream output;
	private DataInputStream input;
	static String file = "src/numeros.txt";
	
	
	//Constructor
	public GestorProcesos (Socket socket) throws IOException {
		
		this.socket=socket;
		this.output= new DataOutputStream(socket.getOutputStream());
		this.input = new DataInputStream(socket.getInputStream());
	}

	
	//Método del hilo
	public void run() {
		try {
			makeProceso();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//Método que realizará todo el proceso de comprobación del número de lotería
	public void makeProceso() throws IOException {
		
		System.out.println("HILO SERVIDOR ->"+Thread.currentThread().getId());		
		
		//1. Recibo el nombre del cliente
		String nombreCliente=getNombreCliente();
		
		//Solo si tengo un nombre de cliente que sea válido (mayor que 0)
		if(nombreCliente.length()>0) {
			
			String accion="";
			
			//2. Una vez que tengo el nombre del cliente -> le envío la instrucción correspondiente para que muestre el menú
			sendAccion("MENU");
			
			while (true) {				
								
	            // Esperar hasta que el cliente envíe un dato
	            String dato = input.readUTF();
	            
	            if (dato == null) {
	                System.out.println("Cliente desconectado.");
	                break; // Si el cliente se desconecta, salir del bucle
	            }
	            
	            System.out.println("dato del cliente->"+dato);
	            
	            
	            //Compruebo si el dato que me ha mandado el cliente es una acción
	            if(isAccion(dato)) {
	            	
	            	accion=dato;
	            	
	            	//Dependiendo de la opción que envíe el cliente
		            switch(accion) {
		            
		            	//La opción es "almacenar el número que introduzca el usuario en un archivo común + almacenar el número del usuario en un archivo en el que estén todos los números que introduzca"
		            	case "a":
		            		
		            		//Le pido que me mande un número
		            		sendAccion("NUM");
		            		
		            	break;
		            	
		            	//Cantidad de números almacenados hasta el momento por todos los usuarios
		            	case "b":
		            		
		            		countNumeros();
		            		
			            break;
			            	
			            //Listar los números almacenados
		            	case "c":
		            		
		            		listNumeros();
		            		
			            break;
			            	
			            //Cantidad de números almacenados por un cliente
		            	case "d":
		            		
		            		sendAccion("NAME");
		            		
			            break;
		            }
		            
	            	
	            }
	            else {
	            	
	            	//Almaceno el número que he recibido
	            	if(accion.equals("a")) {
	            		
	            		//A estos métodos solamente puede acceder un usuario mientras se están escribiendo
	            		writeNumero(nombreCliente, dato); 
	            		writeNumeroCliente(nombreCliente, dato);
	            		sendAccion("MENU");
	            			            		
	            	}
	            	
	            	
	            	//Recibo el nombre correspondiente y envío la respuesta al cliente
	            	if(accion.equals("d")) {
	            		
	            		//Devuelvo la cantidad de números de un cliente
	            		countNumerosCliente(dato);
	            	}
	            }
	            
	            
	            System.out.println("Servidor dice: " + dato);
	        }
		}
		
		
		
		
		
	}
	
	
	//Método para recuperar el nombre del cliente
	private String getNombreCliente() throws IOException {
		
		String aux=input.readUTF();		
		
		return aux;
	}
	
	
	//Método para enviar una instrucción al cliente
	private void sendAccion(String accion) throws IOException {		
		
		output.writeUTF(accion);
				
	}
	
	
	//Método para comprobar si el dato que me envía el cliente es una acción
	private Boolean isAccion(String accion) {
		
		Boolean aux = false;
		
		if((accion.equals("a"))||(accion.equals("b"))||((accion.equals("c")))||(accion.equals("d"))) {
			aux=true;
		}
		
		return aux;
	}
	
	
	//Método para almacenar el número que ha mandado el cliente
	private synchronized static void writeNumero(String nombreCliente, String numero) throws IOException {		
				
		FileWriter writer = new FileWriter(file, true);
		writer.write(numero+"-"+nombreCliente+"\n");
		writer.close();
		
	
		
	}
	
	
	//Método para guardar los números del cliente (en un archivo con su nombre)
	private synchronized static void writeNumeroCliente(String nombreCliente, String numero) throws IOException {
		
		String file = "src/"+nombreCliente.toLowerCase()+".txt";
		
		FileWriter writer = new FileWriter(file, true);
		writer.write(numero+"\n");
		writer.close();
		
	}
	
	
	//Método para contar los números introducidos por los usuarios
	private void countNumeros() throws IOException {
		
		int cont=0;
		
		BufferedReader reader = new BufferedReader(new FileReader(file));

        // Leer el archivo línea por línea y contar las líneas
        while (reader.readLine() != null) {
            cont++;
        }

        // Cerrar el lector
        reader.close();

		//Enviar el resultado al cliente
        output.writeUTF("Los clientes han introducido "+cont+" números");
        
        sendAccion("MENU");
	}
	
	
	//Método para listar los números introducidos por los usuarios
	private void listNumeros() throws IOException {
		
		String aux = "";
		String linea;
		
		BufferedReader reader = new BufferedReader(new FileReader(file));

        // Leer el archivo línea por línea y contar las líneas
        while ((linea = reader.readLine()) != null) {
            
        	String[] valores = linea.split("-");
        	
        	aux+=valores[0]+"\n";
        }

        // Cerrar el lector
        reader.close();
        
        //Enviar el resultado al cliente
        output.writeUTF("Los números introducidos por los clientes son: \n"+aux);
        sendAccion("MENU");
	}
	
	
	//Método para devolver la cantidad de números almacenados por un cliente
	private void countNumerosCliente(String nombreCliente) throws IOException {
		
		
		String ruta ="src/"+nombreCliente+".txt";
		File archivo = new File(ruta);
		String mensaje="";
		
		if(archivo.exists()) {
			
			int cont=0;
			
			BufferedReader reader = new BufferedReader(new FileReader(ruta));

	        // Leer el archivo línea por línea y contar las líneas
	        while (reader.readLine() != null) {
	            cont++;
	        }

	        // Cerrar el lector
	        reader.close();

	        //Mensaje
	        mensaje="El usuario "+nombreCliente+" ha introducido "+cont+" números.";
			
		}
		else {
			
			mensaje="El cliente indicado ("+nombreCliente+") no ha introducido ningún número.";
			
		}
		
		//Envío el mensaje
		output.writeUTF(mensaje);
		sendAccion("MENU");
	}

}
