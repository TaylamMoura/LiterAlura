package com.alura.literAlura.Model;

import java.util.List;
import java.util.stream.Collectors;

public class Livro {
    private String titulo;
    private List<Autor> autor;
    private  List<String> idioma;
    private double downloads;

    public Livro(DadosLivros dadosLivros){
        this.titulo = dadosLivros.titulo();
        this.autor = dadosLivros.autor();
        this.idioma = dadosLivros.idioma();
        this.downloads = dadosLivros.numeroDeDownloads();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutor() {
        return autor;
    }

    public void setAutor(List<Autor> autor) {
        this.autor = autor;
    }

    public List<String> getIdioma() {
        return idioma;
    }

    public void setIdioma(List<String> idioma) {
        this.idioma = idioma;
    }

    public double getDownloads() {
        return downloads;
    }

    public void setDownloads(double downloads) {
        this.downloads = downloads;
    }


    @Override
    public String toString() {
        String autores = autor.stream().map(Autor::toString).collect(Collectors.joining(", "));
        String idiomas = String.join(", ", idioma);

        return "\nLIVRO\n" +
                "Título = " +titulo+ "\n" +
                "Autor= " +autores+ "\n" +
                "Idioma = " + idiomas+ "\n" +
                "Nº de downloads = " +downloads + "\n";
    }
}
