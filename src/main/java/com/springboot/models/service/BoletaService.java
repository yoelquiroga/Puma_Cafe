package com.springboot.models.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.springboot.models.entiys.DetallePedido;
import com.springboot.models.entiys.Pedido;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class BoletaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoletaService.class);

    private static final Color VERDE_PRIMARIO = new Color(46, 125, 50);
    private static final Color VERDE_OSCURO = new Color(21, 21, 21);
    private static final Color GRIS_CLARO = new Color(245, 245, 245);
    private static final Color BLANCO = new Color(255, 255, 255);

    public void generarBoleta(Pedido pedido, List<DetallePedido> detalles, double total, HttpServletResponse response) {
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            addHeader(document);
            addTitle(document, pedido);
            addCustomerData(document, pedido);
            addProductsTable(document, detalles, total);
            addFooter(document, pedido);
            document.close();
        } catch (Exception e) {
            LOGGER.error("Error generando boleta PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar la boleta", e);
        }
    }

    public byte[] generarBoletaBytes(Pedido pedido, List<DetallePedido> detalles, double total) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            addHeader(document);
            addTitle(document, pedido);
            addCustomerData(document, pedido);
            addProductsTable(document, detalles, total);
            addFooter(document, pedido);
            document.close();
        } catch (Exception e) {
            LOGGER.error("Error generando boleta PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar la boleta", e);
        }
        return baos.toByteArray();
    }

    private void addHeader(Document document) throws DocumentException {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1f, 2f});

        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setPaddingBottom(10f);

        try {
            InputStream is = getClass().getResourceAsStream("/static/img/puma_cafe_boleta.png");
            if (is != null) {
                byte[] logoBytes = is.readAllBytes();
                Image logo = Image.getInstance(logoBytes);
                logo.scaleToFit(120, 80);
                logoCell.addElement(logo);
            } else {
                LOGGER.warn("Logo no encontrado en classpath:/static/img/puma_cafe_boleta.png, mostrando solo texto");
                logoCell.addElement(new Paragraph("PUMA CAFÉ",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, VERDE_PRIMARIO)));
            }
        } catch (Exception e) {
            LOGGER.warn("No se pudo cargar el logo: {}", e.getMessage());
            logoCell.addElement(new Paragraph("PUMA CAFÉ",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, VERDE_PRIMARIO)));
        }
        headerTable.addCell(logoCell);

        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.setPaddingBottom(10f);

        Font companyFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, VERDE_OSCURO);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.BLACK);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);

        infoCell.addElement(new Paragraph("PUMA CAFÉ", companyFont));
        infoCell.addElement(createInfoLine("RUC: ", "20506370346", labelFont, valueFont));
        infoCell.addElement(createInfoLine("Razón Social: ", "CENTRAL DE ORGANIZACIONES PRODUCTORAS DE CAFÉ Y CACAO DEL PERÚ - CAFE PERU", labelFont, valueFont));
        infoCell.addElement(createInfoLine("Dirección: ", "Av. Arenales 1199, Santa Beatriz, Lima", labelFont, valueFont));
        infoCell.addElement(createInfoLine("Teléfono: ", "(511) 265 3844", labelFont, valueFont));
        infoCell.addElement(createInfoLine("Email: ", "ventas@centralcafeycacao.org", labelFont, valueFont));

        headerTable.addCell(infoCell);
        document.add(headerTable);

        Paragraph separator = new Paragraph("___________________________________________________________",
                FontFactory.getFont(FontFactory.HELVETICA, 8, Color.LIGHT_GRAY));
        separator.setAlignment(Element.ALIGN_CENTER);
        separator.setSpacingAfter(15f);
        document.add(separator);
    }

    private Phrase createInfoLine(String label, String value, Font labelFont, Font valueFont) {
        Phrase phrase = new Phrase();
        phrase.add(new Chunk(label, labelFont));
        phrase.add(new Chunk(value, valueFont));
        phrase.add(Chunk.NEWLINE);
        return phrase;
    }

    private void addTitle(Document document, Pedido pedido) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, VERDE_OSCURO);
        Paragraph title = new Paragraph("BOLETA DE VENTA ELECTRÓNICA", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(4f);
        document.add(title);

        Font numFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, VERDE_PRIMARIO);
        Paragraph num = new Paragraph("B001-" + pedido.getIdPedidos(), numFont);
        num.setAlignment(Element.ALIGN_CENTER);
        num.setSpacingAfter(20f);
        document.add(num);
    }

    private void addCustomerData(Document document, Pedido pedido) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, VERDE_PRIMARIO);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

        Paragraph sectionTitle = new Paragraph("DATOS DEL CLIENTE", sectionFont);
        sectionTitle.setSpacingAfter(8f);
        document.add(sectionTitle);

        String nombreCompleto = (pedido.getUsuario().getNombre() != null ? pedido.getUsuario().getNombre() : "")
                + " " + (pedido.getUsuario().getApellido() != null ? pedido.getUsuario().getApellido() : "");
        String fechaStr = pedido.getFecha() != null
                ? new SimpleDateFormat("dd/MM/yyyy").format(pedido.getFecha())
                : "-";

        PdfPTable dataTable = new PdfPTable(2);
        dataTable.setWidthPercentage(100);
        dataTable.setWidths(new float[]{1f, 1f});

        addDataCell(dataTable, "Cliente:", nombreCompleto, labelFont, valueFont, false);
        addDataCell(dataTable, "Fecha:", fechaStr, labelFont, valueFont, false);
        addDataCell(dataTable, "Dirección:", pedido.getDireccionEntrega() != null ? pedido.getDireccionEntrega() : "-", labelFont, valueFont, false);
        addDataCell(dataTable, "Método de Pago:", pedido.getMetodoPago() != null ? pedido.getMetodoPago().name() : "-", labelFont, valueFont, false);

        document.add(dataTable);
        document.add(Chunk.NEWLINE);
    }

    private void addDataCell(PdfPTable table, String label, String value, Font labelFont, Font valueFont, boolean isFooter) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPaddingBottom(4f);
        if (isFooter) {
            labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        }
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPaddingBottom(4f);
        table.addCell(valueCell);
    }

    private void addProductsTable(Document document, List<DetallePedido> detalles, double total) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, VERDE_PRIMARIO);
        Paragraph sectionTitle = new Paragraph("PRODUCTOS", sectionFont);
        sectionTitle.setSpacingAfter(8f);
        document.add(sectionTitle);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3f, 1f, 2f, 2f});
        table.setSpacingAfter(10f);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BLANCO);
        Color headerBg = VERDE_OSCURO;

        addHeaderCell(table, "Producto", headerFont, headerBg);
        addHeaderCell(table, "Cant.", headerFont, headerBg);
        addHeaderCell(table, "Precio Unit.", headerFont, headerBg);
        addHeaderCell(table, "Subtotal", headerFont, headerBg);

        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);
        boolean alternate = false;

        for (DetallePedido detalle : detalles) {
            Color rowBg = alternate ? GRIS_CLARO : BLANCO;

            String nombre = detalle.getProducto() != null ? detalle.getProducto().getNombre() : "-";
            int cantidad = detalle.getCantidad();
            double precioUnit = detalle.getPrecioUnitario();
            double subtotal = precioUnit * cantidad;

            addBodyCell(table, nombre, bodyFont, rowBg);
            addBodyCell(table, String.valueOf(cantidad), bodyFont, rowBg);
            addBodyCell(table, "S/ " + String.format("%.2f", precioUnit), bodyFont, rowBg);
            addBodyCell(table, "S/ " + String.format("%.2f", subtotal), bodyFont, rowBg);

            alternate = !alternate;
        }

        document.add(table);

        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, VERDE_PRIMARIO);
        double subtotal = total; // The total may already be the subtotal, but let's compute
        double igv = total * 0.18;
        double totalConIgv = total + igv;

        PdfPTable totalsTable = new PdfPTable(2);
        totalsTable.setWidthPercentage(50);
        totalsTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalsTable.setWidths(new float[]{1f, 1f});

        addTotalRow(totalsTable, "Subtotal:", "S/ " + String.format("%.2f", subtotal), boldFont, false);
        addTotalRow(totalsTable, "IGV (18%):", "S/ " + String.format("%.2f", igv), boldFont, false);

        PdfPCell labelTotalCell = new PdfPCell(new Phrase("TOTAL:", totalFont));
        labelTotalCell.setBorder(PdfPCell.TOP);
        labelTotalCell.setPaddingTop(6f);
        labelTotalCell.setPaddingBottom(4f);
        labelTotalCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        totalsTable.addCell(labelTotalCell);

        PdfPCell valueTotalCell = new PdfPCell(new Phrase("S/ " + String.format("%.2f", totalConIgv), totalFont));
        valueTotalCell.setBorder(PdfPCell.TOP);
        valueTotalCell.setPaddingTop(6f);
        valueTotalCell.setPaddingBottom(4f);
        valueTotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalsTable.addCell(valueTotalCell);

        document.add(totalsTable);
        document.add(Chunk.NEWLINE);
    }

    private void addHeaderCell(PdfPTable table, String text, Font font, Color bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(6f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    private void addBodyCell(PdfPTable table, String text, Font font, Color bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(4f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    private void addTotalRow(PdfPTable table, String label, String value, Font font, boolean isBold) {
        Font rowFont = isBold
                ? FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK)
                : font;

        PdfPCell labelCell = new PdfPCell(new Phrase(label, rowFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(2f);
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, rowFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(2f);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(valueCell);
    }

    private void addFooter(Document document, Pedido pedido) throws DocumentException {
        Paragraph separator = new Paragraph("___________________________________________________________",
                FontFactory.getFont(FontFactory.HELVETICA, 8, Color.LIGHT_GRAY));
        separator.setAlignment(Element.ALIGN_CENTER);
        separator.setSpacingAfter(15f);
        document.add(separator);

        Font thanksFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, VERDE_PRIMARIO);
        Paragraph thanks = new Paragraph("¡Gracias por su compra!", thanksFont);
        thanks.setAlignment(Element.ALIGN_CENTER);
        thanks.setSpacingAfter(6f);
        document.add(thanks);

        Font msgFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Color.DARK_GRAY);
        Paragraph msg = new Paragraph("Su apoyo a los productores de café peruano es fundamental", msgFont);
        msg.setAlignment(Element.ALIGN_CENTER);
        msg.setSpacingAfter(15f);
        document.add(msg);

        String estadoStr = pedido.getEstado() != null ? pedido.getEstado().name() : "PENDIENTE";
        Font estadoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, getEstadoColor(pedido.getEstado()));
        Paragraph estado = new Paragraph("Estado: " + estadoStr.replace("_", " "), estadoFont);
        estado.setAlignment(Element.ALIGN_CENTER);
        estado.setSpacingAfter(10f);
        document.add(estado);

        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.LIGHT_GRAY);
        Paragraph footerNote = new Paragraph("Este documento es una representación electrónica de su boleta", footerFont);
        footerNote.setAlignment(Element.ALIGN_CENTER);
        document.add(footerNote);
    }

    private Color getEstadoColor(Pedido.EstadoPedido estado) {
        if (estado == null) return Color.DARK_GRAY;
        return switch (estado) {
            case PENDIENTE -> new Color(255, 193, 7);
            case EN_RUTA -> new Color(13, 110, 253);
            case ENTREGADO -> new Color(25, 135, 84);
            case CANCELADO -> new Color(220, 53, 69);
        };
    }
}
