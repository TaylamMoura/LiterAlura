package com.alura.literAlura.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LivroAutor> livroAutores = new ArrayList<>();

    @ElementCollection(fetch =  FetchType.EAGER)
    @CollectionTable(name = "livro_idioma", joinColumns = @JoinColumn(name = "livro_id"))
    @Column(name = "idioma")
    private  List<String> idioma;

    @Column(name = "downloads")
    private double downloads;


    public Livro(){}


    public Livro(DadosLivros dadosLivros){
        this.titulo = dadosLivros.titulo();
        this.idioma = dadosLivros.idioma();
        this.downloads = dadosLivros.numeroDeDownloads();
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<LivroAutor> getLivroAutores() {
        return livroAutores;
    }

    public void setLivroAutores(List<LivroAutor> livroAutores) {
        this.livroAutores = livroAutores;
    }

    public List<String> getIdioma() {
        return idioma;
    }

    public double getDownloads() {
        return downloads;
    }

    @Override
    public String toString() {
        String autores = livroAutores.stream()
                .map(livroAutor -> livroAutor.getAutor().getNome())
                .collect(Collectors.joining(", "));
        String idiomas = String.join(", ", idioma);

        return "------------------------------" +
                "\nLIVRO\n" +
                "Título = " + titulo + "\n" +
                "Autor= " + autores + "\n" +
                "Idioma = " + idiomas + "\n" +
                "Nº de downloads = " + downloads + "\n";
    }
}
