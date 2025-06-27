package Base_datos;

import java.io.IOException;
import java.sql.SQLException;

public class Base_inventario extends Conexion{
    

    private static final String[] COLUMNAS = {"ID P","PRODUCTO","CANTIDAD"};
    private static final String CONSULTAR_MUCHOS = "SELECT * FROM VW_INVENTARIO WHERE pro_id = ? OR pro_nombre LIKE ?;";
    private static final String CANTIDAD_MUCHOS = "SELECT COUNT(*) FROM VW_INVENTARIO WHERE pro_id = ? OR pro_nombre LIKE ?;";
    private static final String CONSULTAR_UNO = "SELECT * FROM VW_COMPRA WHERE pro_id = ?;";


    /**
     * Metodo constructor de la clase
     * para el manejo de la tabla
     * Base_inventario
     * @throws IOException
     * @throws SQLException
     */
    public Base_inventario() throws IOException, SQLException{

        super();

    }


    /**
     * Este metodo se utiliza para consultar varios registros
     * que se encuentren en la tabla inventario,
     * @param parametro este parametro puede ser el id
     * del producto, o puede ser el nombre del mismo
     * @return retorna una matriz tipo String[][]
     * con la cabecera de la tabla en la primera fila
     * de la matriz
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


            // Se prepara la consulta
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
     * Este metodo se encarga de consultar un unico
     * registro de la tabla de datos, almacenados
     * en la tabla inventario
     * @param id deberia ser el id del producto
     * @return retorna un String[] con los datos
     * encontrados en la base de datos
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
