package Grafics.Dialogos;

import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import Base_datos.Base_inventario;
import Base_datos.Base_producto;
import Grafics.Utilidades.Producto_callback;

public class Actualizar_producto extends Adicinar_producto{
    
    private long id_producto;


    public Actualizar_producto(JFrame padre, Producto_callback callback, long id_producto){

        super(padre, callback);
        this.id_producto = id_producto;
        cargar_datos();

    }

    private void cargar_datos(){

        Base_producto producto = null;
        Base_inventario inventario = null;
        String[] datos;
        String box;
        long cantida_producto;
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
    protected void guardar(
            String nombre,
            long cantidad,
            long valor_compra,
            long valor_venta,
            long id_ubicacion,
            String nota
            ){

        Base_producto producto = null;

        try{
            
            producto = new Base_producto();
            
            producto.actualizar(
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
        }
    }
}
