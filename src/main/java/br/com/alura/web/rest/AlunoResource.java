package br.com.alura.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.alura.domain.Aluno;
import br.com.alura.repository.AlunoRepository;
import br.com.alura.web.rest.util.HeaderUtil;
import br.com.alura.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Aluno.
 */
@RestController
@RequestMapping("/api")
public class AlunoResource {

    private final Logger log = LoggerFactory.getLogger(AlunoResource.class);
        
    @Inject
    private AlunoRepository alunoRepository;
    
    /**
     * POST  /alunos -> Create a new aluno.
     */
    @RequestMapping(value = "/alunos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Aluno> createAluno(@Valid @RequestBody Aluno aluno) throws URISyntaxException {
        log.debug("REST request to save Aluno : {}", aluno);
        if (aluno.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("aluno", "idexists", "A new aluno cannot already have an ID")).body(null);
        }
        Aluno result = alunoRepository.save(aluno);
        return ResponseEntity.created(new URI("/api/alunos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("aluno", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /alunos -> Updates an existing aluno.
     */
    @RequestMapping(value = "/alunos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Aluno> updateAluno(@Valid @RequestBody Aluno aluno) throws URISyntaxException {
        log.debug("REST request to update Aluno : {}", aluno);
        if (aluno.getId() == null) {
            return createAluno(aluno);
        }
        Aluno result = alunoRepository.save(aluno);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("aluno", aluno.getId().toString()))
            .body(result);
    }

    /**
     * GET  /alunos -> get all the alunos.
     */
    @RequestMapping(value = "/alunos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Aluno>> getAllAlunos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Alunos");
        Page<Aluno> page = alunoRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/alunos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /alunos/:id -> get the "id" aluno.
     */
    @RequestMapping(value = "/alunos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Aluno> getAluno(@PathVariable Long id) {
        log.debug("REST request to get Aluno : {}", id);
        Aluno aluno = alunoRepository.findOne(id);
        return Optional.ofNullable(aluno)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /alunos/:id -> delete the "id" aluno.
     */
    @RequestMapping(value = "/alunos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAluno(@PathVariable Long id) {
        log.debug("REST request to delete Aluno : {}", id);
        alunoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("aluno", id.toString())).build();
    }
}
