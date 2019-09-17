/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorarchivotexto;
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
public class ServidorArchivoTexto {

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
        
    
        String entrada ="";
        String completado= "success";
        String salida;
        
        try{
        escritor = new PrintWriter(socket.getOutputStream(),true);
        }catch(Exception e){
            System.out.println("No se completo la escritura");
            //System.exit(0);
        }
        
        String cadena;
        String enviar="";
        FileReader f = null;
        boolean reactivar = false;
        boolean enviando = false;
        boolean archivoEncontrado = false;
        
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
                entrada=lector.readLine(); // esperamos a que el cliente nos diga que archio de texto quiere
                System.out.println("");
                enviando = true; // para decir que puede enviar el texto al cliente, en un if mas abajo
            }catch(Exception e){
                System.out.println("El Cliente se desconecto. Servidor Funcionando.");
                System.out.println("--------------------------------------------------");
                //System.out.println("igual a :"+entrada+"-");
                // si el cliente se desconecta cerramos sockets
                try{
                    socketServidor.close();
                    socket.close();
                }catch(Exception o){
                    System.out.println("no se puedo cerrar el socket");
                }
                reactivar = true; // para volver a levantar el servidor
                enviando = false; // y no dejamos que entre al if de enviando por que ocurrio un error
                //System.exit(0);
            }
              if(entrada==null){
                  try{
                      socket.close();
                      socketServidor.close();
                  }catch(Exception e){
                      System.out.println("Error a cerrar el socket");
                  }
                  reactivar = true;
                  enviando = false;
                  //System.exit(0);
              }

              // si todo salio bien se empieza a enviar el texto
              if(enviando==true){
                  System.out.println("Archivo solicitado: " + entrada); // imprimo el archivo que se solicito
                  System.out.println("");
                try{
                    f = new FileReader(entrada); // f es el archivo que solicito el cliente y lo vamos a leer
                    archivoEncontrado = true; // si no entra en el catch el archivo fue encontrado
                }catch(Exception e){
                    System.out.println("Archivo no Encontrado");
                    escritor.println("Archivo no encontradox"); // no fue encontrado y le avisamos al cliente
                    archivoEncontrado = false;
                    //System.exit(0);
                }
                // si fue encontrado comenzamos a enviar el texto
                if(archivoEncontrado==true){
                try{
                    BufferedReader b = new BufferedReader(f); // leemos el archivo solicitado
                while((cadena = b.readLine())!=null) { // lee linea por linea hasta que se acaben
                    enviar = cadena; // igualamos cada linea leida a la variable enviar
                    
                    escritor.println(enviar); // enviamos al cliente lo que se almaceno en enviar
                }
                    escritor.println(completado); // cuando se envia todo, se lo decimos al cliente
                    System.out.println("Transferencia finalizada");
                    System.out.println("--------------------------------------------------");
                    b.close();
                }catch(Exception e){
                    System.out.println("El Archivo no se envió correctamente");
                }
                }
              }
           
        }while(true);
        
      
        
    }
    
}