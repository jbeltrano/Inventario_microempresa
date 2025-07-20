package Grafics.Dialogos;

import Grafics.Utilidades.Generador_informe_ventas;
import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Generar_informe extends JDialog {
    private JTextField txtFechaInicial;
    private JTextField txtFechaFinal;
    private JButton btnGenerar;
    private JButton btnCancelar;
    private SimpleDateFormat formatoFecha;
    
    public Generar_informe(JFrame parent) {
        super(parent, "Generar Informe de Ventas", true);
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        
        // Panel principal con GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Fecha Inicial
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Fecha Inicial (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1;
        txtFechaInicial = new JTextField(10);
        panel.add(txtFechaInicial, gbc);
        
        // Fecha Final
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Fecha Final (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1;
        txtFechaFinal = new JTextField(10);
        panel.add(txtFechaFinal, gbc);
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        btnGenerar = new JButton("Generar Informe");
        btnCancelar = new JButton("Cancelar");
        
        btnGenerar.addActionListener(_ -> generarInforme());
        btnCancelar.addActionListener(_ -> dispose());
        
        panelBotones.add(btnGenerar);
        panelBotones.add(btnCancelar);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(panelBotones, gbc);
        
        getContentPane().add(panel);
        pack();
    }
    
    private void generarInforme() {
        try {
            // Validar formato de fechas
            Date fechaInicial = formatoFecha.parse(txtFechaInicial.getText());
            Date fechaFinal = formatoFecha.parse(txtFechaFinal.getText());
            
            // Validar que la fecha inicial no sea posterior a la fecha final
            if (fechaInicial.after(fechaFinal)) {
                JOptionPane.showMessageDialog(this,
                    "La fecha inicial no puede ser posterior a la fecha final",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Generar el informe
            Generador_informe_ventas.generarInformeVentas(
                txtFechaInicial.getText(),
                txtFechaFinal.getText()
            );
            
            dispose();
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this,
                "Formato de fecha inválido. Use YYYY-MM-DD",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
