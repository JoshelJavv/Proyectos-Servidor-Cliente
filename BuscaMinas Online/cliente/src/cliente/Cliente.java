/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;



import java.awt.BorderLayout;
import static java.awt.BorderLayout.SOUTH;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author android-4ff5z998
 */
public class Cliente {

    String serverAddress;
    Scanner in;
    PrintWriter out;
    //JFrame frame = new JFrame("Chatter");
    //JTextField textField = new JTextField(50);
    //JTextArea messageArea = new JTextArea(16, 5);
    Map<Integer, String> estado = new HashMap<Integer, String>();
    
   int size = 30;
    String miColor = "";
    String color ="";
    int x = 0;
    int y = 0;
    JButton [][] btns = new JButton[32][32];
    String [][]busca2 = new String[32][32];
    boolean ig = false;

    public Cliente(String serverAddress) {
        this.serverAddress = serverAddress;
        //textField.setEditable(false);
        //messageArea.setEditable(false);
        //frame.getContentPane().add(textField, BorderLayout.SOUTH);
        //frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        //frame.pack();
        /*
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });*/
    }

    private String getName() {
        return JOptionPane.showInputDialog(
                new JFrame("name"), "choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);

    }

    ;
    
    private void setAd(){
         JOptionPane.showMessageDialog(new JFrame(":P"), "Perdiste te toca Espectar");
    }
    ;
    
    private void over(){
         JOptionPane.showMessageDialog(new JFrame(":P"), "Se acabó!!");
    }
    ;
    
    private void puntos(String ganadores,String a, String r, String v, String am){
         JOptionPane.showMessageDialog(new JFrame(":P"),"Ganadores: " + ganadores + "\n"+ "puntos Azul: " + a + "\n" + "puntos Rojo: " + r + "\n" +"puntos Verdes: " + v +"\n"+ "puntos Amarillos: " + am);
    }
    ;
  
    
    
    String name = "";
    String pass = "";
     private void run() throws IOException {
        try {
            Socket socket = new Socket(serverAddress, 59001);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.startsWith("SUBMITNAME")) {
                    this.name = getName();
                    while(name.contains(" ") || name.equalsIgnoreCase("null") || name.equals("")){
                        name = getName();
                    }
                    out.println(name);
                    //lista.put(getName(), 1);
                }
                else if (line.startsWith("NAMEACCEPTED")) {
                    
                    //this.frame.setTitle("Chatter - " + line.substring(13));
                    //textField.setEditable(true);
                       
                    
                }else if(line.startsWith("game")){
                    String jaja = line.substring(4);
                    String [] ax = jaja.split("_");
                    
                    for(int i=0; i < busca2.length; i++){
                        String [] gog = ax[i].split("-");
                        //System.out.println(gog[i]);
                        for(int u=0; u < busca2.length; u++){
                            busca2[i][u] = gog[u];
                            System.out.print(busca2[i][u]+" ");
                        }
                        System.out.println("");
                    }
                    if(ig==false){
                    interfaz(busca2,out);
                    ig=true;
                    }
                }else if(line.startsWith("banderas")){
                    //String he = line.substring(size);
                    String [] band = line.split(" ");
                    int px = Integer.parseInt(band[1]);
                    int py = Integer.parseInt(band[2]);
                    this.color = band[3];
                    ponerBandera(px,py,btns);
                }else if(line.startsWith("quitar")){
                    String [] q = line.split(" ");
                    int qx = Integer.parseInt(q[1]);
                    int qy = Integer.parseInt(q[2]);
                    quitarBandera(qx,qy,btns);
                }else if(line.startsWith("perdiste")){
                    setAd();
                }else if(line.startsWith("over")){
                    over();
                    overOpenAll(btns);
                }else if(line.startsWith("tucolor")){
                    String colores = line.substring(8);
                    this.miColor = colores;
                    System.out.println("colores: " + this.miColor);
                }else if(line.startsWith("puntajes")){
                    String [] p = line.substring(8).split(" ");
                    String ganadores = "";
                    int numMayor = Integer.parseInt(p[0]);
                    int posi = 0;
                    for(int x = 1; x < p.length ; x++){
                        if(Integer.parseInt(p[x])>numMayor){
                            numMayor = Integer.parseInt(p[x]);
                            posi = x;
                        }
                    }
                    
                    if(numMayor==Integer.parseInt(p[0])){
                            ganadores = ganadores + "azul, ";
                    }
                    if(numMayor==Integer.parseInt(p[1])){
                            ganadores = ganadores + "rojo, ";
                    }
                    if(numMayor==Integer.parseInt(p[2])){
                            ganadores = ganadores + "verde, ";
                    }
                    if(numMayor==Integer.parseInt(p[3])){
                            ganadores = ganadores + "amarillo, ";
                    }
                    
                    puntos(ganadores,p[0],p[1], p[2], p[3]);
                }else if (line.startsWith("MESSAGE")) {
                    //messageArea.setForeground(Color.black);
                    //messageArea.append(line.substring(8) + "\n");
                }else if(line.startsWith("open")){
                    //messageArea.setForeground(Color.yellow);
                    //messageArea.append(line.substring(8) + "\n");
                    String xd = line.substring(4);
                    String []d = xd.split(" ");
                    System.out.println("xx: " + d[0] + " yy: " + d[1]);
                    abrir(Integer.parseInt(d[0]),Integer.parseInt(d[1]),busca2,btns);
                }
                
            }
        } finally {
            //frame.setVisible(false);
            //frame.dispose();
        }

    }
    
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server ip as the solve ");
            return;
        }
        Cliente client = new Cliente(args[0]);
        //client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //client.frame.setVisible(true);
        client.run();

    }

    private void setXY(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    private void interfaz(String [][] t, PrintWriter tx){
        JFrame v = new JFrame(this.name+"- Color: " + this.miColor);
        JPanel panel = new JPanel(new GridLayout(30,30));
        v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        v.setSize(1280, 700);
        //v.setResizable(false);
        panel.setPreferredSize(new Dimension(650,650));
        v.add(panel);
        String [][] busca = t;
        btns = new JButton[32][32];
        System.out.println("busca: " + busca.length);
        for(int i=1; i < busca.length-1; i++){
            for(int u=1; u < busca.length-1; u++){
                System.out.print(busca[i][u] + " ");
                //JButton btn = new JButton("");
                String name = busca[i][u];
                final int x = i;
                final int y = u;
                btns[x][y] = new JButton();
                btns[x][y].setBackground(Color.LIGHT_GRAY);
                btns[x][y].setName(name);
                btns[x][y].addMouseListener(new MouseListener(){
                    @Override
                    public void mouseClicked(MouseEvent me) {
                        if(me.getButton() == 1){
                           tx.println("/privado "+x+" "+y);
                           System.out.println("Coordenada: " + x +" : " + y);
                        }
                        if(me.getButton() == 3){
                            
                            if(btns[x][y].getBackground()!=Color.LIGHT_GRAY){
                                tx.println("quitar " + x + " " + y);
                            }else{
                                tx.println("bandera " +x+" "+y);
                            }
                        }
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void mousePressed(MouseEvent me) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void mouseReleased(MouseEvent me) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void mouseEntered(MouseEvent me) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void mouseExited(MouseEvent me) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                
                });
                /*btns[x][y].addActionListener(new ActionListener(){
                   public void actionPerformed(ActionEvent arg0){

                       //btns[x][y].setBackground(Color.WHITE);
                       //btns[x][y].setText(btns[x][y].getName());
                       
                       //setXY(x,y);
                       tx.println("/privado "+x+" "+y);
                       if(btns[x][y].getName().equals(" ")){
                          //abrir(x,y,busca, btns);
                       }
                       
                              System.out.println("Coordenada: " + x +" : " + y);
                              
                   }
                });*/
                //btns[i][u] = btn;
                panel.add(btns[x][y]);
            }
            System.out.println("");
        }
        v.setVisible(true);
        System.out.println("completa");
        for(int i=0; i < size+2 ; i++){
            for(int u=0; u < size+2; u++){
                System.out.print(busca[i][u] + " ");
            }
            System.out.println("");
        }
    }
    
    public void ponerBandera(int x, int y, JButton[][] b){
        JButton [][] a = b;
        if(color.equalsIgnoreCase("azul")){
            a[x][y].setBackground(Color.BLUE);
        }
        if(color.equalsIgnoreCase("rojo")){
          a[x][y].setBackground(Color.red);  
        }
        if(color.equalsIgnoreCase("verde")){
          a[x][y].setBackground(Color.GREEN);  
        }
        if(color.equalsIgnoreCase("amarillo")){
          a[x][y].setBackground(Color.YELLOW);  
        }
        
    }
    
    public void quitarBandera(int x, int y, JButton [][] b){
        JButton [][] a = b;
        
        a[x][y].setBackground(Color.LIGHT_GRAY);
    }
    
    public void overOpenAll(JButton [][] b){
        JButton [][] a = b;
        
        for(int i=0; i < a.length; i++){
            for(int u=0; u < a.length; u++){
                if(a[i][u]!=null){
                    if(a[i][u].getBackground()==Color.LIGHT_GRAY){
                        a[i][u].setBackground(Color.WHITE);
                        a[i][u].setText(a[i][u].getName());
                    }else{
                        a[i][u].setText(a[i][u].getName());
                    }
                }
            }
        }
    }
    
    public  static void abrir(int x, int y, String [][] j, JButton [][] b){
        String [][] buscac = j;
        JButton [][] a = b;
        
        if(a[x][y]!=null){
            if(a[x][y].getText().equals("")){
                //System.out.println("color: " + a[x][y].getBackground().getBlue());
                        a[x][y].setBackground(Color.white);
                        a[x][y].setText(a[x][y].getName());
                    if(a[x][y].getName().equals(" ")){
                        
                        abrir(x,y-1,buscac,a);
                        abrir(x,y+1,buscac,a);
                        abrir(x+1,y,buscac,a);
                        abrir(x-1,y,buscac,a);
                    }else{
                        return;
                    }
            }
        }

    }

}