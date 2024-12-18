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
        System.out.println("\n*BIBLIOTECA LITERALURA*");
        System.out.println("         - MENU - ");
        System.out.println("1 - buscar livro pelo TÍTULO;");
        System.out.println("2 - listar LIVROS registrados;");
        System.out.println("3 - listar AUTORES registrados;");
        System.out.println("4 - listar autores vivos em determinado ANO;");
        System.out.println("5 - listar livros em um determinado IDIOMA;");
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
                case 5:
                    listarLivrosPeloIdioma();
                    break;
                case 0:
                    System.out.println("\nObrigado por usar nossos Serviços!");
                    System.out.println("Volte Sempre!\n");
                    return;
                default:
                    System.out.println("\nOpção inválida.Digite novamente!\n");
            }
        }
    }


    public void buscarLivroAPI() {
        System.out.println("Digite o título do livro: ");
        var nomeLivro = leitura.nextLine().toLowerCase();
        var url = ENDERECO + nomeLivro.replace(" ", "%20");
        var json = conexao.obterDados(url);

        RespostaAPI respostaAPI = converteDados.obterDados(json, RespostaAPI.class);

        Optional<DadosLivros> livroEncontrado = respostaAPI.getResultados()
                .stream().filter(dadosLivros -> dadosLivros.titulo().equalsIgnoreCase(nomeLivro))
                .findFirst();

        if (livroEncontrado.isPresent()) {
            DadosLivros dadosLivro = livroEncontrado.get();
            Livro livro = new Livro(dadosLivro);

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
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("\nErro ao salvar livro: " + e.getMessage() + "\n");
            }
        } else {
            System.out.println("\nLivro não encontrado.\n");
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
                    System.out.println("------------------------------" +
                            "\nAutor(a): " + a.getNome() +
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
                System.out.println("------------------------------" +
                        "\nAutor(a): " + a.getNome() +
                        "\nAno de Nascimento: " + a.getNascimento() +
                        "\nAno de Falecimento: " + (a.getAnoFalecimento() == 0 ? "Ainda vivo" : a.getAnoFalecimento()) +
                        "\nLivros: ");
                listarObrasDeAutor(a);
                System.out.println();
                autorEncontrado.set(true);
            }
        });

        if (!autorEncontrado.get()) {
            System.out.println("Não há autores cadastrados para o ano digitado!\n");
         }
    }

    private void listarLivrosPeloIdioma() {
        String menuIdioma = """
                --> pt - português,
                --> es - espanhol,
                --> en - inglês,
                --> fr - francês.
                
                Digite a sigla do idioma desejado:
                """;

        System.out.println(menuIdioma);
        var livroIdioma = leitura.nextLine().toLowerCase();

        if(livroIdioma.equals("pt") || livroIdioma.equals("es") || livroIdioma.equals("en") || livroIdioma.equals("fr")){
            List<Livro> idiomas = livroService.buscarLivrosPorIdioma(livroIdioma);

            if (!idiomas.isEmpty()) {
                idiomas.forEach(i -> {
                    String autor = i.getLivroAutores().get(0).getAutor().getNome() ;
                    System.out.println("------------------------------" +
                            "\nLIVROS\n" +
                            "\nTítulo: " + i.getTitulo() +
                            "\nAutor(a): " + autor +
                            "\nIdioma: " + i.getIdioma() +
                             "\nNº de Downloads: " + i.getDownloads() + "\n");
                });
            } else {
                System.out.println("Não há livros cadastrados para esse idioma: " + livroIdioma);
            }
        }else {
            System.out.println("Sigla inválida. Digite novamente!");
        }
    }
}
