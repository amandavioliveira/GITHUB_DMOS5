package br.edu.ifsp.arq.dmos5_2020s1.github_dmos5.model;

import java.io.Serializable;

public class NomesRepositorios implements Serializable {

    private String name;

    public NomesRepositorios(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
