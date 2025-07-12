package Grafics.Dialogos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Base_datos.Base_ubicacion;
import Grafics.Utilidades.Generic_callback;

public class Adicionar_ubicacion extends JDialog{
    
    private Generic_callback callback;
    protected JTextField text_nombre;
    private JLabel label_nombre;
    protected JButton boton_guardar;
    private JPanel panel;
    private String mensaje;

    public Adicionar_ubicacion(JFrame padre, Generic_callback callback){
        super(padre);
        this.callback = callback;
        setTitle("Formulario Ubicacion");
        setLayout(new BorderLayout());

        inicializarComponentes();
        configurarVentana();

    }

    private void inicializarComponentes() {
        
        // Panel principal
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        
        // Campo de búsqueda de producto
        gbc.gridx = 0; gbc.gridy = 0;
        label_nombre = new JLabel("Buscar Producto:");
        label_nombre.setOpaque(true);
        label_nombre.setBackground(Color.LIGHT_GRAY);
        label_nombre.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        panel.add(label_nombre, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        text_nombre = new JTextField(20);
        text_nombre.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(text_nombre, gbc);
        
        
        
        // Botón para agregar ubicacion
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        boton_guardar = new JButton("Agregar ubicacion");
        boton_guardar.setFont(new Font("Arial", Font.BOLD, 12));
        boton_guardar.setBackground(Color.LIGHT_GRAY);
        boton_guardar.addActionListener(_ -> procesar_ubicacion());
        panel.add(boton_guardar, gbc);
        
        
        add(panel, BorderLayout.CENTER);
        
    }
    
    
    protected void procesar_ubicacion() {
        
        String cantidadTexto = text_nombre.getText().trim();
        if (cantidadTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, ingresa la Ubicacion.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            
            guardar();

            mensaje = personalizar_mensjae();
            
            JOptionPane.showMessageDialog(this, mensaje, 
                "ubicacion Registrada", JOptionPane.INFORMATION_MESSAGE);
            
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
    
    protected String personalizar_mensjae(){

        return String.format(
                "Ubicacion registrada exitosamente:\n\n" +
                "Ubicacion: %s\n",
                text_nombre.getText()
            );

    }

    protected void guardar(){
        Base_ubicacion ubicacion = null;

            try{
                ubicacion = new Base_ubicacion();
                ubicacion.insertar(text_nombre.getText());

            }catch(SQLException | IOException ex){
                JOptionPane.showMessageDialog(Adicionar_ubicacion.this, ex, getTitle(), JOptionPane.ERROR_MESSAGE);
                
            }finally{
                if(ubicacion != null){
                    ubicacion.close();
                }
            }
            
    }
    
    private void limpiarFormulario() {
        text_nombre.setText("");
    }
    
    private void configurarVentana() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 150);
        setLocationRelativeTo(null);
        setResizable(false);
    }

}
