package br.com.alura.repository;

import br.com.alura.domain.Aluno;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Aluno entity.
 */
public interface AlunoRepository extends JpaRepository<Aluno,Long> {

}
