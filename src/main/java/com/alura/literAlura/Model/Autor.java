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

//    @Transient
//    private List<Livro> obras;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LivroAutor> livroAutores;

    public Autor(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNascimento() {
        return nascimento;
    }

    public void setNascimento(int nascimento) {
        this.nascimento = nascimento;
    }

    public int getAnoFalecimento() {
        return anoFalecimento;
    }

    public void setAnoFalecimento(int anoFalecimento) {
        this.anoFalecimento = anoFalecimento;
    }

    public List<LivroAutor> getLivroAutores() {
        return livroAutores;
    }

    public void setLivroAutores(List<LivroAutor> livroAutores) {
        this.livroAutores = livroAutores;
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
