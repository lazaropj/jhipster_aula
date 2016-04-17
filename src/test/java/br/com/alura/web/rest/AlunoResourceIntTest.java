package br.com.alura.web.rest;

import br.com.alura.Application;
import br.com.alura.domain.Aluno;
import br.com.alura.repository.AlunoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AlunoResource REST controller.
 *
 * @see AlunoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AlunoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";

    private static final Integer DEFAULT_IDADE = 1;
    private static final Integer UPDATED_IDADE = 2;
    private static final String DEFAULT_ENDERECO = "AAAAA";
    private static final String UPDATED_ENDERECO = "BBBBB";

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private AlunoRepository alunoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAlunoMockMvc;

    private Aluno aluno;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AlunoResource alunoResource = new AlunoResource();
        ReflectionTestUtils.setField(alunoResource, "alunoRepository", alunoRepository);
        this.restAlunoMockMvc = MockMvcBuilders.standaloneSetup(alunoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        aluno = new Aluno();
        aluno.setNome(DEFAULT_NOME);
        aluno.setIdade(DEFAULT_IDADE);
        aluno.setEndereco(DEFAULT_ENDERECO);
        aluno.setDataNascimento(DEFAULT_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    public void createAluno() throws Exception {
        int databaseSizeBeforeCreate = alunoRepository.findAll().size();

        // Create the Aluno

        restAlunoMockMvc.perform(post("/api/alunos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(aluno)))
                .andExpect(status().isCreated());

        // Validate the Aluno in the database
        List<Aluno> alunos = alunoRepository.findAll();
        assertThat(alunos).hasSize(databaseSizeBeforeCreate + 1);
        Aluno testAluno = alunos.get(alunos.size() - 1);
        assertThat(testAluno.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAluno.getIdade()).isEqualTo(DEFAULT_IDADE);
        assertThat(testAluno.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testAluno.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = alunoRepository.findAll().size();
        // set the field null
        aluno.setNome(null);

        // Create the Aluno, which fails.

        restAlunoMockMvc.perform(post("/api/alunos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(aluno)))
                .andExpect(status().isBadRequest());

        List<Aluno> alunos = alunoRepository.findAll();
        assertThat(alunos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = alunoRepository.findAll().size();
        // set the field null
        aluno.setIdade(null);

        // Create the Aluno, which fails.

        restAlunoMockMvc.perform(post("/api/alunos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(aluno)))
                .andExpect(status().isBadRequest());

        List<Aluno> alunos = alunoRepository.findAll();
        assertThat(alunos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataNascimentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = alunoRepository.findAll().size();
        // set the field null
        aluno.setDataNascimento(null);

        // Create the Aluno, which fails.

        restAlunoMockMvc.perform(post("/api/alunos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(aluno)))
                .andExpect(status().isBadRequest());

        List<Aluno> alunos = alunoRepository.findAll();
        assertThat(alunos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAlunos() throws Exception {
        // Initialize the database
        alunoRepository.saveAndFlush(aluno);

        // Get all the alunos
        restAlunoMockMvc.perform(get("/api/alunos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(aluno.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].idade").value(hasItem(DEFAULT_IDADE)))
                .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO.toString())))
                .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())));
    }

    @Test
    @Transactional
    public void getAluno() throws Exception {
        // Initialize the database
        alunoRepository.saveAndFlush(aluno);

        // Get the aluno
        restAlunoMockMvc.perform(get("/api/alunos/{id}", aluno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(aluno.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.idade").value(DEFAULT_IDADE))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO.toString()))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAluno() throws Exception {
        // Get the aluno
        restAlunoMockMvc.perform(get("/api/alunos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAluno() throws Exception {
        // Initialize the database
        alunoRepository.saveAndFlush(aluno);

		int databaseSizeBeforeUpdate = alunoRepository.findAll().size();

        // Update the aluno
        aluno.setNome(UPDATED_NOME);
        aluno.setIdade(UPDATED_IDADE);
        aluno.setEndereco(UPDATED_ENDERECO);
        aluno.setDataNascimento(UPDATED_DATA_NASCIMENTO);

        restAlunoMockMvc.perform(put("/api/alunos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(aluno)))
                .andExpect(status().isOk());

        // Validate the Aluno in the database
        List<Aluno> alunos = alunoRepository.findAll();
        assertThat(alunos).hasSize(databaseSizeBeforeUpdate);
        Aluno testAluno = alunos.get(alunos.size() - 1);
        assertThat(testAluno.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAluno.getIdade()).isEqualTo(UPDATED_IDADE);
        assertThat(testAluno.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testAluno.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    public void deleteAluno() throws Exception {
        // Initialize the database
        alunoRepository.saveAndFlush(aluno);

		int databaseSizeBeforeDelete = alunoRepository.findAll().size();

        // Get the aluno
        restAlunoMockMvc.perform(delete("/api/alunos/{id}", aluno.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Aluno> alunos = alunoRepository.findAll();
        assertThat(alunos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
