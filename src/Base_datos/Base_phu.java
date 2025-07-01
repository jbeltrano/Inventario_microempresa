package Base_datos;

import java.io.IOException;
import java.sql.SQLException;

public class Base_phu extends Conexion{
    
    private static final String[] COLUMNAS = {"ID","PRODUCTO","ID UBI","UBICACION"};
    private static final String INSERTAR = "INSERT INTO PRODUCTO_HAS_UBICACION (pro_id, ubi_id) VALUES (?,?);";
    private static final String ELIMINAR = "DELETE FROM PRODUCTO_HAS_UBICACION WHERE pro_id = ?;";
    private static final String CONSULTAR_MUCHOS = "SELECT * FROM VW_PHU WHERE pro_id LIKE ? OR ubi_id LIKE ? OR pro_nombre LIKE ? OR ubi_nombre LIKE ?;";
    private static final String CANTIDAD_MUCHOS = "SELECT COUNT(*) FROM VW_PHU WHERE pro_id LIKE ? OR ubi_id LIKE ? OR pro_nombre LIKE ? OR ubi_nombre LIKE ?;";
    private static final String CONSULTAR_UNO = "SELECT * FROM VW_PHU WHERE pro_id = ?;";
    private static final String ACTUALIZAR = "UPDATE PRODUCTO_HAS_UBICACION SET ubi_id = ? WHERE pro_id = ?;";

    /**
     * Este es el metod constructor, para todas las operaciones
     * necesarias a realizar en la basde de datos para al tabla
     * producto_has_ubicacion
     * @throws IOException
     * @throws SQLException
     */
    public Base_phu() throws IOException, SQLException{

        super();
        
    }

    /**
     * Este metodo se encarga de actualizar la ubicacion
     * de un producto dentro de la base de datos
     * @param id_producto Deberia ser el id del producto
     * @param id_ubicacion Deberia ser el id de la ubicacion
     * @throws SQLException
     */
    public void actualizar(long id_producto, long id_ubicacion) throws SQLException{

        try{
            // Preparando la update a realizar
            pstate = conexion.prepareStatement(ACTUALIZAR);

            // Modificando por los datos pasados por el usuario
            pstate.setLong(1, id_ubicacion);
            pstate.setLong(2, id_producto);

            // Ejecutando el update
            pstate.executeUpdate();


        }catch(SQLException ex){    // Por si llegan a haber errores

            throw ex;

        }finally{   // Limpia los objetos utilizados

            pstate.close();
            
        }
    }


    /**
     * Este metodo se encarga de inserar en la base de datos 
     * un regsitro de la pertenencia de un producto en una
     * ubicacion dada
     * @param id_producto Corresponde a el id del producto
     * @param id_ubicacion Corresponde a el id de la ubicacion
     * @throws SQLException
     */
    public void insertar(long id_producto, long id_ubicacion) throws SQLException{
        
        try{
            
            // Prepara lo que se va a ejecutar en la base de datos
            pstate = conexion.prepareStatement(INSERTAR);

            // Modifica los valores que se van a insertar en el String predefinido
            pstate.setLong(1, id_producto);
            pstate.setLong(2, id_ubicacion);

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
     * Este metodo elimina un regsitro de la base de datos
     * un registro de la tabla producto_has_ubicacion
     * @param id Este deberia ser el id del producto
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
     * Este metodo se encarga de realizar una consulta a la
     * base de datos hacia la tabla producto_has_ubiacion
     * utilizando el parametro para realizar el filtrado
     * de los datos
     * @param parametro este deberia ser el parametro de busqueda
     * en este caso puede ser una parte del id del produdcto o
     * la ubicacion o una subcadena de los nombres del producto o
     * de la ubicacion.
     * @return Retorna una matriz con los encabezados de la tabala
     * y los registros encontrados, en caso de no encontrar
     * informacion, simplemente retorna los encabezados de la tabla
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
     * Este metodo se encarga de consultar un unico registro
     * de la tabla producto_has_ubicacion, utilizando como
     * parametro de busqueda la id del producto, puesto
     * que este es unico.
     * @param id Deberia ser el id del producto, el cual
     * solo esta de manera unica en una unica ubicacion 
     * @return Retorna un arreglo de tipo String[] con
     * los datos encontrados, en caso de no encontrar
     * datos, retorna un arreglo con valores null
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
