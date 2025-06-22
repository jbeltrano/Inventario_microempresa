/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Juan Beltran
 * @author Diego Tique
 */

import java.io.IOException;
import java.sql.SQLException;

import Base_datos.Conexion;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Conexion conexion = null;

        try{
            conexion = new Conexion();
        }catch(SQLException|IOException ex){
            System.out.println(ex);
        }finally{
            if(conexion != null){
                conexion.close();
            }
        }
        
    }
    
}
