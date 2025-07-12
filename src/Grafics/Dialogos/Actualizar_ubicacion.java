package Grafics.Dialogos;

import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Base_datos.Base_ubicacion;
import Grafics.Utilidades.Generic_callback;

public class Actualizar_ubicacion extends Adicionar_ubicacion{
    
    private long id;

    public Actualizar_ubicacion(JFrame padre, Generic_callback callback, long id){

        super(padre, callback);
        this.id = id;
        boton_guardar.setText("Actualizar ubicacion");
        cargar_datos();

    }

    private void cargar_datos(){

        Base_ubicacion base_ubicacion = null;
        String[] datos;

        try{

            base_ubicacion = new Base_ubicacion();
            datos = base_ubicacion.consultar_uno(id);
            text_nombre.setText(datos[1]);

        }catch(SQLException | IOException ex){

            JOptionPane.showMessageDialog(Actualizar_ubicacion.this, ex, "Error", JOptionPane.ERROR_MESSAGE);

        }finally{

            if(base_ubicacion != null){

                base_ubicacion.close();

            }
        }


    }

    @Override
    protected String personalizar_mensjae(){

        return String.format(
                "Ubicacion actualizada exitosamente:\n\n" +
                "Ubicacion: %s\n",
                text_nombre.getText()
            );

    }
    
    protected void guardar(){
        Base_ubicacion ubicacion = null;

            try{
                ubicacion = new Base_ubicacion();
                ubicacion.actualizar(id, text_nombre.getText());

            }catch(SQLException | IOException ex){
                JOptionPane.showMessageDialog(Actualizar_ubicacion.this, ex, getTitle(), JOptionPane.ERROR_MESSAGE);
                
            }finally{
                if(ubicacion != null){
                    ubicacion.close();
                }

                Actualizar_ubicacion.this.dispose();
            }
            
    }

}
