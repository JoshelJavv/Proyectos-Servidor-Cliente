/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logincliente;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.SOUTH;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author android-4ff5z998
 */
public class LoginCliente {

    String serverAddress;
    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(50);
    JTextArea messageArea = new JTextArea(16, 5);
    Map<String, Integer> lista = new HashMap<String, Integer>();

    public LoginCliente(String serverAddress) {
        this.serverAddress = serverAddress;
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.pack();
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }

    private String getName() {
        return JOptionPane.showInputDialog(
                frame, "choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);

    }

    ;
    
    private String getPass(){
        return JOptionPane.showInputDialog(
        frame, "Set a Password", "User Password",
                JOptionPane.PLAIN_MESSAGE);
    }
    
    ;
    
    private void passwrong(){
        JOptionPane.showMessageDialog(frame, "Contraseña Incorrecta");
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
                    name = getName();
                    while(name.contains(" ") || name.equalsIgnoreCase("null") || name.equals("")){
                        name = getName();
                    }
                    out.println(name);
                    //lista.put(getName(), 1);
                }
                else if(line.startsWith("SUBMITPASS")){
                    pass = getPass();
                    while(pass.contains(" ") || pass.equalsIgnoreCase("null") || pass.equals("")){
                        pass = getPass();
                    }
                    out.println(pass);
                }
                else if(line.startsWith("INCORRECT")){
                    passwrong();
                }
                else if (line.startsWith("NAMEACCEPTED")) {
                    this.frame.setTitle("Chatter - " + line.substring(13));
                    textField.setEditable(true);
                    
                } else if (line.startsWith("MESSAGE")) {
                    //messageArea.setForeground(Color.black);
                    messageArea.append(line.substring(8) + "\n");
                }else if(line.startsWith("PRIVATE")){
                    //messageArea.setForeground(Color.yellow);
                    messageArea.append(line.substring(8) + "\n");
                }
            }
        } finally {
            frame.setVisible(false);
            frame.dispose();
        }

    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server ip as the solve ");
            return;
        }
        LoginCliente client = new LoginCliente(args[0]);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();

    }

}