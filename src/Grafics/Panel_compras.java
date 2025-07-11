package Grafics;

import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import java.awt.event.WindowEvent;
import Base_datos.Base_compra;
import Grafics.Dialogos.Adicionar_compras;
import Grafics.Utilidades.Modelo_tabla;

public class Panel_compras extends Panel_central{
    
    private Base_compra base_compra;

    public Panel_compras(){

        super();
        boton_adicionar.setToolTipText("Adicionar Compras"); // Muestra el tooltip
        pop_menu.remove(item_modificar);
        

    }


    @Override
    protected void cargar_datos_tabla() {
        
        
        try{
            base_compra = new Base_compra();   // Hace una coneccion a la base de datos
            tabla = Modelo_tabla.set_tabla_compras( // Pone un formato para la tabla
                base_compra.consultar("") // Pasa los datos que va a tener la tabla
            );
            
        }catch(SQLException|IOException ex){
            // En caso que haya un error, muestra este mensaje de error con el motivo
            JOptionPane.showMessageDialog(this, ex.getMessage()+"\nCerrando el Programa", "Error", JOptionPane.ERROR_MESSAGE);
            
            // Esto se utiliza para cerrar el programa despues del error
            if (window != null) {
                window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
            }

        }finally{
            base_compra.close();
            
        }   

    }

    @Override
    protected void accion_text_bucar() {
        try{
            base_compra = new Base_compra();
            // Obtiene los datos y crea una tabla auxiliar con los datos proporcionados por el text Field
            JTable tabla_aux = Modelo_tabla.set_tabla_compras(
                base_compra.consultar(text_bucar.getText())
            );

            // Estos metodos se encargan que el formato de la tabla se aplique sin afectar sus propiedades
            tabla.setModel(tabla_aux.getModel());
            tabla.setColumnModel(tabla_aux.getColumnModel());

        }catch(SQLException|IOException ex){
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally{
            base_compra.close();
        }
    }

    @Override
    protected void config_listener_pop_menu() {
        
        
        item_eliminar.addActionListener(_ ->{

            int number = tabla.getSelectedRow();
            String valor = "" + tabla.getValueAt(number, 0) + " | " + tabla.getValueAt(number, 2) + " | " + tabla.getValueAt(number, 3);
            int id = Integer.parseInt((String) tabla.getValueAt(number, 0));

            number = JOptionPane.showConfirmDialog(this, "Esta seguro que deceas eliminar la compra:\n"+ valor, "eliminar", JOptionPane.OK_CANCEL_OPTION);
            if(number == 0){
                
                try{
                    base_compra = new Base_compra();
                    base_compra.eliminar(id);
                    JOptionPane.showMessageDialog(this, "Compra eliminado correctamente");
                }catch(SQLException|IOException ex){
                    JOptionPane.showMessageDialog(this,ex,"Error",JOptionPane.ERROR_MESSAGE);
                }finally{
                    base_compra.close();
                }
                
                accion_text_bucar();
            }
                  
        });

    }

    @Override
    protected void accion_adicionar() {
        
        Adicionar_compras compras = new Adicionar_compras((JFrame) SwingUtilities.getWindowAncestor(this), () -> accion_text_bucar());

        compras.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Se ejecuta cuando el diálogo se cierra
                accion_text_bucar();
            }
        });

        compras.setVisible(true);

    }

    
}

