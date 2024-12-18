package com.alura.literAlura.Repository;

import com.alura.literAlura.Model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a")
    List<Autor> autoresCadastrados();

    @Query("SELECT a FROM Autor a WHERE a.nascimento <= :ano AND (a.anoFalecimento >= :ano OR a.anoFalecimento = 0)")
    List<Autor> autoresVivosNoAno(@Param("ano") int ano);
}
