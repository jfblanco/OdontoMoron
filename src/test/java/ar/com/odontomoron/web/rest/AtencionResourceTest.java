package ar.com.odontomoron.web.rest;

import ar.com.odontomoron.Application;
import ar.com.odontomoron.domain.Atencion;
import ar.com.odontomoron.repository.AtencionRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AtencionResource REST controller.
 *
 * @see AtencionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AtencionResourceTest {


    private static final BigDecimal DEFAULT_INGRESO = new BigDecimal(0);
    private static final BigDecimal UPDATED_INGRESO = new BigDecimal(1);

    private static final BigDecimal DEFAULT_EGRESO = new BigDecimal(0);
    private static final BigDecimal UPDATED_EGRESO = new BigDecimal(1);

    @Inject
    private AtencionRepository atencionRepository;

    private MockMvc restAtencionMockMvc;

    private Atencion atencion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AtencionResource atencionResource = new AtencionResource();
        ReflectionTestUtils.setField(atencionResource, "atencionRepository", atencionRepository);
        this.restAtencionMockMvc = MockMvcBuilders.standaloneSetup(atencionResource).build();
    }

    @Before
    public void initTest() {
        atencion = new Atencion();
        atencion.setIngreso(DEFAULT_INGRESO);
        atencion.setEgreso(DEFAULT_EGRESO);
    }

    @Test
    @Transactional
    public void createAtencion() throws Exception {
        int databaseSizeBeforeCreate = atencionRepository.findAll().size();

        // Create the Atencion
        restAtencionMockMvc.perform(post("/api/atencions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(atencion)))
                .andExpect(status().isCreated());

        // Validate the Atencion in the database
        List<Atencion> atencions = atencionRepository.findAll();
        assertThat(atencions).hasSize(databaseSizeBeforeCreate + 1);
        Atencion testAtencion = atencions.get(atencions.size() - 1);
        assertThat(testAtencion.getIngreso()).isEqualTo(DEFAULT_INGRESO);
        assertThat(testAtencion.getEgreso()).isEqualTo(DEFAULT_EGRESO);
    }

    @Test
    @Transactional
    public void getAllAtencions() throws Exception {
        // Initialize the database
        atencionRepository.saveAndFlush(atencion);

        // Get all the atencions
        restAtencionMockMvc.perform(get("/api/atencions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(atencion.getId().intValue())))
                .andExpect(jsonPath("$.[*].ingreso").value(hasItem(DEFAULT_INGRESO.intValue())))
                .andExpect(jsonPath("$.[*].egreso").value(hasItem(DEFAULT_EGRESO.intValue())));
    }

    @Test
    @Transactional
    public void getAtencion() throws Exception {
        // Initialize the database
        atencionRepository.saveAndFlush(atencion);

        // Get the atencion
        restAtencionMockMvc.perform(get("/api/atencions/{id}", atencion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(atencion.getId().intValue()))
            .andExpect(jsonPath("$.ingreso").value(DEFAULT_INGRESO.intValue()))
            .andExpect(jsonPath("$.egreso").value(DEFAULT_EGRESO.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAtencion() throws Exception {
        // Get the atencion
        restAtencionMockMvc.perform(get("/api/atencions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAtencion() throws Exception {
        // Initialize the database
        atencionRepository.saveAndFlush(atencion);

		int databaseSizeBeforeUpdate = atencionRepository.findAll().size();

        // Update the atencion
        atencion.setIngreso(UPDATED_INGRESO);
        atencion.setEgreso(UPDATED_EGRESO);
        restAtencionMockMvc.perform(put("/api/atencions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(atencion)))
                .andExpect(status().isOk());

        // Validate the Atencion in the database
        List<Atencion> atencions = atencionRepository.findAll();
        assertThat(atencions).hasSize(databaseSizeBeforeUpdate);
        Atencion testAtencion = atencions.get(atencions.size() - 1);
        assertThat(testAtencion.getIngreso()).isEqualTo(UPDATED_INGRESO);
        assertThat(testAtencion.getEgreso()).isEqualTo(UPDATED_EGRESO);
    }

    @Test
    @Transactional
    public void deleteAtencion() throws Exception {
        // Initialize the database
        atencionRepository.saveAndFlush(atencion);

		int databaseSizeBeforeDelete = atencionRepository.findAll().size();

        // Get the atencion
        restAtencionMockMvc.perform(delete("/api/atencions/{id}", atencion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Atencion> atencions = atencionRepository.findAll();
        assertThat(atencions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
