package Grafics.Utilidades;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class Filtros_text {
    

    public static void aplicarFiltroEnteros(JTextField textField) {
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
    
    public static void aplicarFiltroDecimales(JTextField textField) {
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


}
