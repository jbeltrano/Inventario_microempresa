/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author juanp
 */

import java.awt.Dimension;
import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        FlatLightLaf.setup();
        JFrame frame = new JFrame("I am here");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setPreferredSize(new Dimension(300,300));
        frame.setMinimumSize(new Dimension(300,300));
        frame.setVisible(true);

    }
    
}
