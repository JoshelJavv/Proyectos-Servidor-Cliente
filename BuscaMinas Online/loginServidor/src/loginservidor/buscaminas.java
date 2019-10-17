/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginservidor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author android-4ff5z998
 */
public class buscaminas {
    static int size = 30;
    
    public String [][] llenar(){
        String [][] tablero = new String[size+2][size+2];
        
        for(int i=0; i < tablero.length; i++){
            for(int u=0; u < tablero.length; u++){
                    tablero[i][u] = "S";
            }
        }
        return tablero;
    }

    public  String [][] minas(){
       int alea;
       int torio;
       int minas = 0;
       String [][] tablero = llenar();
        System.out.println("tablero: " + tablero.length);
       while(minas<70){
           alea = (int) ((Math.random()*(tablero.length-2))+1);
           torio = (int) ((Math.random()*(tablero.length-2))+1);
           if(!tablero[alea][torio].equalsIgnoreCase("m")){
               tablero[alea][torio] = "M";
               minas++;
           }
           
       }
       return tablero;
    }
    
    
    public String[][] ruleta(){
        String [][] phantom = new String[3][3];
        for(int a=0; a<phantom.length; a++){
            for(int x=0; x < phantom.length; x++){
                phantom[a][x] = "S";
            }
        }
        return phantom;
    }
    
    public String numero(String [][] mandalo){
        String [][] num = mandalo;
        int suma = 0;
        String mandar ="";
        for(int i=0; i < num.length; i++){
            for(int u=0; u < num.length; u++){
                if(num[i][u].equalsIgnoreCase("m")){
                    suma++;
                }
            }
        }
        //System.out.println("numbero: " + suma);
        mandar = "" +suma;
        return mandar;
    }
    
    public String [][] contar(){
        String [][] tablero = minas();
        String [][] rule = new String[3][3];
        
        for(int i=0; i < tablero.length; i++){
            for(int u=0; u < tablero.length; u++){
                rule = ruleta();
                if(!tablero[i][u].equalsIgnoreCase("m")){
                 rule[1][1] = tablero[i][u]; 
                
                 //que no sea esquina, ni lateral
                 if(i!=0 && i!=tablero.length-1 && u!=0 && u!=tablero.length-1){
                     rule[0][0] = tablero[i-1][u-1];
                     rule[0][1] = tablero[i-1][u];
                     rule[0][2] = tablero[i-1][u+1];
                     rule[1][0] = tablero[i][u-1];
                     rule[1][2] = tablero[i][u+1];
                     rule[2][0] = tablero[i+1][u-1];
                     rule[2][1] = tablero[i+1][u];
                     rule[2][2] = tablero[i+1][u+1];
                     tablero[i][u] = numero(rule);
                     if(tablero[i][u].equals("0")){
                         tablero[i][u] = " ";
                     }
                 }
                }
            }
        }
        System.out.println("rule--------------------");
        for(int i=0; i < 3; i++){
            for(int u=0; u < 3; u++){
                System.out.print(rule[i][u] + " ");
            }
            System.out.println("");
        }
        System.out.println("Servidor iniciado");
        return tablero;
    }
    

}
