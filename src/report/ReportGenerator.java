/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package report;

/**
 *
 * @author Ale Carmona
 */


import model.Tweet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportGenerator {

    public static void guardarTweetsLimpios(List<Tweet> tweets, String rutaSalida) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaSalida))) {
            for (Tweet t : tweets) {
                // Formato simple: id|entidad|sentimiento|texto
                bw.write(String.format("%s|%s|%s|%s", t.getId(), t.getEntidad(), t.getSentimiento(), t.getTexto()));
                bw.newLine();
            }
            System.out.println("Archivo de tweets limpios guardado en: " + rutaSalida);
        } catch (IOException e) {
            System.err.println("Error guardando tweets limpios: " + e.getMessage());
        }
    }

    public static void guardarResumenEstadisticas(String resumen, String rutaSalida) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaSalida))) {
            bw.write(resumen);
            bw.newLine();
            System.out.println("Resumen de estadísticas guardado en: " + rutaSalida);
        } catch (IOException e) {
            System.err.println("Error guardando resumen de estadísticas: " + e.getMessage());
        }
    }
}

