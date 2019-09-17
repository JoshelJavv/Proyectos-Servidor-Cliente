/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientebasico;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author android-4ff5z998
 */
public class ClienteBasico {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Socket socket = null;
        PrintWriter escritor = null;
        
        try{
        socket = new Socket(args[0], Integer.parseInt(args[1])); // ponemos ip y un puerto
        System.out.println("Conexión establecida");
        }catch(Exception e){
            System.out.println("Ingresa una ip correcta y un puerto correcto");
            System.exit(1);
        }
        
        try{
        escritor = new PrintWriter(
            socket.getOutputStream(), true
        ); 
        }catch(Exception e){
            System.out.println("No fue posible enviar el mensaje");
            System.exit(1);
        }
        
        System.out.println("Escribe tu mensaje para el Servidor");
        Scanner scanner = new Scanner(System.in);
        String mensaje = scanner.nextLine(); // lee lo que escribo en consola
        escritor.println(mensaje); // manda al servidor lo que escribo
        
        try{
        socket.close(); // cerramos el socket
        }catch(Exception e){
            System.out.println("Error al cerrar conexion");
            System.exit(1); // despues de enviar nos desconectamos y se cierra el programa del cliente
        }
        
        
       
    }
    
}
