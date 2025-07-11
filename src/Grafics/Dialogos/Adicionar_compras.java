package Grafics.Dialogos;

import javax.swing.*;

import Base_datos.Base_compra;
import Base_datos.Base_producto;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Grafics.Utilidades.Generic_callback;
import Grafics.Utilidades.Key_adapter;
import static Grafics.Utilidades.Filtros_text.aplicarFiltroEnteros;

public class Adicionar_compras extends JDialog {
    
    // Clase para representar un producto
    static class Producto {
        private long id;
        private String nombre;
        private double precio;
        
        public Producto(long id, String nombre, double precio) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
        }
        
        public long getId() { return id; }
        public String getNombre() { return nombre; }
        public double getPrecio() { return precio; }
        
        @Override
        public String toString() {
            return nombre + " (ID: " + id + ") - $" + precio;
        }

        public static List<Producto> parseProducto(String[][] datos){
            List<Producto> producto = new ArrayList<>();
            if(datos != null && datos.length > 1) {
                for(int i = 1; i < datos.length; i++){
                    if(datos[i].length >= 2) {
                        producto.add(new Producto(Long.parseLong(datos[i][0]), datos[i][1], Double.parseDouble(datos[i][4])));
                    }
                    
                }
            }
            return producto;
        }
    }
    
    // Componentes del formulario
    private JTextField text_buscar;
    private JTextField text_cantidad;
    private JLabel label_producto;
    private JLabel label_buscar_producto;
    private JLabel label_cantidad;
    private JButton boton_guardar;
    private JPopupMenu popupSugerencias;
    private List<Producto> productos;
    private Producto productoSeleccionado;
    private JPanel panelPrincipal;
    private Generic_callback callback;
    
    public Adicionar_compras(JFrame padre, Generic_callback callback) {
        super(padre);
        this.callback = callback;
        setLayout(new BorderLayout());
        inicializarComponentes();
        configurarVentana();
        // No llamar actualizarSugerencias() aquí - se llamará cuando escribas
    }
    
    private void inicializarComponentes() {
        
        // Panel principal
        panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        
        // Campo de búsqueda de producto
        gbc.gridx = 0; gbc.gridy = 0;
        label_buscar_producto = new JLabel("Buscar Producto:");
        label_buscar_producto.setOpaque(true);
        label_buscar_producto.setBackground(Color.LIGHT_GRAY);
        label_buscar_producto.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        panelPrincipal.add(label_buscar_producto, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        text_buscar = new JTextField(20);
        text_buscar.setFont(new Font("Arial", Font.PLAIN, 12));
        panelPrincipal.add(text_buscar, gbc);
        
        // Label para mostrar producto seleccionado
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        label_producto = new JLabel("Ningún producto seleccionado");
        label_producto.setFont(new Font("Arial", Font.ITALIC, 11));
        label_producto.setForeground(Color.GRAY);
        panelPrincipal.add(label_producto, gbc);
        
        // Campo de cantidad
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        label_cantidad = new JLabel("Cantidad:");
        label_cantidad.setOpaque(true);
        label_cantidad.setBackground(Color.LIGHT_GRAY);
        label_cantidad.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        panelPrincipal.add(label_cantidad, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        text_cantidad = new JTextField(10);
        text_cantidad.setFont(new Font("Arial", Font.PLAIN, 12));
        aplicarFiltroEnteros(text_cantidad);
        panelPrincipal.add(text_cantidad, gbc);
        
        // Botón para agregar compra
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        boton_guardar = new JButton("Agregar Compra");
        boton_guardar.setFont(new Font("Arial", Font.BOLD, 12));
        boton_guardar.setBackground(Color.LIGHT_GRAY);
        boton_guardar.addActionListener(e -> procesarCompra());
        panelPrincipal.add(boton_guardar, gbc);
        
        
        add(panelPrincipal, BorderLayout.CENTER);
        
        // Configurar autocompletado
        configurarAutocompletado();
    }
    
    private void configurarAutocompletado() {
        popupSugerencias = new JPopupMenu();
        
        // Usar KeyAdapter personalizado para mejor control
        text_buscar.addKeyListener(new Key_adapter() {
            @Override
            public void accion() {
                // Usar SwingUtilities.invokeLater para que se ejecute después 
                // de que el texto se haya actualizado completamente
                
                actualizarSugerencias();
                text_buscar.requestFocus();
                //text_buscar.requestFocus();
            }
            
            @Override
            public void accion2() {
                // Acción para Enter - seleccionar primera sugerencia si hay
                seleccionarPrimeraSugerencia();
            }
        });
    }
    
    private void actualizarSugerencias() {
        Base_producto base_producto = null;
        try {
            String texto = text_buscar.getText();
            // Limpiar sugerencias anteriores
            popupSugerencias.setVisible(false);
            popupSugerencias.removeAll();
            
            if (texto.isEmpty()) {
                popupSugerencias.setVisible(false);
                return;
            }
            
            // Consultar base de datos
            base_producto = new Base_producto();
            String datos[][] = base_producto.consultar(texto);
            productos = Producto.parseProducto(datos);
            
            // Verificar si hay productos
            if (productos == null || productos.isEmpty()) {
                popupSugerencias.setVisible(false);
                return;
            }
            
            
            
            // Mostrar sugerencias
            if (productos.isEmpty()) {
                popupSugerencias.setVisible(false);
            } else {
                // Limitar a 5 sugerencias máximo
                int maxSugerencias = Math.min(10, productos.size());
                
                for (int i = 0; i < maxSugerencias; i++) {
                    Producto producto = productos.get(i);
                    JMenuItem item = new JMenuItem(producto.toString());
                    item.addActionListener(e -> seleccionarProducto(producto));
                    
                    // Agregar efecto hover
                    item.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            item.setBackground(new Color(230, 240, 250));
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            item.setBackground(UIManager.getColor("MenuItem.background"));
                        }
                    });
                    
                    popupSugerencias.add(item);
                }
                
                // Posicionar y mostrar el popup
                try {
                    
                    popupSugerencias.show(text_buscar, 0 , text_buscar.getHeight());
                    // popupSugerencias.setLocation(this.getX() + text_buscar.getX() + 8, this.getY() + text_buscar.getY() + text_buscar.getHeight() +35);
                    // popupSugerencias.setVisible(true);
                    
                } catch (Exception e) {
                    // Si falla mostrar el popup, no hacer nada
                    System.out.println("Error mostrando popup: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            // Manejo de errores
            System.out.println("Error en actualizarSugerencias: " + e.getMessage());
            e.printStackTrace();
            popupSugerencias.setVisible(false);
        } finally {
            if (base_producto != null) {
                base_producto.close();
            }
        }
    }

    private void seleccionarPrimeraSugerencia() {
        if (popupSugerencias.getComponentCount() > 0) {
            JMenuItem primerItem = (JMenuItem) popupSugerencias.getComponent(0);
            primerItem.doClick();
        }
    }
    
    private void seleccionarProducto(Producto producto) {
        this.productoSeleccionado = producto;
        text_buscar.setText(producto.getNombre());
        label_producto.setText("Seleccionado: " + producto.toString());
        label_producto.setForeground(new Color(0, 120, 0));
        popupSugerencias.setVisible(false);
        
        // Enfocar el campo de cantidad
        text_cantidad.requestFocus();
    }
    
    private void procesarCompra() {
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, selecciona un producto válido de la lista de sugerencias.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String cantidadTexto = text_cantidad.getText().trim();
        if (cantidadTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, ingresa la cantidad.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int cantidad = Integer.parseInt(cantidadTexto);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "La cantidad debe ser un número positivo.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Aquí llamarías a tu método para insertar en la BD
            insertarCompraEnBD(productoSeleccionado.getId(), cantidad);
            
            // Mostrar confirmación
            double total = productoSeleccionado.getPrecio() * cantidad;
            String mensaje = String.format(
                "Compra registrada exitosamente:\n\n" +
                "Producto: %s\n" +
                "ID: %d\n" +
                "Cantidad: %d\n" +
                "Precio unitario: $%.2f\n" +
                "Total: $%.2f",
                productoSeleccionado.getNombre(),
                productoSeleccionado.getId(),
                cantidad,
                productoSeleccionado.getPrecio(),
                total
            );
            
            JOptionPane.showMessageDialog(this, mensaje, 
                "Compra Registrada", JOptionPane.INFORMATION_MESSAGE);
            
            // Limpiar formulario
            limpiarFormulario();
            
            if(callback != null){
                callback.callback();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "La cantidad debe ser un número válido.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void insertarCompraEnBD(long idProducto, long cantidad) {
        // Aquí implementarías la inserción en tu base de datos
        
        Base_compra compra = null;

        try{
            compra = new Base_compra();
            compra.insertar(idProducto, cantidad);

        }catch(SQLException | IOException ex){
            JOptionPane.showMessageDialog(Adicionar_compras.this, ex, getTitle(), JOptionPane.ERROR_MESSAGE);
        }finally{
            compra.close();
        }
        // Ejemplo de lo que harías:
        /*
        try {
            String sql = "INSERT INTO compras (id_producto, cantidad, fecha) VALUES (?, ?, NOW())";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, idProducto);
            stmt.setLong(2, cantidad);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
    }
    
    private void limpiarFormulario() {
        text_buscar.setText("");
        text_cantidad.setText("");
        productoSeleccionado = null;
        label_producto.setText("Ningún producto seleccionado");
        label_producto.setForeground(Color.GRAY);
        popupSugerencias.setVisible(false);
    }
    
    private void configurarVentana() {
        setTitle("Formulario de Compra");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}