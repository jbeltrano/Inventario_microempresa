package Base_datos;

import java.io.IOException;
import java.sql.SQLException;

public class Base_ubicacion extends Conexion{
    
    private static final String[] COLUMNAS = {"ID","UBICACION"};
    private static final String INSERTAR = "INSERT INTO UBICACION (ubi_nombre) VALUES (?);";
    private static final String ELIMINAR = "DELETE FROM UBICACION WHERE ubi_id = ?;";
    private static final String CONSULTAR_MUCHOS = "SELECT * FROM UBICACION WHERE ubi_id LIKE ? OR ubi_nombre LIKE ?;";
    private static final String CANTIDAD_MUCHOS = "SELECT COUNT(*) FROM UBICACION WHERE ubi_id LIKE ? OR ubi_nombre LIKE ?;";
    private static final String CONSULTAR_UNO = "SELECT * FROM UBICACION WHERE ubi_id = ?;";
    private static final String ACTUALIZAR = "UPDATE UBICACION SET ubi_nombre = ? WHERE ubi_id = ?;";


    /**
     * Metodo constructor de la clase Base_ubicacion
     * establece la conexion con la base e datos
     * @throws IOException
     * @throws SQLException
     */
    public Base_ubicacion() throws IOException, SQLException{

        super();

    }

    /**
     * Este metodo inserta en la tabla ubicacion
     * el el nombre de una ubicacion dentro del almacen
     * @param ubicacion este es el nombre de alguna ubicacion
     * en el sistema de organizacion dentro de la empresa a utilizar
     * @throws SQLException
     */
    public void insertar(String ubicacion) throws SQLException{
        
        try{
            
            // Prepara lo que se va a ejecutar en la base de datos
            pstate = conexion.prepareStatement(INSERTAR);

            // Modifica los valores que se van a insertar en el String predefinido
            pstate.setString(1, ubicacion);

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
     * Este metodo se encarga de eliminar un regsitro
     * de la tabla ubicacion, utilizando el id que la identifica
     * @param id este es el id de la ubicacion
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
     * Realiza una consulta en la base de datos 
     * utilizando el parametro dado, retornando
     * una matriz con los datos y la cabecera de
     * la tabla
     * @param parametro Puede ser el id o una
     * fraccion del nombre a consultar dentro
     * de la base de datos
     * @return retorna una matriz tipo String[][]
     * con la cabecera y en caso de encontrar resultados
     * retorna mas filas con datos, caso contrario
     * simplemente retorna la cabecera de la tabla
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
            pstate.setString(2, parametro_aux);
            
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
        
            // Se prepara la consulta real
            pstate = conexion.prepareStatement(CONSULTAR_MUCHOS);

            // Se modifican los parametros a utilizar en la consulta
            pstate.setString(1, id);
            pstate.setString(2, parametro_aux);
            
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
     * Retorna un unico registro de la tabla ubicacion
     * utilizando como identificador unico el id la ubicacion
     * @param id Deberia ser el id excacto de la ubiacion a consultar
     * @return Retorna un arreglo tipo String[] con los datos encontrados
     * en caso de no encontrar nada, retorna null
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

    /**
     * Este metodo se utiliza para actualizar un registro
     * en la base de datos, actualiando unicamente el nombre
     * y utilizando el id, para identificar a cual registro actualizar
     * @param id Deberia ser el id exacto de la ubiacion
     * @param nombre Es el nuevo nombre a utilizar
     * @throws SQLException
     */
    public void actualizar(long id, String nombre) throws SQLException{

        try{
            // Preparando la update a realizar
            pstate = conexion.prepareStatement(ACTUALIZAR);

            // Modificando por los datos pasados por el usuario
            pstate.setString(1, nombre);
            pstate.setLong(2, id);

            // Ejecutando el update
            pstate.executeUpdate();


        }catch(SQLException ex){    // Por si llegan a haber errores

            throw ex;

        }finally{   // Limpia los objetos utilizados

            pstate.close();
            
        }
    }
}
