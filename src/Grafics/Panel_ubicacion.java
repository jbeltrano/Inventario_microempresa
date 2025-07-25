package Grafics;

import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import java.awt.event.WindowEvent;
import Base_datos.Base_ubicacion;
import Grafics.Dialogos.Actualizar_ubicacion;
import Grafics.Dialogos.Adicionar_ubicacion;
import Grafics.Utilidades.Modelo_tabla;

public class Panel_ubicacion extends Panel_central{
    
    private Base_ubicacion base_ubicacion;

    public Panel_ubicacion(){
        
        super();
        boton_adicionar.setToolTipText("Adicionar Ubicacion");
        label_panel.setText("Ubicación");
    }

    @Override
    protected void cargar_datos_tabla() {
        
        
        try{
            base_ubicacion = new Base_ubicacion();   // Hace una coneccion a la base de datos
            tabla = Modelo_tabla.set_tabla_ubicacion( // Pone un formato para la tabla
                base_ubicacion.consultar("") // Pasa los datos que va a tener la tabla
            );
            
        }catch(SQLException|IOException ex){
            // En caso que haya un error, muestra este mensaje de error con el motivo
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage()+"\nCerrando el Programa", "Error", JOptionPane.ERROR_MESSAGE);
            
            // Esto se utiliza para cerrar el programa despues del error
            if (window != null) {
                window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
            }

        }finally{
            base_ubicacion.close();
            
        }   

    }

    @Override
    protected void accion_text_bucar() {
        try{
            base_ubicacion = new Base_ubicacion();
            // Obtiene los datos y crea una tabla auxiliar con los datos proporcionados por el text Field
            JTable tabla_aux = Modelo_tabla.set_tabla_ubicacion(
                base_ubicacion.consultar(text_bucar.getText())
            );

            // Estos metodos se encargan que el formato de la tabla se aplique sin afectar sus propiedades
            tabla.setModel(tabla_aux.getModel());
            tabla.setColumnModel(tabla_aux.getColumnModel());

        }catch(SQLException|IOException ex){
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally{
            base_ubicacion.close();
        }
    }

    @Override
    protected void config_listener_pop_menu() {
        
        item_modificar.addActionListener(_ ->{

            int select_row = tabla.getSelectedRow();
            Actualizar_ubicacion actualizar = new Actualizar_ubicacion(
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
            String valor = "" + tabla.getValueAt(number, 0) + " | " + tabla.getValueAt(number, 1);
            long id = Long.parseLong((String) tabla.getValueAt(number, 0));

            number = JOptionPane.showConfirmDialog(this, "Esta seguro que deceas eliminar la ubicacion:\n"+ valor, "eliminar", JOptionPane.OK_CANCEL_OPTION);
            if(number == 0){
                
                try{

                    base_ubicacion = new Base_ubicacion();
                    base_ubicacion.eliminar(id);
                    JOptionPane.showMessageDialog(this, "Ubicacion eliminado correctamente");

                }catch(SQLException|IOException ex){

                    JOptionPane.showMessageDialog(this,ex.getLocalizedMessage(),"Error",JOptionPane.ERROR_MESSAGE);

                }finally{

                    base_ubicacion.close();

                }
                
                accion_text_bucar();
            }
                  
        });

    }

    @Override
    protected void accion_adicionar() {
        
        Adicionar_ubicacion ubicacion = new Adicionar_ubicacion((JFrame) SwingUtilities.getWindowAncestor(this), () -> accion_text_bucar());
        
        ubicacion.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Se ejecuta cuando el diálogo se cierra
                accion_text_bucar();
            }
        });

        ubicacion.setVisible(true);

    }

}
