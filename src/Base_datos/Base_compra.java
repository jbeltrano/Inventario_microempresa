package Base_datos;

import java.io.IOException;
import java.sql.SQLException;

public class Base_compra extends Conexion{
    
    private static final String[] COLUMNAS = {"ID","ID PRODUCTO","PRODUCTO","FECHA","PRECIO", "CANTIDAD"};
    private static final String INSERTAR = "INSERT INTO COMPRA (pro_id, com_precio, com_cantidad) VALUES (?,(SELECT pro_precio_compra FROM PRODUCTO WHERE pro_id = ?),?);";
    private static final String ELIMINAR = "DELETE FROM COMPRA WHERE com_id = ?;";
    private static final String CONSULTAR_MUCHOS = "SELECT * FROM VW_COMPRA WHERE com_id = ? OR pro_id LIKE ? OR pro_nombre LIKE ? OR com_fecha LIKE ?;";
    private static final String CANTIDAD_MUCHOS = "SELECT COUNT(*) FROM VW_COMPRA WHERE com_id = ? OR pro_id LIKE ? OR pro_nombre LIKE ? OR com_fecha LIKE ?;";
    private static final String CONSULTAR_UNO = "SELECT * FROM VW_COMPRA WHERE com_id = ?;";
    
    /**
     * Metodo constructor
     * @throws IOException
     * @throws SQLException
     */
    public Base_compra()throws IOException, SQLException{
        super();
    }
    

    /**
     * Este metodo se encarga de insertar un registro
     * de compra en la base de datos
     * @param id es el id del producto
     * @param cantidad es la cantidad de producto comprada
     * @throws SQLException
     */
    public void insertar(long id, long cantidad) throws SQLException{
        
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

            throw ex;

        }finally{   // Finaliza la utilizacion del objeto utlizado

            if(pstate != null)
                pstate.close();

        }


    }

    /**
     * Este metodo se encarga de eliminar un registro
     * de las compras
     * @param id es el id o consecutivo
     * de la compra que se realizo
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

            
            if(ex.getErrorCode() == 19 ){

                ex = new SQLException("No es posible elminar esta compra, puesto que el inventario quedaria negativo.");
            }
            throw ex;

        }finally{
            
            if(pstate != null)
                pstate.close();

        }
    }

    /**
     * Este metodo se utiiza para consultar los datos de la
     * vista vw_compra
     * @param parametro este parametro se utiliza para filtrar
     * en la tabla compra por el id del producto, la fecha en
     * cualquier subcadena de la forma "yyyy-mm-dd" y por el
     * nombre del producto
     * @return retorna una matriz con los datos 
     * y los encabezados de la vista vw_compra
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
     * Este metodo se encarga de retornar un unico registro
     * de la vista vw_compra, utilizando el id de la compra
     * para identificar de manera unica el registro
     * @param id es el id de la compra
     * @return retorna un arreglo de tipo String[]
     * con los datos en caso de ser encontrados
     * en caso que no sea asi retorna el arreglo vacio
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
