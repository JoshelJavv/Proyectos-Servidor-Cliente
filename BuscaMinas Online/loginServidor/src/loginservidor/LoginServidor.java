/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginservidor;
import java.beans.Statement;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
import java.sql.*;

/**
 *
 * @author android-4ff5z998
 */
public class LoginServidor {

    private static Set<String> names = new HashSet<>();
    private static Set<PrintWriter> writers = new HashSet<>();
    static HashMap<PrintWriter, String> color = new HashMap<PrintWriter, String>();
    private static Set<PrintWriter> lose = new HashSet<PrintWriter>();
    static HashMap<String, String> estado = new HashMap<String, String>();
    static HashMap<String, String> banderas = new HashMap<String, String>();
    static HashMap<String, String> gamers = new HashMap<String, String>();
    
    
    static Connection conn = null;
    static String [][] mandar;
    static int s = 1;
    static int minas = 0;
    static int max = 0;
    
    
    public static void main(String[] args) throws Exception {
        ExecutorService poole = Executors.newFixedThreadPool(4);
        System.out.println("The chat server is running... ");
        ExecutorService pool = Executors.newFixedThreadPool(4);
        mandar = empezar();
        try (ServerSocket listener = new ServerSocket(59001)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));

            }
        }
        
    }
    
    public static void sala(int x) throws Exception{
        ExecutorService pools = Executors.newFixedThreadPool(4);
        try(ServerSocket listener = new ServerSocket(59001)){
            while(true){
                pools.execute(new Handler(listener.accept()));
            }
        }
        
    }
    
    private String guardarname(String n){
        return n;
    }
   
    private static class Handler implements Runnable {

        private String name;
        private String pass;
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

                        synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            break;

                        }
                      
                    }
                }
                
                
                //bloqueados.put(name, new ArrayList());
               
                out.println("NAMEACCEPTED " + name);
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE " + name + " has joined");
                }
                writers.add(out);
                if(names.size()==1){
                    color.put(out,"azul");
                    out.println("tucolor azul");
                }
                if(names.size()==2){
                    color.put(out,"rojo");
                    out.println("tucolor rojo");
                }
                if(names.size()==3){
                    color.put(out,"verde");
                    out.println("tucolor verde");
                }
                if(names.size()==4){
                    color.put(out,"amarillo");
                    out.println("tucolor amarillo");
                }
                
                 if(names.size()>=2 && names.size()<5){
                     
                     
                 
                 int sum = 0;
                String go ="";
                String sumalo = "";
                for(int i=0; i<mandar.length; i++){
                    for(int u=0; u<mandar.length; u++){
                        //sumalo = ""+i +u;
                        String xy = i+"-"+u;
                        if(estado.get(xy)!=null){
                         if(estado.get(xy).contains("false")){
                        System.out.println("repetido: " + xy);
                         }
                        
                        }
                        if(i!=0 && i!=mandar.length-1 && u!=0 && u!=mandar.length-1){
                        estado.put(xy, "false");
                        }
                        sum = sum +1;
                        if(u==mandar.length-1){
                            
                        go = go + mandar[i][u];
                        }else{
                            
                            go = go + mandar[i][u] +"-";
                        }
                        
                        
                    }
                    if(i==mandar.length-1){
                    go= go;
                    }else{
                        go= go + "_";
                    }
                }    System.out.println("mandar..: " + mandar.length);
                     System.out.println("sum: " + sum);
                     System.out.println("tamaño estado: "+ estado.size());
                     //System.out.println("xxx" + estado.keySet());
                
                //System.out.println(go);
                for(PrintWriter writer : writers){
                    writer.println("game" + go);
                }
                
                }

                while (true) {
                    
                    String input = in.nextLine();
                    if (input.toLowerCase().startsWith("/quit")) {
                        return;
                    }
                    if(input.toLowerCase().startsWith("/privado")){
                        String [] extraer = input.split(" ");
                        String eso = "";
                        
                        String xy = extraer[1]+"-"+extraer[2];
                        int mx = Integer.parseInt(extraer[1]);
                        int my = Integer.parseInt(extraer[2]);
                        //System.out.println("estado:: "+ xy);
                        if(!lose.contains(out)){
                        if(estado.get(xy).contains("false")){
                            System.out.println("Cordenadas: " + extraer[1] + " , " + extraer[2]);
                            //estado.put(xy, "true");
                            actualizar(mx,my,mandar);
                            for(PrintWriter writer : writers){
                                writer.println("open" + extraer[1]+" "+ extraer[2]+ " " + estado.get(xy));
                            }
                            max = total()-minas;
                            // si aplastó una mina pierde
                            if(mandar[mx][my].equalsIgnoreCase("m")){
                                lose.add(out);
                                minas = minas + 1;
                                
                                System.out.println("max: " + max);
                                System.out.println("minero: " + lose.toString());
                                out.println("perdiste");
                            }
                            //comprobar si se acabó
                            
                            
                            if(max==890 || lose.size()==2){
                                for(PrintWriter writer : writers){
                                    writer.println("over");
                                }
                                int [] puntajes = puntaje();
                                int puntosAzules = puntajes[0]*100;
                                int puntosRojos = puntajes[1]*100;
                                int puntosVerdes = puntajes[2]*100;
                                int puntosAmarillos = puntajes[3]*100;
                                for(PrintWriter writer : writers){
                                    writer.println("puntajes" + puntosAzules + " " + puntosRojos + " " + puntosVerdes + " " + puntosAmarillos);
                                }
                                estado.clear();
                                banderas.clear();
                                gamers.clear();
                                lose.clear();
                                color.clear();
                                writers.clear();
                                names.clear();
                                max=0;
                                minas=0;
                            }
                        }
                        
                        }
                        
                       
                    }else if(input.toLowerCase().startsWith("bandera")){
                        String [] extraer = input.split(" ");
                        String eso = "";
                        String xy = extraer[1]+"-"+extraer[2];
                        int mx = Integer.parseInt(extraer[1]);
                        int my = Integer.parseInt(extraer[2]);
                        
                        String colour = color.get(out).toString();
                        if(!lose.contains(out)){
                            
                            //para poner
                            if(estado.get(xy).contains("false")){
                                estado.put(xy, "true");
                                
                                if(esMina(mx,my,mandar)){
                                minas = minas +1;
                                }
                                banderas.put(xy, colour);
                                for(PrintWriter writer : writers){
                                    writer.println("banderas " + extraer[1]+ " "+ extraer[2] + " " + colour);
                                }
                            }
                            //
                        }
                        
                    }else if(input.toLowerCase().startsWith("quitar")){
                        String [] extraer = input.split(" ");
                        String eso = "";
                        String xy = extraer[1]+"-"+extraer[2];
                        int mx = Integer.parseInt(extraer[1]);
                        int my = Integer.parseInt(extraer[2]);
                        
                        String colour = banderas.get(xy).toString();
                        if(!lose.contains(out)){
                            //para quitar
                            if(estado.get(xy).contains("true") && color.get(out).contains(colour)){
                                estado.put(xy, "false");
                                banderas.remove(xy);
                                for(PrintWriter writer : writers){
                                    writer.println("quitar " + extraer[1] + " " + extraer[2]);
                                }
                            }
                            //
                        }
                    }
                    else{
                    
                      
                    for (String imprimir : names) {
                        
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
    
     static boolean esMina(int x, int y, String [][] b){
        boolean si = false;
        String a[][] = b;
        
        
        if(a[x][y].equalsIgnoreCase("m")){
            si = true;
            System.out.println("es mina?: " + si);
        }
        
        return si;
     }
    
    static int[] puntaje(){
        int azul = 0;
        int rojo = 0;
        int verde = 0;
        int amarillo = 0;
        int [] colores = new int[4];
        for(String colors : banderas.values()){
            if(colors.equalsIgnoreCase("azul")){
                azul++;
            }else if(colors.equalsIgnoreCase("rojo")){
                rojo++;
            }else if(colors.equalsIgnoreCase("verde")){
                verde++;
            }else if(colors.equalsIgnoreCase("amarillo")){
                amarillo++;
            }
        }
        colores[0] = azul;
        System.out.println("Azules: " + azul);
        colores[1] = rojo;
        System.out.println("Rojos: " + rojo);
        colores[2] = verde;
        colores[3] = amarillo;
        return colores;
    }
    
    static String [][] empezar(){
        buscaminas bmw = new buscaminas();
        String [][]ct = bmw.contar();
        for(int i=0; i < ct.length; i++){
            for(int u=0; u < ct.length; u++){
                //System.out.print(ct[i][u] + " ");
            }
            //System.out.println("");
        }
        return ct;
    }

    static void actualizar(int x, int y, String [][] p){
        String [][] actu = p;
        String fusion = x+"-"+y;
       
        if(actu[x][y]!=null && !actu[x][y].equalsIgnoreCase("s")){
            if(estado.get(fusion).contains("false")){
                estado.put(fusion, "true");

                if(actu[x][y].equalsIgnoreCase(" ")){
                    actualizar(x,y-1,actu);
                    actualizar(x,y+1,actu);
                    actualizar(x+1,y,actu);
                    actualizar(x-1,y,actu);
                }else{
                    return;
                }
            }
        }

    }
    
    static int total(){
        int x =0;
        for(HashMap.Entry<String, String> entry : estado.entrySet()){
            String cor = entry.getKey();
            String val = entry.getValue();
            if(val.equals("true")){
                System.out.println("c:--" + cor + "-" + val);
                x = x +1;
            }
        }
        System.out.println("total: " + x);
        System.out.println("---------------");
        return x;
    }
    
}