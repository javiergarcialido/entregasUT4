package com.jgarcia.objetos;

public class Manga {
    private Long id;
    private String titulo;
    private String genero;
    private int capitulos;
    private double precio;

    public Manga() {
    }

    public Manga(Long id, String titulo, String genero, int capitulos, double precio) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.capitulos = capitulos;
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(int capitulos) {
        this.capitulos = capitulos;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Manga{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", genero='" + genero + '\'' +
                ", capitulos=" + capitulos +
                ", precio=" + precio +
                '}';
    }
}
