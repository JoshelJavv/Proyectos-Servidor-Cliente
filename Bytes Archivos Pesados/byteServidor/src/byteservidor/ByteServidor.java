/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package byteservidor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author android-4ff5z998
 */
public class ByteServidor {

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
            System.out.println("Escriba un puerto correcto.");
            System.exit(0);
        }
        
        try{
        lector = new BufferedReader(
                  new InputStreamReader(
                      socket.getInputStream()
                   )
        );
        }catch(Exception e){
            System.out.println("No se pudo completar la lectura");
            System.exit(0);
        }

        
        try{
        escritor = new PrintWriter(socket.getOutputStream(),true);
        }catch(Exception e){
            System.out.println("No se completo la escritura");
            System.exit(0);
        }
        

 
        BufferedInputStream archi = null;
        BufferedOutputStream enviaBytes = null;
        DataOutputStream dd1 = null;
        
        String entrada ="";
        String completado= "success";
        boolean reactivar = false;
        boolean enviando = false;
        boolean existe = false;
        byte send [];
        int in;
        
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
               }
                entrada=lector.readLine(); //obtengo la url del archivo a descargar
                enviando = true;
                System.out.println("");
                System.out.println("Archivo solicitado: " + entrada);
                System.out.println("");
             }catch(Exception e){
                try{
                socketServidor.close();
                socket.close(); // si el cliente se desconecta cerramos sockets
                }catch(Exception o){
                    System.out.println("El servidor no se pudo cerrar");
                }
                System.out.println("El Cliente se desconecto. Conexión terminada.");
                System.out.println("--------------------------------------------------");
                reactivar = true; // y preparamos reactivar = true para levantar el server de nuevo
                enviando = false; // enviando = false para evitar que ocurrar un error al archivo que recibe la ruta, asi no recibe null

                //System.exit(0);
            }
              //System.out.println(entrada);
              if(entrada==null){
                  System.out.println("Descarga finalizada");
                  
                  try{
                      socket.close();
                      socketServidor.close();
                  }catch(Exception e){
                      System.out.println("Error a cerrar el socket");
                  }
                  
                  //System.exit(0);
              }
              
                if(enviando == true){
                try{

                    File nom = new File(entrada); // archivo nom es igual a la ruta ingresada
                    dd1 = new DataOutputStream(socket.getOutputStream());
                    if(nom.exists()){
                        System.out.println("Archivo Encontrado");
                        System.out.println("Enviando...");
                        existe = true;
                        dd1.writeUTF("si existe");
                    }else{
                        System.out.println("Archivo no existe");
                        existe = false;
                        dd1.writeUTF("Archivo no Existe");
                        System.out.println("--------------------------------------------------");
                        dd1.close();
                        socketServidor.close();
                        socket.close(); // cerramos los sockets
                        reactivar=true; // reactivar = true para volver a levantar el servidor y que otro cliente pueda descargar
                    }
                    
                    if(existe == true){
                    send = new byte[1024];
                    archi = new BufferedInputStream(new FileInputStream(entrada)); // con archi se leera el archivo por bytes
                    enviaBytes = new BufferedOutputStream(socket.getOutputStream()); // mandaremos los bytes al cliente
                    
                    dd1.writeLong(nom.length()); // le mando al cliente el peso del archivo
                    dd1.writeUTF(nom.getName()); // le mando al cliente el nombre del archivo
                    while((in = archi.read(send)) != -1){
                        enviaBytes.write(send, 0, in); // enviamos el archivo por bytes al cliente hasta que se complete
                    }
                    archi.close();
                    enviaBytes.close();
                    socketServidor.close();
                    socket.close(); // cerramos los sockets
                    reactivar=true; // reactivar = true para volver a levantar el servidor y que otro cliente pueda descargar
                    escritor.println(completado);
                    System.out.println("Transferencia finalizada");
                    System.out.println("--------------------------------------------------");
                    }
                }catch(Exception e){
                    System.out.println("El Archivo no se envió correctamente");
                }
                }
                    

                
             
           
        }while(true);
        
      
        
    }
    
}