package Grafics.Utilidades;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.Color;
import java.awt.event.MouseAdapter;

public class Modelo_tabla {
    

    public static DefaultTableModel set_modelo_tablas(String [][] datos){
        DefaultTableModel modelo;
        
        modelo = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hacer que todas las celdas no sean editables
                return false;
            }
        };

        for(int i = 0; i < datos[0].length; i++){
            modelo.addColumn(datos[0][i]);
        }
    
        for(int i = 1; i < datos.length; i++){
                
            modelo.addRow(datos[i]);
    
        }

        return modelo;
    }
    
    
    public static void add_mouse_listener(JTable tabla){

        tabla.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Obtener la fila seleccionada
                    int filaSeleccionada = tabla.rowAtPoint(e.getPoint());
                    int columna = tabla.columnAtPoint(e.getPoint());

                    // Seleccionar la fila
                    tabla.setRowSelectionInterval(filaSeleccionada, filaSeleccionada);
                    tabla.setColumnSelectionInterval(columna,columna);
                    
                }
                
            }
        });
    }

    //tab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    public static JTable set_tabla_inventario(String[][] datos){ 
        JTable tab;                     // Variable para la tabla
        DefaultTableModel modelo;       // Variable para el modelo de la tabla
        TableColumnModel clum_model;    // Variable para el modelo de la comlumna de la tabla
        
        modelo = set_modelo_tablas(datos);  // Genera el modelo por defecto para nuestra tabla

        tab = new JTable(modelo);    // Se crea una nueva instancia de un JTable con el modelo

        tab.setShowGrid(true);      // Se encarga de mostrar las lineas de la tabla
        tab.setGridColor(Color.lightGray);  // Se en caega de modificar el color de las lineas de la tabla
        tab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  // Hace que la tabla no se renderice al tamaño por defecto
        tab.getTableHeader().setReorderingAllowed(false);   // Evita que se puedan reordenar las columnas
        tab.setCellSelectionEnabled(true);  // Hace que se puedan seleccionar las celdas
        add_mouse_listener(tab);    // Hace que solo se pueda seleccionar con el mouse derecho una unica celda
        
        
        // Configuarcion del tamaño de las columnas, para que tengan un tamaño por defecto
        clum_model = tab.getColumnModel();
        clum_model.getColumn(0).setPreferredWidth(40);
        clum_model.getColumn(1).setPreferredWidth(250);
        clum_model.getColumn(2).setPreferredWidth(100);
        clum_model.getColumn(3).setPreferredWidth(100);
        clum_model.getColumn(4).setPreferredWidth(80);
        clum_model.getColumn(5).setPreferredWidth(200);
        clum_model.getColumn(6).setPreferredWidth(350);
        

        return tab;

    }


    public static JTable set_tabla_compras(String[][] datos){ 
        JTable tab;                     // Variable para la tabla
        DefaultTableModel modelo;       // Variable para el modelo de la tabla
        TableColumnModel clum_model;    // Variable para el modelo de la comlumna de la tabla
        
        modelo = set_modelo_tablas(datos);  // Genera el modelo por defecto para nuestra tabla

        tab = new JTable(modelo);    // Se crea una nueva instancia de un JTable con el modelo

        tab.setShowGrid(true);      // Se encarga de mostrar las lineas de la tabla
        tab.setGridColor(Color.lightGray);  // Se en caega de modificar el color de las lineas de la tabla
        tab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  // Hace que la tabla no se renderice al tamaño por defecto
        tab.getTableHeader().setReorderingAllowed(false);   // Evita que se puedan reordenar las columnas
        tab.setCellSelectionEnabled(true);  // Hace que se puedan seleccionar las celdas
        add_mouse_listener(tab);    // Hace que solo se pueda seleccionar con el mouse derecho una unica celda
        
        
        // Configuarcion del tamaño de las columnas, para que tengan un tamaño por defecto
        clum_model = tab.getColumnModel();
        clum_model.getColumn(0).setPreferredWidth(40);
        clum_model.getColumn(1).setPreferredWidth(80);
        clum_model.getColumn(2).setPreferredWidth(250);
        clum_model.getColumn(3).setPreferredWidth(150);
        clum_model.getColumn(4).setPreferredWidth(150);
        clum_model.getColumn(5).setPreferredWidth(100);
        

        return tab;

    }
}
