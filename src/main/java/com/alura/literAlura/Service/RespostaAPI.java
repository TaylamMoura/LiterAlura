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

    public int getContagem() {
        return contagem;
    }

    public void setContagem(int contagem) {
        this.contagem = contagem;
    }

    public List<DadosLivros> getResultados() {
        return resultados;
    }

    public void setResultados(List<DadosLivros> resultados) {
        this.resultados = resultados;
    }
}
