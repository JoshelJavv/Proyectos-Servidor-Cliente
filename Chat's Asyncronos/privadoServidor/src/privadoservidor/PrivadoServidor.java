/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privadoservidor;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

/**
 *
 * @author android-4ff5z998
 */
public class PrivadoServidor {

    private static Set<String> names = new HashSet<>();
    private static Set<PrintWriter> writers = new HashSet<>();
    static HashMap<String, PrintWriter> lista = new HashMap<String, PrintWriter>();
    static Hashtable<String, ArrayList<String>> bloqueados = new Hashtable<String, ArrayList<String>>();
    

    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running... ");
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (ServerSocket listener = new ServerSocket(59001)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));

            }
        }

    }
    
    private String guardarname(String n){
        return n;
    }

    private static class Handler implements Runnable {

        private String name;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;
        private String privado;
        

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("SUBMITNAME");
                    name = in.nextLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            break;

                        }
                    }
                }
                
                lista.put(name, out);
                bloqueados.put(name, new ArrayList());
                
                out.println("NAMEACCEPTED " + name);
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE " + name + " has joined");
                }
                writers.add(out);

                while (true) {
                    String input = in.nextLine();
                    if (input.toLowerCase().startsWith("/quit")) {
                        return;
                    }
                    if(input.toLowerCase().startsWith("/privado")){
                        String [] extraer = input.split(" ");
                        String eso = "";
                        if(extraer.length>=2){
                        for(int i=2; i<extraer.length; i++){
                            eso = eso + extraer[i].toString() + " ";
                        }
                        
                        if(bloqueados.get(extraer[1])!=null){
                            if(!bloqueados.get(extraer[1]).contains(name)){
                            lista.get(extraer[1]).println("PRIVATE " +"Susurro ("+ name +"): "+ eso);
                            lista.get(name).println("PRIVATE " + "Para (" + extraer[1] + "): " + eso);
                            }
                        }
                       }

                    }
                    else{
                    
                    for (String imprimir : names) {
                        if(!bloqueados.get(imprimir).contains(name)){
                        lista.get(imprimir).println("MESSAGE " + name + ": " + input);
                        }
                    }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    writers.remove(out);
                }
                if (name != null) {
                    System.out.println(" is leaving");
                    names.remove(name);
                    for(PrintWriter writer:writers){
                        writer.println("MESSAGE "+name+" has left");
                    }
                }
                try{
                    socket.close();
                } catch(IOException e){
                }
                }
            }

        }

    }
