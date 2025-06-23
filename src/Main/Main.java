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

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Base_compra compra = null;

        try{
            compra = new Base_compra();
            compra.insertar(2, 3);
            String[][] valores = compra.consultar("");

            for(int i = 0; i < valores.length; i++){
                for(int j = 0; j < valores[0].length; j++){
                    System.out.print(valores[i][j] + " | ");
                }
                System.out.println();
            }
        }catch(SQLException|IOException ex){
            System.out.println(ex);
        }finally{
            if(compra != null)
                compra.close();
            
        }
        
    }
    
}
