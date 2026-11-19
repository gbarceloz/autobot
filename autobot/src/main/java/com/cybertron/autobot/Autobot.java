package com.cybertron.autobot;

public class Autobot {

    private String nome;
    private double energon;
    private int nivel;

    public Autobot(String nome, double energon, int nivel) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do Autobot inválido");
        }
        if (energon < 0) {
            throw new IllegalArgumentException("Energon não pode ser negativo");
        }
        this.nome = nome;
        this.energon = energon;
        this.nivel = nivel;
    }

    public String getNome() { return nome; }
    public double getEnergon() { return energon; }
    public int getNivel() { return nivel; }

    public void setEnergon(double energon) {
        if (energon < 0) {
            throw new IllegalArgumentException("Energon não pode ser negativo");
        }
        this.energon = energon;
    }
}