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
import Base_datos.Base_compra;
import Base_datos.Base_inventario;
import Base_datos.Base_producto;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        Base_producto base = null;

        try{
            
            base = new Base_producto();

            String[][] datos = base.consultar("asdf");

            for(int i = 0; i < datos.length; i++){
                for(int j = 0; j < datos[0].length; j++){
                    System.out.print(datos[i][j] + " | ");
                }
                System.out.println();
            }
            
            System.out.println();

            String[] dato = base.consultar_uno(1);

            for(int j = 0; j < dato.length; j++){
                    System.out.print(dato[j] + " | ");
            }

        }catch(SQLException | IOException ex){
            System.out.println(ex);
        }finally{

            base.close();
        }
        
    }
    
}
