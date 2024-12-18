package com.alura.literAlura.Service;

import com.alura.literAlura.Model.DadosLivros;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RespostaAPI {
    @JsonAlias("count")
    private int contagem;

    @JsonAlias("results")
    private List<DadosLivros> resultados;

    public List<DadosLivros> getResultados() {
        return resultados;
    }
}
