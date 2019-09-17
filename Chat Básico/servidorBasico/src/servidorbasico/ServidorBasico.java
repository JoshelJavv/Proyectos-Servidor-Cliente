/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorbasico;
import java.net.*;
import java.io.*;

/**
 *
 * @author android-4ff5z998
 */
public class ServidorBasico {

   /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ServerSocket servidorSocket = null;
        Socket socket = null;
        BufferedReader entrada = null;
        
        
        try{
        servidorSocket = new ServerSocket(Integer.parseInt(args[0]));
        System.out.println("Servidor funcionando, esperando al Cliente...");
        socket = servidorSocket.accept();
        }catch(Exception e){
            System.out.println("Escribe un puerto correcto");
            System.exit(0);
        }
        
        try{
        entrada = new BufferedReader(
                  new InputStreamReader(
                      socket.getInputStream()
                   )
        );
        }catch(Exception e){
            System.out.println("Error al recibir mensaje");
            System.exit(0);
        }
        
        String info = "";
        // reactivar lo uso para que el servidor siga funcionando despues de recibir un mensaje
        boolean reactivar = false;
        
        while(true){
            try{
            // si reactivar = a true significa que ya se recibio el primer mensaje o que hubo un error como que el cliente se desconecto
            if(reactivar==true){
              servidorSocket = new ServerSocket(Integer.parseInt(args[0])); //ponemos a funcionar al servidor de nuevo
              socket = servidorSocket.accept(); // aceptamos a un cliente
              entrada = new BufferedReader(     // preparamos para leer lo que el proximo cliente mandará
                  new InputStreamReader(
                      socket.getInputStream()
                   )
            );
            }
            info = entrada.readLine(); // los mensajes que me manda el cliente
            System.out.println("Me dijeron que: " + info);
            servidorSocket.close();
            socket.close();
            reactivar = true; // como el cliente se desconecta, volvemos a levantar el servidor
        }catch(Exception e){
            System.out.println("Error al recibir mensaje cliente se desconecto");
            // si el cliente se desconecta cierro los sockets y reactivar = true para volver a levantar el servidor y continue funcionando
            try{
            servidorSocket.close();
            socket.close();
            }catch(Exception o){
                System.out.println("No se puedo cerrar el socket");
            }
            reactivar = true;
            //System.exit(0);
        }
            }
        
    }
    
}