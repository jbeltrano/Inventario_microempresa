package Grafics.Utilidades;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public abstract class Key_adapter extends KeyAdapter{
    private char key;
    
    /**
     * Constructor por defecto para
     * esta clase
     */
    public Key_adapter(){
        super();
    }
    
    /**
     * Este metodo es el que se encarga
     * de realizar los diferentes eventos
     * de teclado cuando sucedan
     */
    @Override
    public void keyReleased(KeyEvent evt){
        key = evt.getKeyChar();
        if (Character.isLetterOrDigit(key) || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_SPACE) {
            
            accion();
            
        }else if(evt.getExtendedKeyCode() == KeyEvent.VK_ENTER){
            
            accion2();
        }

    }
    
    /**
     * Metodo utilizado por el usuario cuando
     * necesite realizar alguna accion especifca
     * cuando se digiten letras, numeros, etc.
     */
    abstract public void accion();
    /**
     * En este caso son las acciones que se van
     * a realizar cuando se digite la tecla espacio
     */
    abstract public void accion2();
}
