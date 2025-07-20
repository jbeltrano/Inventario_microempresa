package Grafics.Utilidades;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import Base_datos.Base_venta;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class Generador_informe_ventas {
    
    private static final String DESKTOP_PATH = System.getProperty("user.home") + "\\Desktop\\";
    private static final Font TITULO = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font SUBTITULO = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font TEXTO_NORMAL = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font TEXTO_NEGRITA = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    
    public static void generarInformeVentas(String fechaInicial, String fechaFinal) {
        try {
            // Crear el documento
            Document documento = new Document(PageSize.A4);
            String nombreArchivo = "Informe_Ventas_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(DESKTOP_PATH + nombreArchivo));
            documento.open();
            
            // Agregar encabezado
            agregarEncabezado(documento, fechaInicial, fechaFinal);
            
            // Agregar tabla de ventas
            agregarTablaVentas(documento, fechaInicial, fechaFinal);
            
            documento.close();
            JOptionPane.showMessageDialog(null, 
                "Informe generado exitosamente.\nGuardado en: " + DESKTOP_PATH + nombreArchivo, 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al generar el informe: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void agregarEncabezado(Document documento, String fechaInicial, String fechaFinal) throws DocumentException {
        Paragraph titulo = new Paragraph("Informe de Ventas", TITULO);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        
        documento.add(Chunk.NEWLINE);
        
        Paragraph periodo = new Paragraph(
            "Período: " + fechaInicial + " - " + fechaFinal, 
            SUBTITULO
        );
        periodo.setAlignment(Element.ALIGN_CENTER);
        documento.add(periodo);
        
        documento.add(Chunk.NEWLINE);
    }
    
    private static void agregarTablaVentas(Document documento, String fechaInicial, String fechaFinal) 
            throws DocumentException, SQLException, IOException {
        PdfPTable tabla = new PdfPTable(6); // 6 columnas
        tabla.setWidthPercentage(100);
        float[] anchos = {0.5f, 0.5f, 2f, 1f, 1f, 0.7f};
        tabla.setWidths(anchos);
        
        // Agregar encabezados
        agregarCeldaEncabezado(tabla, "ID");
        agregarCeldaEncabezado(tabla, "ID P");
        agregarCeldaEncabezado(tabla, "Producto");
        agregarCeldaEncabezado(tabla, "Fecha");
        agregarCeldaEncabezado(tabla, "Precio");
        agregarCeldaEncabezado(tabla, "Cantidad");
        
        // Obtener y agregar datos
        Base_venta baseVenta = new Base_venta();
        Object[][] datos = baseVenta.consultarPorFechas(fechaInicial, fechaFinal);
        
        double total = 0;
        
        for (Object[] fila : datos) {
            for (Object valor : fila) {
                agregarCelda(tabla, valor.toString());
            }
            // Calcular total
            double precio = Double.parseDouble(fila[4].toString());
            int cantidad = Integer.parseInt(fila[5].toString());
            total += precio * cantidad;
        }
        
        baseVenta.close();
        
        // Agregar total
        PdfPCell celdaTotal = new PdfPCell(new Phrase("Total Ventas: $" + String.format("%,.2f", total), TEXTO_NEGRITA));
        celdaTotal.setColspan(6);
        celdaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaTotal.setPadding(5);
        tabla.addCell(celdaTotal);
        
        documento.add(tabla);
    }
    
    private static void agregarCeldaEncabezado(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, TEXTO_NEGRITA));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
    
    private static void agregarCelda(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, TEXTO_NORMAL));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
}
