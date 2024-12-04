package com.alura.literAlura.Repository;

import com.alura.literAlura.Model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface LivroRepository  extends JpaRepository<Livro, Long> {
    Optional<Livro> findByTitulo(String titulo); //mudar nome, esta no livroservice
}
