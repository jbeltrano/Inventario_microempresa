package Base_datos;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Base_producto extends Conexion{
    
    private static final String[] COLUMNAS = {"ID","PRODUCTO","PRECIO COMPRA","PRECIO VENTA","DISPONIBLE", "UBICACION", "NOTA"};
    private static final String INSERTAR = "INSERT INTO PRODUCTO (pro_nombre, pro_precio_compra, pro_precio_venta, ubi_id, pro_nota) VALUES (?, ?, ?, ?, ?);";
    private static final String ELIMINAR = "DELETE FROM PRODUCTO WHERE pro_id = ?;";
    private static final String CONSULTAR_MUCHOS = "SELECT * FROM VW_PRODUCTO WHERE pro_id LIKE ? OR pro_nombre LIKE ?;";
    private static final String CANTIDAD_MUCHOS = "SELECT COUNT(*) FROM VW_PRODUCTO WHERE pro_id LIKE ? OR pro_nombre LIKE ?;";
    private static final String CONSULTAR_UNO = "SELECT * FROM VW_PRODUCTO WHERE pro_id = ?;";
    private static final String CONSULTAR_UBI = "SELECT ubi_id FROM PRODUCTO WHERE pro_id = ?;";
    private static final String ACTUALIZAR = "UPDATE PRODUCTO SET pro_precio_compra = ?, pro_precio_venta = ?, pro_nombre = ?, ubi_id = ?, pro_nota = ? WHERE pro_id = ?;";

    /**
     * Metodo contructor de la clase 
     * Base_producto
     * @throws IOException
     * @throws SQLException
     */
    public Base_producto() throws IOException, SQLException{

        super();

    }

    /**
     * Este metodo se utiliza para actualizar un registro
     * tipo producto en la base de datos.
     * @param id Deberia ser el id del productoa actualizar
     * @param precio_compra Si cambia deberia ser el
     * nuevo precio de compra del producto
     * @param precio_venta Si cambia deberia ser el
     * nuevo precio de venta del producto
     * @param nombre Si cambia, deberia ser el nuevo
     * nombre asignado al producto
     * @throws SQLException
     */
    public void actualizar( long id, 
                            long precio_compra, 
                            long precio_venta,
                            long ubicacion,
                            String nota, 
                            String nombre) 
                            throws SQLException{

        try{
            // Preparando la update a realizar
            pstate = conexion.prepareStatement(ACTUALIZAR);

            // Modificando por los datos pasados por el usuario
            pstate.setLong(1, precio_compra);
            pstate.setLong(2, precio_venta);
            pstate.setString(3, nombre);
            pstate.setLong(4, ubicacion);
            pstate.setString(5, nota);
            pstate.setLong(6, id);

            // Ejecutando el update
            pstate.executeUpdate();


        }catch(SQLException ex){    // Por si llegan a haber errores

            throw ex;

        }finally{   // Limpia los objetos utilizados

            pstate.close();
            
        }
    }


    /**
     * Este metodo se encarga de insertar un registro
     * de un producto en la base de datos
     * @param nombre deberia ser el nombre del producto
     * @param precio_compra deberia ser el precio de compra
     * del producto
     * @param precio_venta deberia ser el precio de venta
     * del producto
     * @param id_ubiacion debe ser el id de la ubicacion
     * en donde va a estar ubicado el producto
     * @param nota es una nota adiccional si se decea agregar
     * @throws SQLException
     */
    public void insertar(
            String nombre, 
            long precio_compra, 
            long precio_venta,
            long cantidad_inicial,
            long id_ubiacion,
            String nota
            ) throws SQLException{
                
        PreparedStatement stmtInventario = null;
        String sqlActualizarInventario = "UPDATE INVENTARIO SET inv_cantidad = ? WHERE pro_id = ?";
        
        try{
            
            conexion.setAutoCommit(false);
            // Prepara lo que se va a ejecutar en la base de datos
            pstate = conexion.prepareStatement(INSERTAR, Statement.RETURN_GENERATED_KEYS);
            

            // Modifica los valores que se van a insertar en el String predefinido
            pstate.setString(1, nombre);
            pstate.setLong(2, precio_compra);
            pstate.setLong(3, precio_venta);
            pstate.setLong(4, id_ubiacion);
            pstate.setString(5, nota);
            
            
            int filasAfectadas = pstate.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new SQLException("Error al insertar producto");
            }
            
            // 2. Obtener el ID del producto insertado
            ResultSet generatedKeys = pstate.getGeneratedKeys();
            long productoId = 0;
            if (generatedKeys.next()) {
                productoId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el ID del producto insertado");
            }
            
            // 3. Actualizar la cantidad en inventario
            stmtInventario = conexion.prepareStatement(sqlActualizarInventario);
            stmtInventario.setLong(1, cantidad_inicial);
            stmtInventario.setLong(2, productoId);
            
            int filasInventario = stmtInventario.executeUpdate();
            
            if (filasInventario == 0) {
                throw new SQLException("Error al actualizar la cantidad en inventario");
            }
            
            conexion.commit();
            
        } catch (SQLException e) {
            
            if (conexion != null) {
                conexion.rollback();
            }

            throw e;

        } finally {

            if (pstate != null) pstate.close();
            if (stmtInventario != null) stmtInventario.close();
            conexion.setAutoCommit(true);

        }
    }

    

    /**
     * Este metodo se encarga de eliminar un registro
     * en la tabla producto
     * @param id Deberia ser el id del
     * producto a eliminar
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
     * Este metodo se encarga de realizar una consulta
     * en la vista producto retornando una matriz
     * con los datos y las cabeceras de las tablas
     * @param parametro puede ser el id del producto o
     * el una parte del nombre del producto
     * @return Retorna una matriz de tipo String[][]
     * con los datos obtenidos y los titulos de la tabla
     * en caso de no encontrar nada, solo retorna los titulos
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

                    if(j == 2 || j == 3){

                        datos[i][j] = "" + Double.parseDouble(result.getString(j+1))/100;
                        
                    }else{

                        datos[i][j] = result.getString(j+1);

                    }

                    
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
     * Este metodo se encarga de retornar un 
     * unico registro identificado por el id
     * del producto.
     * @param id Debe ser el id del producto
     * a consultar
     * @return Retorna un arreglo de tipo
     * String[] con los datos encontrados,
     * en caso de no encontrarlo, retorna
     * el arreglo con datos null.
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

                    if(j == 2 || j == 3){

                        datos[j] = "" + Double.parseDouble(result.getString(j+1))/100;
                        
                    }else{

                        datos[j] = result.getString(j+1);

                    }
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

    public long consultar_ubi(long id)throws SQLException{

        long ubicacion = 0;

        try{

            pstate = conexion.prepareStatement(CONSULTAR_UBI);  // Prepara la consulta de un unico registro

            pstate.setLong(1, id);  // Modifica los datos necesarios para la consulta

            result = pstate.executeQuery(); // Ejecuta la consulta y lo almacena en result

            if(result.next()){  // Si hay datos ejecuta el for que se encarga de introducirlos en el arreglo

                ubicacion = Long.parseLong(result.getString(1));

            }

        }catch(SQLException ex){

            throw ex;

        }finally{

            pstate.close();
            result.close();

        }

        return ubicacion;

    }
}
