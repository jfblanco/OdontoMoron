package ar.com.odontomoron.web.rest;

import ar.com.odontomoron.Application;
import ar.com.odontomoron.domain.Odontologo;
import ar.com.odontomoron.repository.OdontologoRepository;

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
 * Test class for the OdontologoResource REST controller.
 *
 * @see OdontologoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OdontologoResourceTest {

    private static final String DEFAULT_NOMBRE = "SAMPLE_TEXT";
    private static final String UPDATED_NOMBRE = "UPDATED_TEXT";
    private static final String DEFAULT_APELLIDO = "SAMPLE_TEXT";
    private static final String UPDATED_APELLIDO = "UPDATED_TEXT";

    @Inject
    private OdontologoRepository odontologoRepository;

    private MockMvc restOdontologoMockMvc;

    private Odontologo odontologo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OdontologoResource odontologoResource = new OdontologoResource();
        ReflectionTestUtils.setField(odontologoResource, "odontologoRepository", odontologoRepository);
        this.restOdontologoMockMvc = MockMvcBuilders.standaloneSetup(odontologoResource).build();
    }

    @Before
    public void initTest() {
        odontologo = new Odontologo();
        odontologo.setNombre(DEFAULT_NOMBRE);
        odontologo.setApellido(DEFAULT_APELLIDO);
    }

    @Test
    @Transactional
    public void createOdontologo() throws Exception {
        int databaseSizeBeforeCreate = odontologoRepository.findAll().size();

        // Create the Odontologo
        restOdontologoMockMvc.perform(post("/api/odontologos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(odontologo)))
                .andExpect(status().isCreated());

        // Validate the Odontologo in the database
        List<Odontologo> odontologos = odontologoRepository.findAll();
        assertThat(odontologos).hasSize(databaseSizeBeforeCreate + 1);
        Odontologo testOdontologo = odontologos.get(odontologos.size() - 1);
        assertThat(testOdontologo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testOdontologo.getApellido()).isEqualTo(DEFAULT_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllOdontologos() throws Exception {
        // Initialize the database
        odontologoRepository.saveAndFlush(odontologo);

        // Get all the odontologos
        restOdontologoMockMvc.perform(get("/api/odontologos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(odontologo.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO.toString())));
    }

    @Test
    @Transactional
    public void getOdontologo() throws Exception {
        // Initialize the database
        odontologoRepository.saveAndFlush(odontologo);

        // Get the odontologo
        restOdontologoMockMvc.perform(get("/api/odontologos/{id}", odontologo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(odontologo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOdontologo() throws Exception {
        // Get the odontologo
        restOdontologoMockMvc.perform(get("/api/odontologos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOdontologo() throws Exception {
        // Initialize the database
        odontologoRepository.saveAndFlush(odontologo);

		int databaseSizeBeforeUpdate = odontologoRepository.findAll().size();

        // Update the odontologo
        odontologo.setNombre(UPDATED_NOMBRE);
        odontologo.setApellido(UPDATED_APELLIDO);
        restOdontologoMockMvc.perform(put("/api/odontologos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(odontologo)))
                .andExpect(status().isOk());

        // Validate the Odontologo in the database
        List<Odontologo> odontologos = odontologoRepository.findAll();
        assertThat(odontologos).hasSize(databaseSizeBeforeUpdate);
        Odontologo testOdontologo = odontologos.get(odontologos.size() - 1);
        assertThat(testOdontologo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testOdontologo.getApellido()).isEqualTo(UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    public void deleteOdontologo() throws Exception {
        // Initialize the database
        odontologoRepository.saveAndFlush(odontologo);

		int databaseSizeBeforeDelete = odontologoRepository.findAll().size();

        // Get the odontologo
        restOdontologoMockMvc.perform(delete("/api/odontologos/{id}", odontologo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Odontologo> odontologos = odontologoRepository.findAll();
        assertThat(odontologos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
