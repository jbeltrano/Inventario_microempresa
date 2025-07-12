package Grafics;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import Base_datos.Base_producto;
import Grafics.Dialogos.Actualizar_producto;
import Grafics.Dialogos.Adicinar_producto;
import Grafics.Utilidades.Modelo_tabla;
import java.io.IOException;

public class Panel_inventario extends Panel_central{
    
    private Base_producto base_producto;

    public Panel_inventario(){
        super();
        boton_adicionar.setToolTipText("Adicionar inventario"); // Muestra el tooltip
        label_panel.setText("Producto e Inventario");
    }

    @Override
    protected void cargar_datos_tabla() {
        
        
        try{
            base_producto = new Base_producto();   // Hace una coneccion a la base de datos
            tabla = Modelo_tabla.set_tabla_inventario( // Pone un formato para la tabla
                base_producto.consultar("") // Pasa los datos que va a tener la tabla
            );
            
        }catch(SQLException|IOException ex){
            // En caso que haya un error, muestra este mensaje de error con el motivo
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage()+"\nCerrando el Programa", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);

            

        }finally{
            base_producto.close();
            
        }   

    }

    @Override
    protected void accion_text_bucar() {
        try{
            base_producto = new Base_producto();
            // Obtiene los datos y crea una tabla auxiliar con los datos proporcionados por el text Field
            JTable tabla_aux = Modelo_tabla.set_tabla_inventario(
                base_producto.consultar(text_bucar.getText())
            );

            // Estos metodos se encargan que el formato de la tabla se aplique sin afectar sus propiedades
            tabla.setModel(tabla_aux.getModel());
            tabla.setColumnModel(tabla_aux.getColumnModel());

        }catch(SQLException|IOException ex){
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally{
            base_producto.close();
        }
    }

    @Override
    protected void config_listener_pop_menu() {
        
        item_modificar.addActionListener(_ ->{

            int select_row = tabla.getSelectedRow();
            Actualizar_producto actualizar = new Actualizar_producto(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    () -> accion_text_bucar(), 
                    Long.parseLong((String) tabla.getValueAt(select_row, 0)));
            
            actualizar.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    // Se ejecuta cuando el diálogo se cierra
                    accion_text_bucar();
                }
            });

            actualizar.setVisible(true);

        });
        item_eliminar.addActionListener(_ ->{

            int number = tabla.getSelectedRow();
            String valor = "" + tabla.getValueAt(number, 1);
            int id = Integer.parseInt((String) tabla.getValueAt(number, 0));

            number = JOptionPane.showConfirmDialog(this, "Esta seguro que deceas eliminar el producto:\n"+ valor, "eliminar", JOptionPane.OK_CANCEL_OPTION);
            if(number == 0){
                
                try{
                    base_producto = new Base_producto();
                    base_producto.eliminar(id);
                    JOptionPane.showMessageDialog(this, "Producto eliminado correctamente");
                }catch(SQLException|IOException ex){
                    JOptionPane.showMessageDialog(this,ex.getLocalizedMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }finally{
                    base_producto.close();
                }
                
                accion_text_bucar();
            }
                  
        });

    }

    @Override
    protected void accion_adicionar() {
        
        Adicinar_producto producto = new Adicinar_producto((JFrame) SwingUtilities.getWindowAncestor(this), () -> accion_text_bucar());

        producto.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Se ejecuta cuando el diálogo se cierra
                accion_text_bucar();
            }
        });

        producto.setVisible(true);

    }
    
}
