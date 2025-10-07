package org.example.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CrearArchivoTxt {

    /**
     * Método para crear o sobrescribir un archivo de texto con el contenido recibido.
     *
     * @param nombreArchivo Nombre del archivo (por ejemplo, "salida.txt")
     * @param contenido     Texto que se desea guardar
     */
    public static void guardarTexto(String nombreArchivo, String contenido) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write(contenido);
            System.out.println("Archivo '" + nombreArchivo + "' creado y guardado exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
        }
    }
}
