package Base_datos;

import java.io.IOException;
import java.sql.SQLException;

public class Base_phu extends Conexion{
    
    private static final String[] COLUMNAS = {"ID","PRODUCTO","ID UBI","UBICACION"};
    private static final String INSERTAR = "INSERT INTO COMPRA (pro_id, com_precio, com_cantidad) VALUES (?,(SELECT pro_precio_compra FROM PRODUCTO WHERE pro_id = ?),?);";
    private static final String ELIMINAR = "DELETE FROM COMPRA WHERE com_id = ?;";
    private static final String CONSULTAR_MUCHOS = "SELECT * FROM VW_COMPRA WHERE com_id = ? OR pro_id LIKE ? OR pro_nombre LIKE ? OR com_fecha LIKE ?;";
    private static final String CANTIDAD_MUCHOS = "SELECT COUNT(*) FROM VW_COMPRA WHERE com_id = ? OR pro_id LIKE ? OR pro_nombre LIKE ? OR com_fecha LIKE ?;";
    private static final String CONSULTAR_UNO = "SELECT * FROM VW_COMPRA WHERE com_id = ?;";

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
}
