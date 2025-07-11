package Grafics.Dialogos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import Grafics.Utilidades.Producto_callback;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import Base_datos.Base_producto;
import Base_datos.Base_ubicacion;

public class Adicinar_producto extends JDialog{
    

    protected JTextField text_nombre;
    protected JTextField text_cantidad;
    protected JTextField text_valor_c;
    protected JTextField text_valor_v;
    private JLabel nombreLabel;
    private JLabel cantidadLabel;
    private JLabel valor_c_label;
    private JLabel valor_v_label;
    private JLabel ubicacionLabel;
    private JLabel notasLabel;
    protected JComboBox<String> ubicacionComboBox;
    protected JTextArea area_text_notas;
    private JButton addButton;
    private Producto_callback callback;
    private JPanel panel;
    private JPanel buttonPanel;
    private JScrollPane scrollPane;
    protected JLabel titleLabel;


    public Adicinar_producto(JFrame padre, Producto_callback callback){

        super(padre);
        this.callback = callback;
        initComponents();
        setupLayout();
        setupEventListeners();

    }

    public void setupLayout(){
        setLayout(new BorderLayout());
        
        // Panel principal con título
        panel = new JPanel(new BorderLayout());
        
        // Título
        titleLabel = new JLabel("AÑADIR NUEVO PRODUCTO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        
        // Nombre
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        nombreLabel = new JLabel("* Nombre:");
        nombreLabel.setOpaque(true);
        nombreLabel.setBackground(Color.LIGHT_GRAY);
        nombreLabel.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        formPanel.add(nombreLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(text_nombre, gbc);
        
        // Cantidad
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        cantidadLabel = new JLabel("* Cantidad:");
        cantidadLabel.setOpaque(true);
        cantidadLabel.setBackground(Color.LIGHT_GRAY);
        cantidadLabel.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        formPanel.add(cantidadLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(text_cantidad, gbc);
        
        // Valor-unidad
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        valor_c_label = new JLabel("* Valor compra unidad");
        valor_c_label.setOpaque(true);
        valor_c_label.setBackground(Color.LIGHT_GRAY);
        valor_c_label.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        formPanel.add(valor_c_label, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(text_valor_c, gbc);

        // Valor_venta
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        valor_v_label = new JLabel("* Valor venta unidad");
        valor_v_label.setOpaque(true);
        valor_v_label.setBackground(Color.LIGHT_GRAY);
        valor_v_label.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        formPanel.add(valor_v_label, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(text_valor_v, gbc);
        
        // Ubicación
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        ubicacionLabel = new JLabel("Ubicación");
        ubicacionLabel.setOpaque(true);
        ubicacionLabel.setBackground(Color.LIGHT_GRAY);
        ubicacionLabel.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        formPanel.add(ubicacionLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(ubicacionComboBox, gbc);
        
        // Notas
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        notasLabel = new JLabel("Notas");
        notasLabel.setOpaque(true);
        notasLabel.setBackground(Color.LIGHT_GRAY);
        notasLabel.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        notasLabel.setVerticalAlignment(SwingConstants.TOP);
        formPanel.add(notasLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        scrollPane = new JScrollPane(area_text_notas);
        scrollPane.setPreferredSize(new Dimension(300, 100));
        formPanel.add(scrollPane, gbc);
        
        // Panel para el botón
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        
        // Agregar componentes al panel principal
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(panel);
    }

    private void initComponents() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        // Crear campos de texto
        text_nombre = new JTextField(20);
        text_cantidad = new JTextField(20);
        text_valor_c = new JTextField(20);
        text_valor_v = new JTextField(20);
        
        // Aplicar filtros a los campos numéricos
        aplicarFiltroEnteros(text_cantidad); // Solo enteros
        aplicarFiltroDecimales(text_valor_c); // Decimales con 2 dígitos
        aplicarFiltroDecimales(text_valor_v);  // Decimales con 2 dígitos
        
        // Crear combo box para ubicación
        ubicacionComboBox = new JComboBox<>(cargar_combo());
        
        // Crear área de texto para notas
        area_text_notas = new JTextArea(5, 20);
        area_text_notas.setLineWrap(true);
        area_text_notas.setWrapStyleWord(true);
        
        // Crear botón con ícono de "+"
        addButton = new JButton("+");
        addButton.setFont(new Font("Arial", Font.BOLD, 20));
        addButton.setPreferredSize(new Dimension(50, 50));
        addButton.setBackground(Color.DARK_GRAY);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    
    private boolean verificar(){

        if(text_nombre.getText().equals("")){
            return false;
        }
        if(text_cantidad.getText().equals("")){
            return false;
        }
        if(text_valor_c.getText().equals("")){
            return false;
        }

        return true;
    }

    private void setupEventListeners() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí puedes agregar la lógica para añadir/editar el producto
                if(verificar()){

                    String nombre = text_nombre.getText();
                    String nota = area_text_notas.getText();
                    long cantidad = Long.parseLong(text_cantidad.getText());
                    long valor_compra = (long) (Double.parseDouble(text_valor_c.getText())*100);
                    long valor_venta = (long) (Double.parseDouble(text_valor_v.getText())*100);
                    long id_ubicacion = Long.parseLong(ubicacionComboBox.getSelectedItem().toString().split("|")[0]);

                    guardar(nombre, cantidad, valor_compra, valor_venta, id_ubicacion, nota);

                    String mensaje = "Producto procesado:\n" +
                                "Nombre: " + text_nombre.getText() + "\n" +
                                "Cantidad: " + text_cantidad.getText() + "\n" +
                                "Valor-unidad: " + text_valor_c.getText() + "\n" +
                                "Ubicación: " + ubicacionComboBox.getSelectedItem() + "\n" +
                                "Notas: " + area_text_notas.getText();
                    
                    JOptionPane.showMessageDialog(Adicinar_producto.this, mensaje, "Producto Procesado", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Limpiar campos después de procesar
                    limpiarCampos();

                    if (callback != null){
                        callback.producto_guardado();
                    }
                }else{

                    JOptionPane.showMessageDialog(Adicinar_producto.this, "Los campos con * son obligatorios", "Producto Procesado", JOptionPane.INFORMATION_MESSAGE);

                }
                
            }
        });
    }
    

    protected void guardar(
            String nombre,
            long cantidad,
            long valor_compra,
            long valor_venta,
            long id_ubicacion,
            String nota
            ){

        Base_producto producto = null;

        try{
            
            producto = new Base_producto();
            
            producto.insertar(
                nombre,
                valor_compra,
                valor_venta,
                cantidad,
                id_ubicacion,
                nota
            );

        }catch(SQLException | IOException ex){

            JOptionPane.showMessageDialog(Adicinar_producto.this, ex, "Error", JOptionPane.ERROR_MESSAGE);

        }finally{

            if(producto != null){
                producto.close();
            }
        }
    }


    private void aplicarFiltroEnteros(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) { // Solo dígitos
                    super.insertString(fb, offset, string, attr);
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) { // Solo dígitos
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }
    
    private void aplicarFiltroDecimales(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + string + currentText.substring(offset);
                
                if (esDecimalValido(newText)) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                
                if (esDecimalValido(newText)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
            
            private boolean esDecimalValido(String text) {
                if (text.isEmpty()) return true;
                
                // Permitir solo un punto decimal
                if (text.matches("^\\d*\\.?\\d{0,2}$")) {
                    return true;
                }
                return false;
            }
        });
    }

    
    private void limpiarCampos() {
        text_nombre.setText("");
        text_cantidad.setText("");
        text_valor_c.setText("");
        text_valor_v.setText("");
        ubicacionComboBox.setSelectedIndex(0);
        area_text_notas.setText("");
    }

    private String[] cargar_combo(){
        
        Base_ubicacion ubicacion = null;
        String datos[][];
        String datos_convertidos[] = null;

        try{

            ubicacion = new Base_ubicacion();
            
            datos = ubicacion.consultar("");
            datos_convertidos = new String[datos.length-1];

            for(int i = 1; i < datos.length; i++){

                datos_convertidos[i-1] = datos[i][0] + "| " + datos[i][1];

            }

        }catch(SQLException | IOException ex){

            JOptionPane.showMessageDialog(this, ex, getTitle(), JOptionPane.ERROR_MESSAGE);

        }finally{
            if(ubicacion != null){
                ubicacion.close();
            }
        }
        
        return datos_convertidos;

    }

}