package com.alura.literAlura.Main;

import com.alura.literAlura.Model.Autor;
import com.alura.literAlura.Model.Livro;
import com.alura.literAlura.Model.LivroAutor;
import com.alura.literAlura.Repository.AutorRepository;
import com.alura.literAlura.Service.ConexaoAPI;
import com.alura.literAlura.Service.ConverteDados;
import com.alura.literAlura.Model.DadosLivros;
import com.alura.literAlura.Service.LivroService;
import com.alura.literAlura.Service.RespostaAPI;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
public class Principal {

    @Autowired
    private LivroService livroService;

    private Scanner leitura = new Scanner(System.in);
    private ConexaoAPI conexao = new ConexaoAPI();
    private ConverteDados converteDados = new ConverteDados();
    private AutorRepository repositorio;

    private static final String ENDERECO = "https://gutendex.com/books?search=";

    public Principal(LivroService livroService, AutorRepository repositorio) {
        this.livroService = livroService;
        this.repositorio = repositorio;
    }

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
                case 1:
                    buscarLivroAPI();
                    break;
                case 2:
                    listarLivrosCadastrados();
                    break;
                case 3:
                    autoresCadastrados();
                    break;
                case 4:
                    autoresVivosEmDeterminadoAno();
                    break;
//                case 5:
//                    listarLivrosPeloIdioma();
//                    break;
                case 0:
                    System.out.println("Obrigado por usar nossos Serviços!");
                    return;
                default:
                    System.out.println("Opção inválida.Digite novamente");

                    //                case 4 -> autoresVivosCadastrados();
//                case 5 -> livrosIdioma();

            }
        }
    }


    public void buscarLivroAPI() {
        System.out.println("Digite o título do livro: ");
        var nomeLivro = leitura.nextLine().toLowerCase();
        var url = ENDERECO + nomeLivro.replace(" ", "%20");
        var json = conexao.obterDados(url);
        System.out.println("RESPOSTA API: " + json); //TIRAR DEPOIS

        RespostaAPI respostaAPI = converteDados.obterDados(json, RespostaAPI.class);

        Optional<DadosLivros> livroEncontrado = respostaAPI.getResultados()
                .stream().filter(dadosLivros -> dadosLivros.titulo().equalsIgnoreCase(nomeLivro))
                .findFirst();

        if (livroEncontrado.isPresent()) {
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

    private void listarLivrosCadastrados() {
        var livros = livroService.buscarLivrosSalvos();
        livros.forEach(System.out::println);
    }
    private void listarObrasDeAutor(Autor autor) {
        autor.getObras().forEach(livro -> {
            System.out.println(" - " + livro.getTitulo());
        });
    }

    private void autoresCadastrados() {
        List<Autor> autoresCadastrados = repositorio.autoresCadastrados();
        autoresCadastrados.forEach(a -> {
                    System.out.println("Autor(a): " + a.getNome() +
                            "\nAno de Nascimento: " + a.getNascimento() +
                            "\nAno de Falecimento: " + (a.getAnoFalecimento() == 0 ? "Ainda vivo" : a.getAnoFalecimento()) +
                            "\nLivros: ");
                    listarObrasDeAutor(a);
                    System.out.println();
                });
    }

    private void autoresVivosEmDeterminadoAno() {
        System.out.println("Digite o ano desejado: ");
        var ano = leitura.nextInt();
        leitura.nextLine();
        List<Autor> autoresVivos = repositorio.autoresVivosNoAno(ano);

        AtomicBoolean autorEncontrado = new AtomicBoolean(false);

        autoresVivos.forEach(a -> {
            if (ano >= a.getNascimento() && (a.getAnoFalecimento() == 0 || ano <= a.getAnoFalecimento())) {
                System.out.println("Autor(a): " + a.getNome() +
                        "\nAno de Nascimento: " + a.getNascimento() +
                        "\nAno de Falecimento: " + (a.getAnoFalecimento() == 0 ? "Ainda vivo" : a.getAnoFalecimento()) +
                        "\nLivros: ");
                listarObrasDeAutor(a);
                System.out.println();
                autorEncontrado.set(true);
            }
        });
        if (!autorEncontrado.get()) {
            System.out.println("Não há autores cadastrados para o ano digitado!");
         }

    }
}


//    private void listarLivrosPeloIdioma() {
//
//        System.out.println("--> pt - português, ");
//        System.out.println("--> es - espanhol,");
//        System.out.println("--> en - inglês,");
//        System.out.println("--> fr - francês. ");
//        System.out.println("Digite a sigla do idioma desejado: ");
//        var livroIdioma = leitura.nextLine();
//
//        List<Livro> idiomas = repositorio.livrosPorIdioma(livroIdioma);
//
//        listarLivrosCadastrados();
////        idiomas.forEach(a -> {
////            System.out.println(busc);
//
//        });
//    }

//}