package Base_datos;

import java.io.IOException;
import java.sql.SQLException;

public class Base_venta extends Conexion{
    
    private static final String[] COLUMNAS = {"ID","ID P","PRODUCTO","FECHA","PRECIO V", "CANTIDAD"};
    private static final String INSERTAR = "INSERT INTO VENTA (pro_id, ven_precio, ven_cantidad) VALUES (?,(SELECT pro_precio_venta FROM PRODUCTO WHERE pro_id = ?),?);";
    private static final String ELIMINAR = "DELETE FROM VENTA WHERE ven_id = ?;";
    private static final String CONSULTAR_MUCHOS = "SELECT * FROM VW_VENTA WHERE ven_id = ? OR pro_id LIKE ? OR pro_nombre LIKE ? OR ven_fecha LIKE ?;";
    private static final String CANTIDAD_MUCHOS = "SELECT COUNT(*) FROM VW_VENTA WHERE ven_id = ? OR pro_id LIKE ? OR pro_nombre LIKE ? OR ven_fecha LIKE ?;";
    private static final String CONSULTAR_UNO = "SELECT * FROM VW_VENTA WHERE ven_id = ?;";

    /**
     * Este es el metodo constructor
     * para la calse Base_venta, el
     * cual se utiliza la conexion con
     * la base de datos.
     * @throws IOException
     * @throws SQLException
     */
    public Base_venta() throws IOException, SQLException{
        
        super();

    }


    /**
     * Este metodo se encarga de insertar un registro 
     * en la tabla venta utilizando los parametros dados
     * @param id Debe ser el id del producto que se vendio
     * @param cantidad Debe ser la cantidad de producto que se vendio
     * @throws SQLException
     */
    public void insertar(long id, long cantidad) throws SQLException, IOException{
        Base_inventario inventario = null;

        try{
            
            // Prepara lo que se va a ejecutar en la base de datos
            pstate = conexion.prepareStatement(INSERTAR);

            // Modifica los valores que se van a insertar en el String predefinido
            pstate.setLong(1, id);
            pstate.setLong(2, id);
            pstate.setLong(3, cantidad);

            // Ejecuta la insercion
            pstate.executeUpdate();

        }catch(SQLException ex){    // Por si recibe algun error
            
            if(ex.getErrorCode() == 19){
                String datos = "";
                String nombre = "";
                try{

                    inventario = new Base_inventario();
                    datos = inventario.consultar_uno(id)[2];
                    nombre = inventario.consultar_uno(id)[1];
                    
                }catch(IOException e){

                    throw e;

                }finally{
                    if(inventario != null)
                        inventario.close();
                }

                ex = new SQLException("No hay suficiente inventario disponible para el producto: " + nombre + "\nCantidad solicitada: " + cantidad + "\nCantidad disponible: " + datos);


            }
            
            throw ex;

        }finally{   // Finaliza la utilizacion del objeto utlizado

            if(pstate != null)
                pstate.close();
            if(inventario != null)
                inventario.close();

        }


    }

    /**
     * Este metodo se encarga de eliminar un registro
     * de venta en la base de datos
     * @param id Debe ser el id de la venta
     * @throws SQLException
     */
    public void eliminar(long id) throws SQLException{

        try{

            // Prepara lo que se va a ejecutar en la base de datos
            pstate = conexion.prepareStatement(ELIMINAR);

            // Establece los valores que se van a modificar en el String ELIMINAR
            pstate.setLong(1, id);
            
            // Ejecuta en la base de datos
            pstate.executeUpdate();

        }catch(SQLException ex){

            throw ex;

        }finally{
            
            if(pstate != null)
                pstate.close();

        }
    }

    /**
     * Este metodo se encarga de realizar una consulta,
     * utilizando el parametro, para filtar los datos,
     * retornando una matriz con la cabecera de la tabla
     * y los datos correspondientes si existen
     * @param parametro Puede ser, una parte del id del producto
     * una parte del id de la venta, una subcadena del nombre o la fecha
     * en cualquier subcadena de la forma "yyyy-mm-dd"
     * @return retorna una matriz tipo String[][] con la cabecera
     * de los datos y los datos en caso de encontrar estos registros,
     * en caso de no encontrar los registros, retorna simplmenten la
     * cabecera de la tabla.
     * @throws SQLException
     */
    public String[][] consultar(String parametro) throws SQLException{
        
        String datos[][] = null;

        String id = parametro + "%";                    // Sirve para filtrar por el numero
        String parametro_aux = "%" + parametro + "%";   // Sirve para filtar por el nombre, o por fecha

        int cantidad = 0;   // Este es el parametro que sirve para reservar memoria del arreglo

        try{

            // Se prepara la consulta de la cantidad de retornos
            pstate = conexion.prepareStatement(CANTIDAD_MUCHOS);

            // Se modifican los parametros a utilizar en la consulta
            pstate.setString(1, id);
            pstate.setString(2, id);
            pstate.setString(3, parametro_aux);
            pstate.setString(4, parametro_aux);
            
            // Se ejecuta la consulta
            result = pstate.executeQuery();

            if(result.next()){  // Si hay resultados, reestable el valor de cantidad

                cantidad = result.getInt(1);

            }
            
            // Reserva la memoria con los datos encontrados
            datos = new String[cantidad + 1][COLUMNAS.length];

            datos[0] = COLUMNAS;    // Establece el encabezado de la tabla

            // Limpia los campos anteriores
            pstate.close();
            result.close();

            // Se prepara la consulta
            pstate = conexion.prepareStatement(CONSULTAR_MUCHOS);

            // Se modifican los parametros a utilizar en la consulta
            pstate.setString(1, id);
            pstate.setString(2, id);
            pstate.setString(3, parametro_aux);
            pstate.setString(4, parametro_aux);
            
            // Se ejecuta la consulta
            result = pstate.executeQuery();

            // Se encarga de agregar los demas valores a los campos restantes de la matriz
            for(int i = 1; result.next(); i++){
                for(int j = 0; j < COLUMNAS.length; j++){
                    datos[i][j] = result.getString(j+1);
                }
            }

        }catch(SQLException ex){

            throw ex;

        }finally{

            if(pstate != null)
                pstate.close();

            if(result != null)
                result.close();

        }

        return datos;
    }

    /**
     * Este metodo se encarga de consultar unicamente
     * un registro de la tabal venta, utilizando
     * el id de la venta
     * @param id Deberia ser el id de la venta
     * @return Retorna un arreglo tipo String[] con
     * los datos en caso de encontrarlos, en caso de
     * no encontrarlos retorna el arreglo pero con null
     * @throws SQLException
     */
    public String[] consultar_uno(long id)throws SQLException{

        String datos[] = new String[COLUMNAS.length];   // Reserva el espacio para los datos

        try{

            pstate = conexion.prepareStatement(CONSULTAR_UNO);  // Prepara la consulta de un unico registro

            pstate.setLong(1, id);  // Modifica los datos necesarios para la consulta

            result = pstate.executeQuery(); // Ejecuta la consulta y lo almacena en result

            if(result.next()){  // Si hay datos ejecuta el for que se encarga de introducirlos en el arreglo

                for(int j = 0; j < COLUMNAS.length; j++){
                    datos[j] = result.getString(j+1);
                }

            }

        }catch(SQLException ex){

            throw ex;

        }finally{

            pstate.close();
            result.close();

        }

        return datos;
    }

}
