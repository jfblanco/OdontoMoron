package ar.com.odontomoron.web.rest;

import ar.com.odontomoron.Application;
import ar.com.odontomoron.domain.CondicionFacturacion;
import ar.com.odontomoron.repository.CondicionFacturacionRepository;

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
 * Test class for the CondicionFacturacionResource REST controller.
 *
 * @see CondicionFacturacionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CondicionFacturacionResourceTest {

    private static final String DEFAULT_DESCRIPCION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPCION = "UPDATED_TEXT";

    @Inject
    private CondicionFacturacionRepository condicionFacturacionRepository;

    private MockMvc restCondicionFacturacionMockMvc;

    private CondicionFacturacion condicionFacturacion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CondicionFacturacionResource condicionFacturacionResource = new CondicionFacturacionResource();
        ReflectionTestUtils.setField(condicionFacturacionResource, "condicionFacturacionRepository", condicionFacturacionRepository);
        this.restCondicionFacturacionMockMvc = MockMvcBuilders.standaloneSetup(condicionFacturacionResource).build();
    }

    @Before
    public void initTest() {
        condicionFacturacion = new CondicionFacturacion();
        condicionFacturacion.setDescripcion(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createCondicionFacturacion() throws Exception {
        int databaseSizeBeforeCreate = condicionFacturacionRepository.findAll().size();

        // Create the CondicionFacturacion
        restCondicionFacturacionMockMvc.perform(post("/api/condicionFacturacions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(condicionFacturacion)))
                .andExpect(status().isCreated());

        // Validate the CondicionFacturacion in the database
        List<CondicionFacturacion> condicionFacturacions = condicionFacturacionRepository.findAll();
        assertThat(condicionFacturacions).hasSize(databaseSizeBeforeCreate + 1);
        CondicionFacturacion testCondicionFacturacion = condicionFacturacions.get(condicionFacturacions.size() - 1);
        assertThat(testCondicionFacturacion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllCondicionFacturacions() throws Exception {
        // Initialize the database
        condicionFacturacionRepository.saveAndFlush(condicionFacturacion);

        // Get all the condicionFacturacions
        restCondicionFacturacionMockMvc.perform(get("/api/condicionFacturacions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(condicionFacturacion.getId().intValue())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getCondicionFacturacion() throws Exception {
        // Initialize the database
        condicionFacturacionRepository.saveAndFlush(condicionFacturacion);

        // Get the condicionFacturacion
        restCondicionFacturacionMockMvc.perform(get("/api/condicionFacturacions/{id}", condicionFacturacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(condicionFacturacion.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCondicionFacturacion() throws Exception {
        // Get the condicionFacturacion
        restCondicionFacturacionMockMvc.perform(get("/api/condicionFacturacions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCondicionFacturacion() throws Exception {
        // Initialize the database
        condicionFacturacionRepository.saveAndFlush(condicionFacturacion);

		int databaseSizeBeforeUpdate = condicionFacturacionRepository.findAll().size();

        // Update the condicionFacturacion
        condicionFacturacion.setDescripcion(UPDATED_DESCRIPCION);
        restCondicionFacturacionMockMvc.perform(put("/api/condicionFacturacions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(condicionFacturacion)))
                .andExpect(status().isOk());

        // Validate the CondicionFacturacion in the database
        List<CondicionFacturacion> condicionFacturacions = condicionFacturacionRepository.findAll();
        assertThat(condicionFacturacions).hasSize(databaseSizeBeforeUpdate);
        CondicionFacturacion testCondicionFacturacion = condicionFacturacions.get(condicionFacturacions.size() - 1);
        assertThat(testCondicionFacturacion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void deleteCondicionFacturacion() throws Exception {
        // Initialize the database
        condicionFacturacionRepository.saveAndFlush(condicionFacturacion);

		int databaseSizeBeforeDelete = condicionFacturacionRepository.findAll().size();

        // Get the condicionFacturacion
        restCondicionFacturacionMockMvc.perform(delete("/api/condicionFacturacions/{id}", condicionFacturacion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CondicionFacturacion> condicionFacturacions = condicionFacturacionRepository.findAll();
        assertThat(condicionFacturacions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
