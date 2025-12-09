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

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextTransformService {

    // Regex para menciones y hashtags
    private static final Pattern MENCION = Pattern.compile("@\\w+");
    private static final Pattern HASHTAG = Pattern.compile("#\\w+");
    private static final Pattern MULTI_WHITESPACE = Pattern.compile("\\s+");

    /**
     * Transformación por defecto: quitar menciones, hashtags, limpiar espacios extras y pasar a minúsculas.
     * Devuelve una Function<Tweet, Tweet>.
     */
    public static Function<Tweet, Tweet> transformacionPorDefecto() {
        return tweet -> {
            String t = tweet.getTexto();
            t = MENCION.matcher(t).replaceAll("");      // quitar @usuario
            t = HASHTAG.matcher(t).replaceAll("");      // quitar #tema
            t = t.replaceAll("[\\r\\n]+", " ");        // eliminar saltos de línea
            t = MULTI_WHITESPACE.matcher(t).replaceAll(" ").trim(); // espacios repetidos
            // Ejemplo: pasar a normalizado (no forzamos mayúsculas automáticamente; si quieres, usar toUpperCase)
            return tweet.withTexto(t);
        };
    }

    /**
     * Utility para transformar una lista completa aplicando la Function.
     */
    public static List<Tweet> transformarTweets(List<Tweet> tweets, Function<Tweet, Tweet> transformacion) {
        return tweets.stream()
                .map(transformacion)
                .collect(Collectors.toList());
    }
}

