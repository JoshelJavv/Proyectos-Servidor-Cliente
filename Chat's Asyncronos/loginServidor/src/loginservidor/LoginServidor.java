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
    static HashMap<String, PrintWriter> lista = new HashMap<String, PrintWriter>();
    static Hashtable<String, ArrayList<String>> bloqueados = new Hashtable<String, ArrayList<String>>();
    static Connection conn = null;

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
                crearDB();
                extraerBloqueados();
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.nextLine();
                    if (name == null) {
                        return;
                    }
                    out.println("SUBMITPASS");
                    pass = in.nextLine();
                    if(pass == null){
                        return;
                    }
                    if(usuario(name)){
                        if(validar(name,pass)){
                            synchronized (names) {
                            if (!names.contains(name)) {
                                names.add(name);
                                break;

                            }
                          }
                        }else{
                            out.println("INCORRECT");
                        }
                        
                    }else{
                        db(name,pass);
                        System.out.println("agregado");
                        bloqueados.put(name, new ArrayList());
                        synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            break;

                        }
                      }
                    }
                }
                
                lista.put(name, out);
                //bloqueados.put(name, new ArrayList());
                
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
                        //System.out.println("long: " + extraer.length);
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
                    else if(input.toLowerCase().startsWith("/bloquear")){
                        String [] extraerBloqueados = input.split(" ");
                        if(extraerBloqueados.length>=2){
                        bloqueados.get(name).add(extraerBloqueados[1]);
                        //for(String clave : bloqueados.keySet()){
                        //    System.out.println(clave);
                        //}
                        bloquear(name, extraerBloqueados[1]);
                        //System.out.println("bloqueados: " + bloqueados.get(name));
                        }
                    }
                    else if(input.toLowerCase().startsWith("/desbloquear")){
                        String [] desbloquear = input.split(" ");
                        if(desbloquear.length>=2){
                        desbloquear(name,desbloquear[1]);
                        }
                    }else{
                    
                      
                    for (String imprimir : names) {
                        //System.out.println("llego");
                        if(!bloqueados.get(imprimir).contains(name)){
                            //System.out.println("llego");
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
    
    static void crearDB(){
         try{
           
            //System.out.println("home: "+System.getProperty("user.home"));
            String rutaC = System.getProperty("user.home") + "\\documents\\chat_asyncrono";
            File carpeta = new File(rutaC);
            if(!carpeta.exists()){
                carpeta.mkdir();
            }
            String datab = rutaC + "\\users.db";
            File based = new File(datab);
            Class.forName("org.sqlite.JDBC");
            if(!based.exists()){
                based.createNewFile();
                conn = DriverManager.getConnection("jdbc:sqlite:" + datab);
                String tabla = "create table usuarios(nombre varchar(30) UNIQUE, pass varchar(30), bloqueados varchar)";
                PreparedStatement t = conn.prepareStatement(tabla);
                t.execute();
                t.close();

            }
            
        }catch(Exception e){
            System.out.println("error db: " + e);
        }
    }
    
    static void db(String n, String p) {
        try{
            String rutaC = System.getProperty("user.home") + "\\documents\\chat_asyncrono";

            String datab = rutaC + "\\users.db";

            conn = DriverManager.getConnection("jdbc:sqlite:" + datab);
            
            String sql = "insert into usuarios values(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, n);
            ps.setString(2, p);
            ps.setString(3, "");
            ps.executeUpdate();
            ps.close();
        }catch(Exception e){
            System.out.println("error db: " + e);
        }
    }
    
    static private boolean usuario(String name){
         boolean ok = false;
        try{
            Class.forName("org.sqlite.JDBC");
            String ruta = System.getProperty("user.home") + "\\documents\\chat_asyncrono\\users.db";
            conn = DriverManager.getConnection("jdbc:sqlite:" + ruta);
            String sql = "select * from usuarios where nombre = ?";
            PreparedStatement b = conn.prepareStatement(sql);
            b.setString(1, name);
            ResultSet s = b.executeQuery();
            //b.execute();
            //System.out.println(s.next());
            ok = s.next();
            b.close();
            s.close();
        }catch(Exception e){
            System.out.println(e);
        }finally{
            return ok;
        }
    }
    
    static private boolean validar(String name, String pass){
        boolean ok = false;
        try{
            Class.forName("org.sqlite.JDBC");
            String ruta = System.getProperty("user.home") + "\\documents\\chat_asyncrono\\users.db";
            conn = DriverManager.getConnection("jdbc:sqlite:" + ruta);
            String sql = "select * from usuarios where nombre = ? AND pass = ?";
            PreparedStatement b = conn.prepareStatement(sql);
            b.setString(1, name);
            b.setString(2, pass);
            ResultSet s = b.executeQuery();
            //b.execute();
            //System.out.println(s.next());
            ok = s.next();
            b.close();
            s.close();
        }catch(Exception e){
            System.out.println(e);
        }finally{
            return ok;
        }
    }
    static String ubloqueados = "";
    static void bloquear(String usuario, String bloqueado){
        try{
            Class.forName("org.sqlite.JDBC");
            String ruta = System.getProperty("user.home") + "\\documents\\chat_asyncrono\\users.db";
            conn = DriverManager.getConnection("jdbc:sqlite:" + ruta);
            String sql = "select * from usuarios where nombre = ?";
            PreparedStatement b = conn.prepareStatement(sql);
            b.setString(1, usuario);
            ResultSet s = b.executeQuery();
            //b.execute();
            //System.out.println(s.next());
            //System.out.println("s : " + s.next());
            ubloqueados = s.getString("bloqueados");
            
            b.close();
            s.close();
            
            String sqli = "update usuarios set bloqueados = ? where nombre = ?";
            PreparedStatement i = conn.prepareStatement(sqli);
            i.setString(1, ubloqueados + bloqueado + ",");
            i.setString(2, usuario);
            i.executeUpdate();
            i.close();
            
        }catch(Exception e){
            System.out.println("aqui: " + e);
        }
    }
    
    static void desbloquear(String usuario, String bloqueado){
        try{
            Class.forName("org.sqlite.JDBC");
            String ruta = System.getProperty("user.home") + "\\documents\\chat_asyncrono\\users.db";
            conn = DriverManager.getConnection("jdbc:sqlite:" + ruta);
            
            bloqueados.get(usuario).remove(bloqueado);
            
            String almacen = "";
            for(int i=0; i<bloqueados.get(usuario).size(); i++){
                almacen = almacen + bloqueados.get(usuario).get(i)+",";
            }
            //System.out.println(almacen);
            String sqli = "update usuarios set bloqueados = ? where nombre = ?";
            PreparedStatement i = conn.prepareStatement(sqli);
            i.setString(1, almacen);
            i.setString(2, usuario);
            i.executeUpdate();
            i.close();
            
        }catch(Exception e){
            System.out.println("aqui: " + e);
        }
    }
    
    static void extraerBloqueados(){
        try{
            Class.forName("org.sqlite.JDBC");
            String ruta = System.getProperty("user.home") + "\\documents\\chat_asyncrono\\users.db";
            conn = DriverManager.getConnection("jdbc:sqlite:" + ruta);
            String sql = "select * from usuarios";
            PreparedStatement b = conn.prepareStatement(sql);
            ResultSet s = b.executeQuery();
            
            while(s.next()){
                bloqueados.put(s.getString("nombre"), new ArrayList());
                String [] divide = s.getString("bloqueados").split(",");
                for(int i=0; i<divide.length ; i++){
                    bloqueados.get(s.getString("nombre")).add(divide[i]);
                }
                //System.out.println(bloqueados.get(s.getString("nombre")));
            }
            s.close();
        }catch(Exception e){
            
        }
    }

    }
