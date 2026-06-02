package domain;

public class Telemetria {
    private String data;
    private double nivelRio;
    private double volumeChuva;

    public Telemetria(String data, double nivelRio, double volumeChuva) {
        this.data = data;
        this.nivelRio = nivelRio;
        this.volumeChuva = volumeChuva;
    }

    public String getData() { return data; }
    public double getNivelRio() { return nivelRio; }
    public double getVolumeChuva() { return volumeChuva; }
}