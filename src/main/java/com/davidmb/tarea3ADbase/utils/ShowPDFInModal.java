package com.davidmb.tarea3ADbase.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase que permite visualizar un archivo PDF dentro de una ventana modal en la aplicación.
 * 
 * Utiliza `PDFBox` para cargar y renderizar las páginas del documento PDF, y muestra la imagen 
 * generada en un `ImageView`. La navegación entre páginas se realiza mediante botones.
 * 
 * @author DavidMB
 */
public class ShowPDFInModal {

    /**
     * Muestra un archivo PDF en una ventana modal con controles para navegar entre sus páginas.
     * 
     * @param pdfPath Ruta del archivo PDF a visualizar.
     */
    public void showPdfInModal(String pdfPath) {
        // Usar AtomicInteger para manejar el índice de la página actual
        AtomicInteger pageIndex = new AtomicInteger(0); 

        // Crear la ventana modal
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Reporte de Paradas Visitadas");
        modalStage.setWidth(800);
        modalStage.setHeight(600);
        modalStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/report.png")));

        // Configurar la vista de imagen donde se renderizará la página del PDF
        ImageView imageView = new ImageView();
        imageView.setFitWidth(750);
        imageView.setFitHeight(550);
        imageView.setPreserveRatio(true);

        // Botón para retroceder en las páginas
        Button pageBackwardButton = new Button("<");
        pageBackwardButton.setTooltip(new Tooltip("Página anterior"));
        pageBackwardButton.setOnAction(e -> {
            if (pageIndex.get() > 0) {
                pageIndex.decrementAndGet();  // Disminuir el índice
                render(pdfPath, imageView, pageIndex.get()); // Actualizar la imagen
            }
        });

        // Botón para avanzar en las páginas
        Button pageForwardButton = new Button(">");
        pageForwardButton.setTooltip(new Tooltip("Página siguiente"));
        pageForwardButton.setOnAction(e -> {
            try {
                PDDocument document = Loader.loadPDF(new File(pdfPath));
                if (pageIndex.get() < document.getNumberOfPages() - 1) {
                    pageIndex.incrementAndGet();  // Aumentar el índice
                    render(pdfPath, imageView, pageIndex.get()); // Actualizar la imagen
                }
                document.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Crear la barra de herramientas con los botones de navegación
        ToolBar toolbar = new ToolBar(pageBackwardButton, pageForwardButton);
        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(imageView);

        // Configurar la escena y mostrar la ventana modal
        Scene scene = new Scene(root);
        modalStage.setScene(scene);

        try {
            File pdfFile = new File(pdfPath);
            if (!pdfFile.exists()) {
                System.out.println("El archivo PDF no existe: " + pdfPath);
                return;
            }

            // Cargar el documento PDF
            PDDocument document = Loader.loadPDF(pdfFile);

            // Renderizar la primera página del PDF
            render(pdfPath, imageView, pageIndex.get());

            // Cerrar el documento al cerrar la ventana modal
            modalStage.setOnCloseRequest(event -> {
                try {
                    if (document != null) {
                        document.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Renderiza y muestra una página específica del documento PDF en un `ImageView`.
     * 
     * @param pdfPath Ruta del archivo PDF.
     * @param imageView Componente `ImageView` donde se mostrará la página renderizada.
     * @param pageIndex Índice de la página a visualizar.
     */
    private void render(String pdfPath, ImageView imageView, int pageIndex) {
        try {
            File pdfFile = new File(pdfPath);
            if (!pdfFile.exists()) {
                System.out.println("El archivo PDF no existe: " + pdfPath);
                return;
            }

            // Cargar el documento PDF
            PDDocument document = Loader.loadPDF(pdfFile);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // Renderizar la página solicitada a imagen
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageIndex, 150); // 150 DPI
            Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(fxImage);

            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
