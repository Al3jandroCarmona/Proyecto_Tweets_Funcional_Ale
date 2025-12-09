/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author Ale Carmona
 */


import model.Tweet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TweetLoader {

    /**
     * Devuelve un Supplier<List<Tweet>> que al invocar get() lee el CSV y retorna la lista de tweets.
     * Se asume que las columnas son: id, entidad, sentimiento, texto  (texto es la última columna)
     */
    public static Supplier<List<Tweet>> crearLectorTweets(String rutaArchivo) {
        return () -> {
            List<Tweet> tweets = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
                String linea;
                boolean primera = true;
                while ((linea = br.readLine()) != null) {
                    if (primera) { // saltar encabezado si existe
                        primera = false;
                        if (linea.toLowerCase().contains("id") && linea.toLowerCase().contains("text")) {
                            continue;
                        }
                    }
                    // Dividir en 4 partes como máximo (texto puede contener comas)
                    String[] partes = linea.split(",", 4);
                    if (partes.length < 4) {
                        // línea mal formada: saltar o manejar
                        continue;
                    }
                    String id = partes[0].trim();
                    String entidad = partes[1].trim();
                    String sentimiento = partes[2].trim();
                    String texto = partes[3].trim();
                    tweets.add(new Tweet(id, entidad, sentimiento, texto));
                }
            } catch (IOException e) {
                System.err.println("Error leyendo archivo: " + e.getMessage());
            }
            return tweets;
        };
    }
}
