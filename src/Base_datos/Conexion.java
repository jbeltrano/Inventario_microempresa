package Base_datos;

import java.sql.Statement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Conexion {
    

    private static String url = "jdbc:sqlite:";    // String util para la conexion
    private static boolean band = true;     // variable para manejar la excepcion
    protected Connection conexion;          // Variable para establecer la conexion con la base de datos
    protected String insert;                // Variable que se utiliza para realizar inserts
    protected String update;                // Variable que se utiliza para realizar updates
    protected String delete;                // VAriable que se uitliza para realizar deletes
    protected String query;                 // VAriable que se utilzia para realizar consultas
    protected Statement state;              // Variable que se utiliza para operaciones sin variables
    protected ResultSet resutl;             // Variable para almacenar los resultados
    protected PreparedStatement pstate;     // Variable que se utiliza para operaciones con variables


    static {    //Esta funcion solo se ejecuta una vez, y se hace cuando se carga la clase en memoria
        try{
            url = url.concat(Files.readString(Paths.get("src\\Archivos\\Direccion.txt")));
        }catch(IOException ex){
            band = false;
        }
            
    }

    /**
     * Metodo constructor por defecto
     * @throws IOException
     * @throws SQLException
     */
    public Conexion()throws IOException,SQLException{

        

        if(!band){      // Se encarga de revisar la bandera, en caso de ser negativo retorna un error
            throw new IOException("No es posible encontrar el archivo: Direccion.txt");
        }
        
        try{        // Este segmento de codigo es el que se encarga de establecer la conexion con la base de datos
            conexion = DriverManager.getConnection(url);
            state = conexion.createStatement();
            state.execute("PRAGMA foreign_keys = ON");

        }catch(SQLException ex){

            throw new SQLException("No es posible establecer conexion con la base de datos");

        }finally{

            state.close();
        }
        
    }


    /**
     * Este metodo se encarga de cerrar la conexcion
     * con la base de datos
     * @throws SQLException
     */
    public void close(){
        try{
            conexion.close();
        }catch(SQLException ex){
            System.out.println("No es posible cerrar la conexion con la base de datos");
        }
    }


}
