package com.alura.literAlura.Repository;

import com.alura.literAlura.Model.LivroAutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroAutorRepository extends JpaRepository<LivroAutor, Long> {
}
