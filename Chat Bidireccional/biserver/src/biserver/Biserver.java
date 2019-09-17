/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biserver;
import java.net.*;
import java.io.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 *
 * @author android-4ff5z998
 */
public class Biserver {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) {
        // TODO code application logic here
 
        ServerSocket socketServidor = null;
        Socket socket=null;
        Scanner scanner = new Scanner(System.in);
        BufferedReader lector = null;
        PrintWriter escritor = null;
        
                
        try{
        socketServidor = new ServerSocket(Integer.parseInt(args[0]));
        System.out.println("Servidor funcionando, esperando al Cliente...");
        socket = socketServidor.accept();
        }catch(Exception e){
            System.out.println(" No se puedo llegar al puerto, Escriba un puerto correcto.");
            System.exit(0);
        }
        
        try{
        lector = new BufferedReader(
                  new InputStreamReader(
                      socket.getInputStream()
                   )
        );
        }catch(Exception e){
            System.out.println("No fue posible leer el mensaje");
            System.exit(0);
        }
     
        try{
        escritor = new PrintWriter(socket.getOutputStream(),true);
        }catch(Exception e){
            System.out.println("No fue posible enviar el mensaje");
            System.exit(0);
        }
        
        String entrada ="";
        String salida;
        boolean reactivar = false;
        boolean enviando = false;

        do{
            try{
                //si reactivar = a true significa que ya se recibio el primer mensaje o que hubo un error como que el cliente se desconecto
                if(reactivar==true){
                    socketServidor = new ServerSocket(Integer.parseInt(args[0]));
                    System.out.println("Servidor funcionando, esperando al Cliente...");
                    socket = socketServidor.accept();
                    lector = new BufferedReader(
                            new InputStreamReader(
                                socket.getInputStream()
                             )
                  );
                    escritor = new PrintWriter(socket.getOutputStream(),true);
                    reactivar = false;
                }
                entrada=lector.readLine(); // esperamos un mensaje del cliente
                System.out.println("Cliente: " + entrada); // si el cliente manda un mensaje lo imprimimos
                enviando = true; // si no ocurrio ningun error y el cliente no repsondio enviando = true para que el servidor conteste
            }catch(Exception e){
                // si el cliente se desconecta cerramos los sockets
                try{
                    socketServidor.close();
                    socket.close();
                }catch(Exception o){
                    System.out.println("no se pudo cerrar socket");
                }
                reactivar = true; // y reactivar = true para levantar al servidor
                enviando = false; // enviado = false para que cuando ocurra el error, el scanner del servidor no se quede a la espera
                System.out.println("El cliente se desconecto");
                System.out.println("--------------------------------------------------");
                System.out.println("");
                //System.exit(0);
            }
            
              if(entrada.equalsIgnoreCase("fin")){
                  System.out.println("Me Voy");
                  
                  try{
                  socket.close();
                  socketServidor.close();
                  }catch(Exception e){
                      System.out.println("Error al cerrar la conexión");
                      System.exit(0);
                  }
                  System.exit(0);
              }
              // si todo salio bien enviado = true y podemos responderle al cliente
              if(enviando==true){
              System.out.print("Tu: ");
              salida = scanner.nextLine(); // salida lee lo que escribimos en consola
              escritor.println(salida); // y escritor se lo envia al cliente
              }
        }while(!entrada.equalsIgnoreCase("fin"));
        
      
        
    }
    
}