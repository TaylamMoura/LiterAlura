package com.alura.literAlura.Service;

import com.alura.literAlura.Model.Autor;
import com.alura.literAlura.Model.Livro;
import com.alura.literAlura.Model.LivroAutor;
import com.alura.literAlura.Repository.AutorRepository;
import com.alura.literAlura.Repository.LivroRepository;
import com.alura.literAlura.Repository.LivroAutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroAutorRepository livroAutorRepository;

    @Transactional
    public Livro salvarLivro(Livro livro) {
        Optional<Livro> livroExistente = livroRepository.findByTitulo(livro.getTitulo());
        if (livroExistente.isPresent()) {
            return livroExistente.get();
        }

        List<Autor> autores = livro.getLivroAutores().stream()
                .map(LivroAutor::getAutor)
                .collect(Collectors.toList());
        for (Autor autor : autores) {
            if (autor.getId() == null) {
                autorRepository.save(autor);
            }
        }

        Livro livroSalvo = livroRepository.save(livro);

        for (LivroAutor livroAutor : livro.getLivroAutores()) {
            livroAutor.setLivro(livroSalvo);
            livroAutorRepository.save(livroAutor);
        }
        return livroSalvo;
    }

    @Transactional(readOnly = true)
    public List<Livro> buscarLivrosSalvos() {
        List<Livro> livros = livroRepository.findAll();
        // Inicializa todas as coleções necessárias
        livros.forEach(livro -> {
            livro.getLivroAutores().forEach(livroAutor -> {
                livroAutor.getAutor().getNome();
            });
            livro.getIdioma().size(); // Inicializa a coleção idioma
        });
        return livros;
    }
}
