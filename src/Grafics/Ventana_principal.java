package Grafics;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.Dimension;

public class Ventana_principal extends JFrame{
    
    private ImageIcon icono;
    

    /**
     * Este es el constructor de la clase
     */
    public Ventana_principal(){
        
        super("Inventario");    // Se pasa a la superclase el titulo de la ventana
        
        FlatLightLaf.setup();
        setPreferredSize(new Dimension(1200,700));  // Se define el tamaño por defecto de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Se define la operacion por defecto al oprimir el boton x
        iniciar_componentes();                          // Inicia los componentes necesrios para la ventana
        pack();                                         // Estaviliza todo en conjutno para mastrar la ventana
        setLocationRelativeTo(null);                    // Hace que la ventana aparesca en el centro de la pantalla
        setVisible(true);                               // Hace visible la interfaz
        
        
    }


    private void iniciar_componentes(){
        Panel_principal panel = new Panel_principal();
        add(panel);
        icono = new ImageIcon(getClass().getResource("/Grafics/Recursos/logo.png"));
        setIconImage(icono.getImage());
    }

}
