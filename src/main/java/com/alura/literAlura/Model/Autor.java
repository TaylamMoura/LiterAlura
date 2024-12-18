package com.alura.literAlura.Model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("name")
    @Column(name = "nome")
    private String nome;

    @JsonAlias("birth_year")
    @Column(name = "nascimento")
    private int nascimento;

    @JsonAlias("death_year")
    @Column(name = "ano_falecimento")
    private int anoFalecimento;


    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LivroAutor> livroAutores;


    public Autor(){}


    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getNascimento() {
        return nascimento;
    }

    public int getAnoFalecimento() {
        return anoFalecimento;
    }

    public List<LivroAutor> getLivroAutores() {
        return livroAutores;
    }

    public List<Livro> getObras() {
        return livroAutores.stream()
                .map(LivroAutor::getLivro)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return  nome;
    }
}
