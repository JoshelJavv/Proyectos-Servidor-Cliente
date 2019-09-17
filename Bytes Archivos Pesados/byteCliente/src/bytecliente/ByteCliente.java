/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bytecliente;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author android-4ff5z998
 */
public class ByteCliente {

     /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) {
        // TODO code application logic here
    
        Socket socket = null;
        PrintWriter escritor = null;
        BufferedReader lector = null;
        String usuario = System.getProperty("user.home"); // obtengo la direccion del disco hasta el usuario ej: C:/users/juan/
        
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
        
        
        
        String datos;
        String datosEntrada = "";
        Scanner scanner = new Scanner(System.in);

            
        boolean recibiendo = false;
        boolean existe = false;
            
         int in;
         byte recibidos [];
         String ruta = "";
         String archivoSaved = "";
         File file = new File(ruta);
        
        while(true){
            // si no esta recibiendo datos introduzco la url del archivo de texto a descargar
            if(recibiendo==false){
                System.out.println("");
                System.out.println("Ingresa la URL del archivo:");
                datos=scanner.nextLine();
                ruta = datos;
                file = new File(ruta);
                System.out.println("");
                escritor.println(datos);
   
                recibiendo = true;
            }
           
            // si la transferencia del archivo se completo cierro bufferedWriter y vuelvo a habilitar la opcion de introducir url
            if(datosEntrada.equals("success")){
            
            try{
                escritor.close();
               socket.close();
                
            }catch(Exception e){
                System.out.println("El archivo no se puede cerrar");
                System.exit(1);
            }
            
                recibiendo = false;
                System.out.println("");
                System.out.println(datosEntrada);
                System.out.println("Descarga Finalizada");
                System.out.println("Archivo guardado en: ");
                System.out.println(usuario + "\\downloads\\" + archivoSaved);
                System.out.println("--------------------------------------------------");
                System.exit(1);
            }
            // si esta recibiendo datos escribo en el archivo
            if(recibiendo==true){
                
            try{
                
                recibidos = new byte[1024];  // aqui se almacenan los bytes que se reciben del servidor
                BufferedInputStream lei = new BufferedInputStream(socket.getInputStream());
                DataInputStream dd = new DataInputStream(socket.getInputStream());
                String exist = dd.readUTF();
                if(exist.equalsIgnoreCase("Archivo no Existe")){
                    existe = false;
                    System.out.println("El archivo no existe");
                    System.out.println("--------------------------------------------------");
                    dd.close();
                    System.exit(1);
                    
                } else if(exist.equalsIgnoreCase("si existe")){
                    existe = true;
                }
                
                if(existe==true){
                //System.out.println("Tamaño del archivo: " + dd.readLong());
                long divi = dd.readLong(); // almaceno en divi el tamaño del archivo para despues usarlo para sacar el porcentaje
                archivoSaved = dd.readUTF(); // aqui guardo el nombre del archivo
                long acumulado = 0;
                double fixit;
                int progreso = 0;
                int suma = 2;
                String igual = "=";
                //spacio para la barra de progreso le doy un tamaño de 50 espacios
                String spacio = "                                                  ";
                //System.out.println(usuario + "\\downloads\\" + archivoSaved);
                BufferedOutputStream escr = new BufferedOutputStream( new FileOutputStream(usuario + "\\downloads\\" + archivoSaved));
                System.out.println("Descargando: ");
                while((in = lei.read(recibidos)) != -1){
                    //System.out.println(nudes);
                    acumulado = acumulado + in; // en acumulado se acumulan los bytes recibidos
                    //System.out.println("Acu: " + acumulado + " " + "in: " + in);
                    escr.write(recibidos, 0, in); // se escriben los bytes en el archivo
                    fixit = acumulado; // en fixit acumula el acumulado, pero fixit es double nos ayuda a sacar el porcentaje
                    progreso = Integer.parseInt((String.format("%.0f",(fixit/divi)*100))); // se calcula el porcentaje sin decimales
                    //System.out.println("here: "+ here);
                   // utilizamos las variables de porcentaje y otra que se llama suma para hacer la barra de progreso
                    if(progreso>=suma){
                        // cada vez que aplica la condicion, elimino un espacio para que mantenga el tamaño al sobreescribir
                        spacio = spacio.replaceFirst(" ","");
                        System.out.print("\r"+"[" + igual + spacio + "]" + (String.format("%.0f",(fixit/divi)*100)) + "%");
                        igual = igual + "="; // se van añadiendo = a la barra de progreso
                        suma = suma + 2; // suma aumenta en 2 para que cada ves que se iguale un porcentaje de 2 en 2 aumente la barra
                    } else if(progreso<2){
                        // cuando el porcentaje es menor al 2% se imprime tambien la barra
                        System.out.print("\r"+"["+ spacio + "]" + (String.format("%.0f",(fixit/divi)*100)) + "%");
                    }
                    
                    if(in<1024){ // ultimos bytes recibidos
                        System.out.print("\r"+"["+ "==================================================" + "]" + (String.format("%.0f",(fixit/divi)*100)) + "%");
                    }

                
                }
                System.out.println("");
                lei.close();
                escr.close(); // ya que se recibe bien el archivo cerramos buffered lo cual tambien cerrara el socket
                

                datosEntrada = "success";

                }
            }catch(Exception e){
                System.out.println("Error al descargar archivo");
                System.exit(1);
            }
            
            }
            
        }
 
       
        
       
    }
    
}