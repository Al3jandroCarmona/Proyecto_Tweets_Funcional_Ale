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

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TweetAnalyticsService {

    /**
     * Aplica la transformación a cada tweet y ejecuta la Consumer para cada tweet transformado.
     * Se espera que el caller pase un Consumer (por ejemplo, collector a lista o impresor).
     */
    public static void procesarTweets(List<Tweet> tweets, Function<Tweet, Tweet> transformacion, Consumer<Tweet> accionFinal) {
        tweets.stream()
                .map(transformacion)
                .forEach(accionFinal);
    }

    /**
     * Calcula el promedio de longitud (caracteres) del texto para tweets con el sentimiento indicado.
     * Retorna 0.0 si no hay tweets con ese sentimiento.
     */
    public static double calcularPromedioLongitud(List<Tweet> tweets, String sentimiento) {
        return tweets.stream()
                .filter(t -> t.getSentimiento() != null && t.getSentimiento().equalsIgnoreCase(sentimiento))
                .mapToInt(t -> t.getTexto() != null ? t.getTexto().length() : 0)
                .average()
                .orElse(0.0);
    }

    /**
     * Cuenta cuántos tweets hay por sentimiento y devuelve un Map<sentimiento, conteo>.
     */
    public static Map<String, Long> contarTweetsPorSentimiento(List<Tweet> tweets) {
        return tweets.stream()
                .collect(Collectors.groupingBy(
                        t -> Optional.ofNullable(t.getSentimiento()).orElse("unknown").toLowerCase(),
                        Collectors.counting()
                ));
    }
}
