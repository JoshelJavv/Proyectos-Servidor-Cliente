/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientearchivotexto;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Scanner;

/**
 *
 * @author android-4ff5z998
 */
public class ClienteArchivoTexto {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) {
        // TODO code application logic here
    
        Socket socket = null;
        PrintWriter escritor = null;
        BufferedReader lector = null;
        String usuario = System.getProperty("user.home");
        
        try{
        socket = new Socket(args[0], Integer.parseInt(args[1]));
        System.out.println("Conexión establecida");
        }catch(Exception e){
            System.out.println("Introduzca la Direccion ip y un puerto correctamente");
            System.exit(1);
        }
        
        try{
        escritor = new PrintWriter(
            socket.getOutputStream(), true
        );
        }catch(Exception e){
            System.out.println("Problema al intentar enviar mensaje al servidor");
            System.exit(1);
        }
        
        
        try{
        lector = new BufferedReader(       
                new InputStreamReader(socket.getInputStream())
        );
        }catch(Exception e){
            System.out.println("Problema al recibir mensaje del servidor");
            System.exit(1);
        }
        
        
        
        
        
        String datos;
        String datosEntrada = "";
        Scanner scanner = new Scanner(System.in);

        File file = null;
        
        
        FileWriter fw = null;
        BufferedWriter bw = null;
            
            
            boolean recibiendo = false;
            boolean crear = false;
            boolean creando = false;
            boolean txt = false;
        
        while(true){
            // si no esta recibiendo datos introduzco la url del archivo de texto a descargar
            if(recibiendo==false){
                System.out.println("");
                System.out.println("Ingresa la URL del archivo:");    
                datos=scanner.nextLine(); // esta a la espera de la ruta del archivo que vamos a introducir
                file = new File(datos); // guardamos en la variable file la ruta que escribimos
                if(file.getName().toLowerCase().endsWith(".txt")){ // aqui comprobamos que el archivo termine con .txt
                    escritor.println(datos); // le enviamos la ruta al servidor
                    recibiendo = true; // para permitir recibir el texto
                    creando = false; // para controlar el boolean crear
                    txt = true; // si se cumple la condición, para permitir que se ejecute el codigo que guarda el texto
                }else{
                    System.out.println("Solicite un archivo de texto '.txt' por favor");
                    txt = false; // si no se cumple la condición, no se permite ejecutar el codigo de guardar texto
                }
                
                }
            // si paso la comprobacion de archivo txt se puede ejecutar el codigo para guardar texto
            if(txt==true){
            
            try{
                datosEntrada = lector.readLine(); // esperamos las cademas de texto que el servidor estará enviando
                if(datosEntrada.equalsIgnoreCase("archivo no encontradox")){ // comprobamos si el servidor nos dice que no encontro el archivo
                    System.out.println("Archivo no encontrado");
                    recibiendo = false;
                }else{
                    if(creando==false){
                    System.out.println("encontrado");
                    crear = true; // si se encontro el archivo permitimos que se cree el archivo de texto
                    }
                }
                    
                
            }catch(IOException e){
                System.out.println("Error al recibir mensaje");
                System.exit(1);
            }
            // se permite que se cree y escriba el archivo
            if(crear==true){
                try{
                        fw = new FileWriter(usuario + "\\downloads\\" + file.getName()); // es la ruta donde se guardará el archivo
                    }catch(Exception e){
                        System.out.println("No se puede escribir en el archivo");
                        System.exit(1);
                    }

                    bw = new BufferedWriter(fw); // permite escribir en el archivo
                    crear=false; // para que no entre en esta condicion mientras se escriba el archivo
                    creando=true; //para que no se este creando el archivo cada vez que se reciban cadenas de texto
                }
            
            // si la transferencia del archivo se completo cierro bufferedWriter 
            if(datosEntrada.equals("success")){
            
            try{
                bw.close(); // cierro bufferewriter
            }catch(Exception e){
                System.out.println("El archivo no se puede cerrar");
                System.exit(1);
            }
            
                recibiendo = false;
                System.out.println("");
                System.out.println(datosEntrada);
                System.out.println("Descarga Finalizada");
                System.out.println("Archivo guardado en: ");
                System.out.println(usuario + "\\downloads\\" + file.getName()); // la ruta donde se guardo el archivo
                try{
                socket.close(); // cerramos socket, desconectamos al cliente
                }
                catch(Exception e){
                    System.out.println("No se pudo cerrar el socket");
                }
                System.out.println("--------------------------------------------------");
                System.exit(1); //salimos del programa
            }
            // si esta recibiendo datos escribo en el archivo
            if(recibiendo==true){
                
            try{    
                bw.write(datosEntrada); // se escribe cada cadena de texto recibida del servidor en el archivo
                bw.newLine(); // para que conserve la estrucutra al imprimir saltos de linea
            }catch(Exception e){
                System.out.println("Error al descargar archivo");
                System.exit(1);
            }
            
            }
            }
        }// fin del while
 
       
        
       
    }
    
}