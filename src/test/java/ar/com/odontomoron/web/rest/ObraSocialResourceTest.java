package ar.com.odontomoron.web.rest;

import ar.com.odontomoron.Application;
import ar.com.odontomoron.domain.ObraSocial;
import ar.com.odontomoron.repository.ObraSocialRepository;

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
 * Test class for the ObraSocialResource REST controller.
 *
 * @see ObraSocialResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ObraSocialResourceTest {

    private static final String DEFAULT_NOMBRE = "SAMPLE_TEXT";
    private static final String UPDATED_NOMBRE = "UPDATED_TEXT";

    @Inject
    private ObraSocialRepository obraSocialRepository;

    private MockMvc restObraSocialMockMvc;

    private ObraSocial obraSocial;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ObraSocialResource obraSocialResource = new ObraSocialResource();
        ReflectionTestUtils.setField(obraSocialResource, "obraSocialRepository", obraSocialRepository);
        this.restObraSocialMockMvc = MockMvcBuilders.standaloneSetup(obraSocialResource).build();
    }

    @Before
    public void initTest() {
        obraSocial = new ObraSocial();
        obraSocial.setNombre(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createObraSocial() throws Exception {
        int databaseSizeBeforeCreate = obraSocialRepository.findAll().size();

        // Create the ObraSocial
        restObraSocialMockMvc.perform(post("/api/obraSocials")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(obraSocial)))
                .andExpect(status().isCreated());

        // Validate the ObraSocial in the database
        List<ObraSocial> obraSocials = obraSocialRepository.findAll();
        assertThat(obraSocials).hasSize(databaseSizeBeforeCreate + 1);
        ObraSocial testObraSocial = obraSocials.get(obraSocials.size() - 1);
        assertThat(testObraSocial.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllObraSocials() throws Exception {
        // Initialize the database
        obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocials
        restObraSocialMockMvc.perform(get("/api/obraSocials"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(obraSocial.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void getObraSocial() throws Exception {
        // Initialize the database
        obraSocialRepository.saveAndFlush(obraSocial);

        // Get the obraSocial
        restObraSocialMockMvc.perform(get("/api/obraSocials/{id}", obraSocial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(obraSocial.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingObraSocial() throws Exception {
        // Get the obraSocial
        restObraSocialMockMvc.perform(get("/api/obraSocials/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateObraSocial() throws Exception {
        // Initialize the database
        obraSocialRepository.saveAndFlush(obraSocial);

		int databaseSizeBeforeUpdate = obraSocialRepository.findAll().size();

        // Update the obraSocial
        obraSocial.setNombre(UPDATED_NOMBRE);
        restObraSocialMockMvc.perform(put("/api/obraSocials")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(obraSocial)))
                .andExpect(status().isOk());

        // Validate the ObraSocial in the database
        List<ObraSocial> obraSocials = obraSocialRepository.findAll();
        assertThat(obraSocials).hasSize(databaseSizeBeforeUpdate);
        ObraSocial testObraSocial = obraSocials.get(obraSocials.size() - 1);
        assertThat(testObraSocial.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void deleteObraSocial() throws Exception {
        // Initialize the database
        obraSocialRepository.saveAndFlush(obraSocial);

		int databaseSizeBeforeDelete = obraSocialRepository.findAll().size();

        // Get the obraSocial
        restObraSocialMockMvc.perform(delete("/api/obraSocials/{id}", obraSocial.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ObraSocial> obraSocials = obraSocialRepository.findAll();
        assertThat(obraSocials).hasSize(databaseSizeBeforeDelete - 1);
    }
}
