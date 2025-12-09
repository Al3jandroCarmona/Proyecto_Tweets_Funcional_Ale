/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

/**
 *
 * @author Ale Carmona
 */


import model.Tweet;
import report.ReportGenerator;
import service.TextTransformService;
import service.TweetAnalyticsService;
import service.TweetLoader;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {
        // Rutas (ajusta si necesario)
        final String rutaCsv = "data/twitters.csv";
        final String rutaTweetsLimpios = "output/tweets_limpios.txt";
        final String rutaResumen = "output/resumen_estadisticas.txt";

        // Asegurar carpetas
        try {
            Files.createDirectories(Paths.get("data"));
            Files.createDirectories(Paths.get("output"));
        } catch (Exception e) {
            System.err.println("No se pudieron crear carpetas: " + e.getMessage());
        }

        // Supplier para la carga de tweets
        Supplier<List<Tweet>> lector = TweetLoader.crearLectorTweets(rutaCsv);

        // Transformación (Function) - usando la transformacionPorDefecto
        Function<Tweet, Tweet> transformacion = TextTransformService.transformacionPorDefecto();

        // Lista donde almacenaremos los tweets limpios mediante el Consumer
        List<Tweet> tweetsLimpiosCollector = Collections.synchronizedList(new ArrayList<>());

        // Consumer que agrega cada tweet a la lista de tweets limpios
        Consumer<Tweet> consumerGuardarEnLista = tweetsLimpiosCollector::add;

        // Runnable principal: carga -> transforma -> analiza -> reporta
        Runnable pipelinePrincipal = () -> {
            System.out.println("Iniciando pipeline principal...");

            // 1) Cargar datos
            List<Tweet> tweets = lector.get();
            System.out.println("Tweets cargados: " + tweets.size());

            // 2) Transformar y procesar (se llenará tweetsLimpiosCollector)
            TweetAnalyticsService.procesarTweets(tweets, transformacion, consumerGuardarEnLista);

            System.out.println("Tweets transformados: " + tweetsLimpiosCollector.size());

            // 3) Análisis estadístico
            double promedioPositivos = TweetAnalyticsService.calcularPromedioLongitud(tweetsLimpiosCollector, "positive");
            double promedioNegativos = TweetAnalyticsService.calcularPromedioLongitud(tweetsLimpiosCollector, "negative");
            Map<String, Long> conteo = TweetAnalyticsService.contarTweetsPorSentimiento(tweetsLimpiosCollector);

            // 4) Preparar resumen
            StringBuilder resumen = new StringBuilder();
            resumen.append("RESUMEN DE ESTADÍSTICAS\n");
            resumen.append("======================\n");
            resumen.append("Total tweets cargados: ").append(tweets.size()).append("\n");
            resumen.append("Total tweets transformados: ").append(tweetsLimpiosCollector.size()).append("\n\n");
            resumen.append(String.format("Promedio longitud (positive): %.2f\n", promedioPositivos));
            resumen.append(String.format("Promedio longitud (negative): %.2f\n", promedioNegativos));
            resumen.append("\nConteo por sentimiento:\n");
            conteo.forEach((k, v) -> resumen.append(String.format("  %s : %d\n", k, v)));

            // 5) Guardar reportes
            ReportGenerator.guardarTweetsLimpios(tweetsLimpiosCollector, rutaTweetsLimpios);
            ReportGenerator.guardarResumenEstadisticas(resumen.toString(), rutaResumen);

            System.out.println("Pipeline finalizado.");
        };

        // Runnable alterno para solo generar reportes (ejemplo)
        Runnable generarReportes = () -> {
            System.out.println("Generando reportes (runnable secundario)...");
            // Simplemente reusar el flujo: cargar -> transformar -> guardar
            List<Tweet> tweets = lector.get();
            List<Tweet> transformados = new ArrayList<>();
            TweetAnalyticsService.procesarTweets(tweets, transformacion, transformados::add);
            ReportGenerator.guardarTweetsLimpios(transformados, rutaTweetsLimpios);
            System.out.println("Reportes generados.");
        };

        // Ejecutar el runnable principal
        pipelinePrincipal.run();

        // Si quieres ejecutar el otro Runnable, descomenta:
        // generarReportes.run();
    }
}
