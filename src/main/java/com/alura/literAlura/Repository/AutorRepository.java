package com.alura.literAlura.Repository;

import com.alura.literAlura.Model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
}
