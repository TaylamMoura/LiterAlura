package com.alura.literAlura.Main;

import com.alura.literAlura.Model.Autor;
import com.alura.literAlura.Model.Livro;
import com.alura.literAlura.Model.LivroAutor;
import com.alura.literAlura.Repository.LivroRepository;
import com.alura.literAlura.Service.ConexaoAPI;
import com.alura.literAlura.Service.ConverteDados;
import com.alura.literAlura.Model.DadosLivros;
import com.alura.literAlura.Service.LivroService;
import com.alura.literAlura.Service.RespostaAPI;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {

    @Autowired
    private LivroService livroService;

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
//                case 3 -> autoresCadastrados();
//                case 4 -> autoresVivosCadastrados();
//                case 5 -> livrosIdioma();
                case 2 ->
                        listarLivrosCadastrados();
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

        if(livroEncontrado.isPresent()){
            DadosLivros dadosLivro = livroEncontrado.get();
            Livro livro = new Livro(dadosLivro);
            // Cria a relação LivroAutor
            List<LivroAutor> livroAutores = dadosLivro.autor().stream()
                    .map(autor -> {
                        LivroAutor livroAutor = new LivroAutor();
                        livroAutor.setLivro(livro);
                        livroAutor.setAutor(autor);
                        return livroAutor;
                    }).collect(Collectors.toList());
            livro.setLivroAutores(livroAutores);

            System.out.println(livro);
            try {
                livroService.salvarLivro(livro);
                System.out.println("Livro salvo no banco de dados.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao salvar livro: " + e.getMessage());
            }
    } else {
        System.out.println("Livro não encontrado.");
    }

    }

    private void listarLivrosCadastrados(){
        var livros = livroService.buscarLivrosSalvos();
        livros.forEach(System.out::println);
    }
}


//private void buscarLivroAPI(){
//        DadosLivros dadosLivros = getDadosLivros();
//        Livro livro = new Livro(dadosLivros);
//        repositorio.save(livro);
//        System.out.println(dadosLivros);
//    }
//
//    private  DadosLivros getDadosLivros(){
//        System.out.println("Digite o titulo do livro desejado: ");
//        var nomeLivro = leitura.nextLine().toLowerCase();
//        var json = conexao.obterDados(ENDERECO + nomeLivro.replace(" ", "%20"));
//        //DadosLivros dados = converteDados.obterDados(json, DadosLivros.class);
//        //return dados;
//
//        RespostaAPI respostaAPI = converteDados.obterDados(json, RespostaAPI.class);
//        Optional<DadosLivros> livroEncontrado = respostaAPI.getResultados()
//                .stream().filter(dadosLivros -> dadosLivros.titulo().equalsIgnoreCase(nomeLivro))
//                .findFirst();
//
//        if (livroEncontrado.isPresent()){
//            Livro livro = new Livro(livroEncontrado.get());
//            System.out.println(livro);
//            try{
//                livroService.salvarLivro(livro);
//                System.out.println("\nLivro salvo no bd"); //tirar depois
//            } catch (Exception e ){
//                e.printStackTrace();
//                System.out.println("erro ao salvar " + e.getMessage());
//            }
//
//        } else{
//            System.out.println("Livro não encontrado.");
//        }
//    }
//    private void listarLivrosCadastrados(){
//        livros = repositorio.findAll();
//        livros.stream().sorted(Comparator.comparing(Livro::getTitulo))
//                .forEach(System.out::println);
//
//    }
//