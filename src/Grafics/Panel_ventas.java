package Grafics;

import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import java.awt.event.WindowEvent;
import Base_datos.Base_venta;
import Grafics.Dialogos.Adicionar_ventas;
import Grafics.Utilidades.Modelo_tabla;

public class Panel_ventas extends Panel_central{
    
    private Base_venta base_venta; 

    public Panel_ventas(){
        super();
        boton_adicionar.setToolTipText("Adicionar Ventas");
        label_panel.setText("Ventas");
        pop_menu.remove(item_modificar);

    }

    @Override
    protected void cargar_datos_tabla() {
        
        
        try{
            base_venta = new Base_venta();   // Hace una coneccion a la base de datos
            tabla = Modelo_tabla.set_tabla_compras( // Pone un formato para la tabla
                base_venta.consultar("") // Pasa los datos que va a tener la tabla
            );
            
        }catch(SQLException|IOException ex){
            // En caso que haya un error, muestra este mensaje de error con el motivo
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage()+"\nCerrando el Programa", "Error", JOptionPane.ERROR_MESSAGE);
            
            // Esto se utiliza para cerrar el programa despues del error
            if (window != null) {
                window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
            }

        }finally{
            base_venta.close();
            
        }   

    }

    @Override
    protected void accion_text_bucar() {
        try{
            base_venta = new Base_venta();
            // Obtiene los datos y crea una tabla auxiliar con los datos proporcionados por el text Field
            JTable tabla_aux = Modelo_tabla.set_tabla_compras(
                base_venta.consultar(text_bucar.getText())
            );

            // Estos metodos se encargan que el formato de la tabla se aplique sin afectar sus propiedades
            tabla.setModel(tabla_aux.getModel());
            tabla.setColumnModel(tabla_aux.getColumnModel());

        }catch(SQLException|IOException ex){
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally{
            base_venta.close();
        }
    }

    @Override
    protected void config_listener_pop_menu() {
        
        
        item_eliminar.addActionListener(_ ->{

            int number = tabla.getSelectedRow();
            String valor = "" + tabla.getValueAt(number, 0) + " | " + tabla.getValueAt(number, 2) + " | " + tabla.getValueAt(number, 3);
            int id = Integer.parseInt((String) tabla.getValueAt(number, 0));

            number = JOptionPane.showConfirmDialog(this, "Esta seguro que deceas eliminar la venta:\n"+ valor, "eliminar", JOptionPane.OK_CANCEL_OPTION);
            if(number == 0){
                
                try{
                    base_venta = new Base_venta();
                    base_venta.eliminar(id);
                    JOptionPane.showMessageDialog(this, "Venta eliminado correctamente");
                }catch(SQLException|IOException ex){
                    JOptionPane.showMessageDialog(this,ex.getLocalizedMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }finally{
                    base_venta.close();
                }
                
                accion_text_bucar();
            }
                  
        });

    }

    @Override
    protected void accion_adicionar() {
        
        Adicionar_ventas ventas = new Adicionar_ventas((JFrame) SwingUtilities.getWindowAncestor(this), () -> accion_text_bucar());

        ventas.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Se ejecuta cuando el diálogo se cierra
                accion_text_bucar();
            }
        });

        ventas.setVisible(true);

    }

}
