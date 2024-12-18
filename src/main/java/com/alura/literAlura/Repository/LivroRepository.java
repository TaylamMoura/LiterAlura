package com.alura.literAlura.Repository;

import com.alura.literAlura.Model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface LivroRepository  extends JpaRepository<Livro, Long> {

    Optional<Livro> findByTitulo(String titulo);

    @Query("SELECT l FROM Livro l WHERE :livroIdioma MEMBER OF l.idioma")
    List<Livro> livrosPorIdioma(@Param("livroIdioma") String idioma);
}
