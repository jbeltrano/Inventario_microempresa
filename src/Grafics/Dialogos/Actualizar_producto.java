package Grafics.Dialogos;

import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import Base_datos.Base_inventario;
import Base_datos.Base_producto;
import Grafics.Utilidades.Generic_callback;

public class Actualizar_producto extends Adicinar_producto{
    
    private long id_producto;

    /**
     * Metodo constructor de la clase
     * Actualizar producto
     * @param padre este deberia ser el
     * JFrame padre a utilizar
     * @param callback Este es el metodo
     * que se va a ejecutar despues de
     * oprimirle al boton mas
     * @param id_producto Este es el id
     * del producto a modificar
     */
    public Actualizar_producto(JFrame padre, Generic_callback callback, long id_producto){

        super(padre, callback);
        this.id_producto = id_producto;
        cargar_datos();

    }

    /**
     * Este metodo se encarga de precargar los datos
     * en los diferentes campos del fromulario
     */
    private void cargar_datos(){

        Base_producto producto = null;
        Base_inventario inventario = null;
        String[] datos;
        String box;
        long cantida_producto;
        titleLabel.setText("MODIFICAR PRODUCTO: " + id_producto);

        try{

            producto = new Base_producto();
            inventario = new Base_inventario();

            datos = producto.consultar_uno(id_producto);
            cantida_producto = Long.parseLong(inventario.consultar_uno(id_producto)[2]);
            box = producto.consultar_ubi(id_producto) + "| " + datos[5];
            
            text_nombre.setText(datos[1]);
            text_valor_c.setText(datos[2]);
            text_valor_v.setText(datos[3]);
            text_cantidad.setEnabled(false);
            text_cantidad.setText(""+cantida_producto);
            ubicacionComboBox.setSelectedItem(box);
            area_text_notas.setText(datos[6]);
            
        }catch(SQLException | IOException ex){

            JOptionPane.showMessageDialog(Actualizar_producto.this, ex, "Error", JOptionPane.ERROR_MESSAGE);

        }finally{

            if(producto != null){
                producto.close();
            }
            if(inventario != null){
                inventario.close();
            }
        }

        
    }

    @Override
    protected void setupEventListeners(){
        super.setupEventListeners();
        
    }

    @Override
    protected void guardar(
            String nombre,      // El nuevo nombre del producto
            long cantidad,      // Es la cantidad, pero no interfiere
            long valor_compra,  // Este es el nuevo valor de la compra
            long valor_venta,   // Este es el nuevo valor de la venta
            long id_ubicacion,  // Este es el nuevo id de la ubicacvion
            String nota         // Esta es la nueva nota
            ){

        Base_producto producto = null;

        try{
            
            producto = new Base_producto();
            
            producto.actualizar(    // Simplemente actualiza en la base de datos
                id_producto,
                valor_compra,
                valor_venta,
                id_ubicacion,
                nota,
                nombre
            );

        }catch(SQLException | IOException ex){

            JOptionPane.showMessageDialog(Actualizar_producto.this, ex, "Error", JOptionPane.ERROR_MESSAGE);

        }finally{

            if(producto != null){
                producto.close();
            }
            Actualizar_producto.this.dispose();
        }
    }
}
