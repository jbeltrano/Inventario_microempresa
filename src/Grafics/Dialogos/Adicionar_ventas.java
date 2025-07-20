package Grafics.Dialogos;

import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import Base_datos.Base_venta;
import Grafics.Utilidades.Generic_callback;

public class Adicionar_ventas extends Adicionar_compras{
    

    public Adicionar_ventas(JFrame padre, Generic_callback callback){

        super(padre, callback);
        config();
        
    }

    private void config(){

        setTitle("Formulario de Ventas");
        boton_guardar.setText("Agregar Venta");
    }

    @Override
    protected boolean insertarCompraEnBD(long idProducto, long cantidad) {
        // Aquí implementarías la inserción en tu base de datos
        
        Base_venta venta = null;

        try{
            venta = new Base_venta();
            venta.insertar(idProducto, cantidad);

        }catch(SQLException | IOException ex){
            JOptionPane.showMessageDialog(Adicionar_ventas.this, ex.getLocalizedMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
            return false;
        }finally{
            if(venta != null){
                venta.close();
            }
        }

        return true;
    }

    @Override
    protected String personalizar_mensaje(){

        return String.format(
                "Venta registrada exitosamente:\n\n" +
                "Producto: %s\n" +
                "ID: %d\n" +
                "Cantidad: %d\n" +
                "Precio unitario: $%.2f\n" +
                "Total: $%.2f",
                productoSeleccionado.getNombre(),
                productoSeleccionado.getId(),
                cantidad,
                productoSeleccionado.getPrecio(),
                total
            );
            
    }

}
