package com.cybertron.autobot;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorAutobots {

    private final List<Autobot> lista = new ArrayList<>();

    public void adicionar(Autobot a) {
        if (a == null) {
            throw new IllegalArgumentException("Autobot não pode ser nulo");
        }
        lista.add(a);
    }

    public double calcularEnergonTotal() {
        double total = 0;
        for (int i = 0; i < lista.size(); i++) {        // corrigido
            total += lista.get(i).getEnergon();
        }
        return total;
    }

    public Autobot buscar(String nome) {
        if (nome == null) return null;
        for (Autobot a : lista) {
            if (nome.equals(a.getNome())) {              // corrigido
                return a;
            }
        }
        return null;
    }

    public void reparar(Autobot a) {
        if (a == null) {
            throw new IllegalArgumentException("Autobot não pode ser nulo");
        }
        if (a.getEnergon() < 0) {
            throw new IllegalStateException("Não é possível reparar: energon negativo");
        }
        a.setEnergon(a.getEnergon() + 100);
    }

    public String classificar(Autobot a) {
        if (a == null) {
            throw new IllegalArgumentException("Autobot não pode ser nulo");
        }
        if (a.getEnergon() > 500) return "ELITE";
        if (a.getEnergon() > 200) return "PADRAO";
        return "RESERVA";
    }
}