/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Ale Carmona
 */


public class Tweet {
    private final String id;
    private final String entidad;
    private final String sentimiento;
    private final String texto;

    public Tweet(String id, String entidad, String sentimiento, String texto) {
        this.id = id;
        this.entidad = entidad;
        this.sentimiento = sentimiento;
        this.texto = texto;
    }

    public String getId() { return id; }
    public String getEntidad() { return entidad; }
    public String getSentimiento() { return sentimiento; }
    public String getTexto() { return texto; }

    // Convenience to create a new Tweet with modified text (immutability)
    public Tweet withTexto(String nuevoTexto) {
        return new Tweet(this.id, this.entidad, this.sentimiento, nuevoTexto);
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id='" + id + '\'' +
                ", entidad='" + entidad + '\'' +
                ", sentimiento='" + sentimiento + '\'' +
                ", texto='" + texto + '\'' +
                '}';
    }
}
