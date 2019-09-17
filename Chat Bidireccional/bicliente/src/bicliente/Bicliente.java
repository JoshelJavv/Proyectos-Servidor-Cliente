/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bicliente;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author android-4ff5z998
 */
public class Bicliente {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Socket socket = null;
        PrintWriter escritor = null;
        BufferedReader lector = null;
        
        try{
        socket = new Socket(args[0],Integer.parseInt(args[1]));
        System.out.println("Conexión establecida");
        }catch(Exception e){
            System.out.println("Ingrese una direccion ip correcta y un puerto correcto");
            System.exit(1);
        }
        
        try{
        escritor = new PrintWriter(
            socket.getOutputStream(), true
        );
        }catch(Exception e){
            System.out.println("Problema al recibir mensaje");
            System.exit(1);
        }
        
        try{
        lector = new BufferedReader(       
                new InputStreamReader(socket.getInputStream())
        );
        }catch(Exception e){
            System.out.println("Problema al enviar mensaje");
            System.exit(1);
        }
        
        
        
        
        
        String datos;
        String datosEntrada = "";
        Scanner scanner = new Scanner(System.in);
        
        while(true){
            System.out.print("Tu: "); // solo para ver mas claro cuando es el turno del cliente
            datos=scanner.nextLine(); // el scanner lee lo que escribio el cliente
            escritor.println(datos); // y se lo manda al servidor
            
            try{
            datosEntrada = lector.readLine(); // leemos lo que el servidor nos contesta
            }catch(Exception e){
                System.out.println("Servidor se desconecto");
                System.exit(1);
            }
            System.out.println("Servidor: " + datosEntrada); // y imprimimos lo que el servidor mando

        }
 
       
        
       
    }
    
}