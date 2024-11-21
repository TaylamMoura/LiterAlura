package com.alura.literAlura.Main;

import com.alura.literAlura.Model.Livro;
import com.alura.literAlura.Service.ConexaoAPI;
import com.alura.literAlura.Service.ConverteDados;
import com.alura.literAlura.Model.DadosLivros;
import com.alura.literAlura.Service.RespostaAPI;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConexaoAPI conexao = new ConexaoAPI();
    private ConverteDados converteDados = new ConverteDados();

    private static final String ENDERECO = "https://gutendex.com/books?search=";

    public void menuOpcoes() {
        System.out.println(" - MENU - ");
        System.out.println("1 - buscar livro pelo TÍTULO");
        System.out.println("2 - listar livros registrados");
        System.out.println("3 - listar autores registrados");
        System.out.println("4 - listar autores vivos em determinado ano");
        System.out.println("5 - listar livros em um determinado idioma");
        System.out.println("0 - SAIR");
        System.out.println("\nDigite o número da opção desejada: ");
    }

    public void telaInical() throws JsonProcessingException {
        while (true) {
            menuOpcoes();

            int opcao = leitura.nextInt();
            leitura.nextLine();
            switch (opcao) {
                case 1 -> buscarLivroAPI();
//                case 2 -> livrosCadastrados();
//                case 3 -> autoresCadastrados();
//                case 4 -> autoresVivosCadastrados();
//                case 5 -> livrosIdioma();
                case 0 -> {
                    System.out.println("saindo....");
                    return;
                }
                default -> System.out.println("Opção inválida.Digite novamente");
            }
        }
    }

    public void buscarLivroAPI() {

        System.out.println("Digite o título do livro: ");
        var nomeLivro = leitura.nextLine().toLowerCase();
        var url = ENDERECO + nomeLivro.replace(" ", "%20");
        var json = conexao.obterDados(url);
        System.out.println("RESPOSTA API: "+ json); //TIRAR DEPOIS

        RespostaAPI respostaAPI = converteDados.obterDados(json, RespostaAPI.class);

        Optional<DadosLivros> livroEncontrado = respostaAPI.getResultados()
                .stream().filter(dadosLivros -> dadosLivros.titulo().equalsIgnoreCase(nomeLivro))
                .findFirst();

        if (livroEncontrado.isPresent()){
            Livro livro = new Livro(livroEncontrado.get());
            System.out.println(livro);
        } else{
            System.out.println("Livro não encontrado.");
        }
    }
}




//    public void exibeMenu() {
//        while (true) {
//            exibirMenuOpcoes();
//            System.out.println("Digite uma opção: ");
//            int opcao = leitura.nextInt();
//            leitura.nextLine(); // Consome a nova linha
//
//            switch (opcao) {
//                case 1 -> buscarLivroPorTitulo();
//                case 6 -> {
//                    System.out.println("Saindo...");
//                    return;
//                }
//                default -> System.out.println("Opção inválida, tente novamente.");
//            }
//        }
//    }
//
//    private void exibirMenuOpcoes() {
//        System.out.println("\n-------------------------------------------------" );
//        System.out.println("MENU");
//        System.out.println("1) Buscar livro pelo TÍTULO");
//        System.out.println("6) SAIR.");
//        System.out.println("\n-------------------------------------------------");
//    }
//
//    private void buscarLivroPorTitulo() {
//        System.out.println("\n--------------------------------------------------");
//        System.out.println("Digite o TÍTULO do livro: ");
//        String nomeLivro = leitura.nextLine();
//        String url = ENDERECO + nomeLivro.replace(" ", "%20");
//        System.out.println("URL da Requisição: " + url);
//        String json = conexao.obterDados(url);
//
//
//
//        System.out.println("Resposta da API: " + json);
//
//        if (json == null || json.trim().isEmpty()) {
//            System.out.println("Nenhum dado retornado da API.");
//            return;
//        }
//
//        try {
//            DadosLivros dadosLivros = converteDados.obterDados(json, DadosLivros.class);
//            Livro livro = new Livro(dadosLivros);
//            System.out.println(livro);
//        } catch (RuntimeException e) {
//            System.out.println("Erro ao processar os dados: " + e.getMessage());
//        }
//    }