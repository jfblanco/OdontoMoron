package ar.com.odontomoron.web.rest;

import ar.com.odontomoron.Application;
import ar.com.odontomoron.domain.Prepaga;
import ar.com.odontomoron.repository.PrepagaRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PrepagaResource REST controller.
 *
 * @see PrepagaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PrepagaResourceTest {

    private static final String DEFAULT_NUMERO_AFILIADO = "SAMPLE_TEXT";
    private static final String UPDATED_NUMERO_AFILIADO = "UPDATED_TEXT";

    @Inject
    private PrepagaRepository prepagaRepository;

    private MockMvc restPrepagaMockMvc;

    private Prepaga prepaga;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrepagaResource prepagaResource = new PrepagaResource();
        ReflectionTestUtils.setField(prepagaResource, "prepagaRepository", prepagaRepository);
        this.restPrepagaMockMvc = MockMvcBuilders.standaloneSetup(prepagaResource).build();
    }

    @Before
    public void initTest() {
        prepaga = new Prepaga();
        prepaga.setNumeroAfiliado(DEFAULT_NUMERO_AFILIADO);
    }

    @Test
    @Transactional
    public void createPrepaga() throws Exception {
        int databaseSizeBeforeCreate = prepagaRepository.findAll().size();

        // Create the Prepaga
        restPrepagaMockMvc.perform(post("/api/prepagas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prepaga)))
                .andExpect(status().isCreated());

        // Validate the Prepaga in the database
        List<Prepaga> prepagas = prepagaRepository.findAll();
        assertThat(prepagas).hasSize(databaseSizeBeforeCreate + 1);
        Prepaga testPrepaga = prepagas.get(prepagas.size() - 1);
        assertThat(testPrepaga.getNumeroAfiliado()).isEqualTo(DEFAULT_NUMERO_AFILIADO);
    }

    @Test
    @Transactional
    public void getAllPrepagas() throws Exception {
        // Initialize the database
        prepagaRepository.saveAndFlush(prepaga);

        // Get all the prepagas
        restPrepagaMockMvc.perform(get("/api/prepagas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(prepaga.getId().intValue())))
                .andExpect(jsonPath("$.[*].numeroAfiliado").value(hasItem(DEFAULT_NUMERO_AFILIADO.toString())));
    }

    @Test
    @Transactional
    public void getPrepaga() throws Exception {
        // Initialize the database
        prepagaRepository.saveAndFlush(prepaga);

        // Get the prepaga
        restPrepagaMockMvc.perform(get("/api/prepagas/{id}", prepaga.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(prepaga.getId().intValue()))
            .andExpect(jsonPath("$.numeroAfiliado").value(DEFAULT_NUMERO_AFILIADO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrepaga() throws Exception {
        // Get the prepaga
        restPrepagaMockMvc.perform(get("/api/prepagas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrepaga() throws Exception {
        // Initialize the database
        prepagaRepository.saveAndFlush(prepaga);

		int databaseSizeBeforeUpdate = prepagaRepository.findAll().size();

        // Update the prepaga
        prepaga.setNumeroAfiliado(UPDATED_NUMERO_AFILIADO);
        restPrepagaMockMvc.perform(put("/api/prepagas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prepaga)))
                .andExpect(status().isOk());

        // Validate the Prepaga in the database
        List<Prepaga> prepagas = prepagaRepository.findAll();
        assertThat(prepagas).hasSize(databaseSizeBeforeUpdate);
        Prepaga testPrepaga = prepagas.get(prepagas.size() - 1);
        assertThat(testPrepaga.getNumeroAfiliado()).isEqualTo(UPDATED_NUMERO_AFILIADO);
    }

    @Test
    @Transactional
    public void deletePrepaga() throws Exception {
        // Initialize the database
        prepagaRepository.saveAndFlush(prepaga);

		int databaseSizeBeforeDelete = prepagaRepository.findAll().size();

        // Get the prepaga
        restPrepagaMockMvc.perform(delete("/api/prepagas/{id}", prepaga.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Prepaga> prepagas = prepagaRepository.findAll();
        assertThat(prepagas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
