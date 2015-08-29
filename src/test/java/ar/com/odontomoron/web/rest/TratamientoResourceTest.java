package ar.com.odontomoron.web.rest;

import ar.com.odontomoron.Application;
import ar.com.odontomoron.domain.Tratamiento;
import ar.com.odontomoron.repository.TratamientoRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TratamientoResource REST controller.
 *
 * @see TratamientoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TratamientoResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_FECHA = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_FECHA = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_FECHA_STR = dateTimeFormatter.print(DEFAULT_FECHA);

    private static final Boolean DEFAULT_CONFORMIDAD = false;
    private static final Boolean UPDATED_CONFORMIDAD = true;
    private static final String DEFAULT_CARA = "SAMPLE_TEXT";
    private static final String UPDATED_CARA = "UPDATED_TEXT";
    private static final String DEFAULT_SECTOR = "SAMPLE_TEXT";
    private static final String UPDATED_SECTOR = "UPDATED_TEXT";
    private static final String DEFAULT_PIEZA = "SAMPLE_TEXT";
    private static final String UPDATED_PIEZA = "UPDATED_TEXT";

    @Inject
    private TratamientoRepository tratamientoRepository;

    private MockMvc restTratamientoMockMvc;

    private Tratamiento tratamiento;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TratamientoResource tratamientoResource = new TratamientoResource();
        ReflectionTestUtils.setField(tratamientoResource, "tratamientoRepository", tratamientoRepository);
        this.restTratamientoMockMvc = MockMvcBuilders.standaloneSetup(tratamientoResource).build();
    }

    @Before
    public void initTest() {
        tratamiento = new Tratamiento();
        tratamiento.setFecha(DEFAULT_FECHA);
        tratamiento.setConformidad(DEFAULT_CONFORMIDAD);
        tratamiento.setCara(DEFAULT_CARA);
        tratamiento.setSector(DEFAULT_SECTOR);
        tratamiento.setPieza(DEFAULT_PIEZA);
    }

    @Test
    @Transactional
    public void createTratamiento() throws Exception {
        int databaseSizeBeforeCreate = tratamientoRepository.findAll().size();

        // Create the Tratamiento
        restTratamientoMockMvc.perform(post("/api/tratamientos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tratamiento)))
                .andExpect(status().isCreated());

        // Validate the Tratamiento in the database
        List<Tratamiento> tratamientos = tratamientoRepository.findAll();
        assertThat(tratamientos).hasSize(databaseSizeBeforeCreate + 1);
        Tratamiento testTratamiento = tratamientos.get(tratamientos.size() - 1);
        assertThat(testTratamiento.getFecha().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_FECHA);
        assertThat(testTratamiento.getConformidad()).isEqualTo(DEFAULT_CONFORMIDAD);
        assertThat(testTratamiento.getCara()).isEqualTo(DEFAULT_CARA);
        assertThat(testTratamiento.getSector()).isEqualTo(DEFAULT_SECTOR);
        assertThat(testTratamiento.getPieza()).isEqualTo(DEFAULT_PIEZA);
    }

    @Test
    @Transactional
    public void getAllTratamientos() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientos
        restTratamientoMockMvc.perform(get("/api/tratamientos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tratamiento.getId().intValue())))
                .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA_STR)))
                .andExpect(jsonPath("$.[*].conformidad").value(hasItem(DEFAULT_CONFORMIDAD.booleanValue())))
                .andExpect(jsonPath("$.[*].cara").value(hasItem(DEFAULT_CARA.toString())))
                .andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR.toString())))
                .andExpect(jsonPath("$.[*].pieza").value(hasItem(DEFAULT_PIEZA.toString())));
    }

    @Test
    @Transactional
    public void getTratamiento() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);

        // Get the tratamiento
        restTratamientoMockMvc.perform(get("/api/tratamientos/{id}", tratamiento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tratamiento.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA_STR))
            .andExpect(jsonPath("$.conformidad").value(DEFAULT_CONFORMIDAD.booleanValue()))
            .andExpect(jsonPath("$.cara").value(DEFAULT_CARA.toString()))
            .andExpect(jsonPath("$.sector").value(DEFAULT_SECTOR.toString()))
            .andExpect(jsonPath("$.pieza").value(DEFAULT_PIEZA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTratamiento() throws Exception {
        // Get the tratamiento
        restTratamientoMockMvc.perform(get("/api/tratamientos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTratamiento() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);

		int databaseSizeBeforeUpdate = tratamientoRepository.findAll().size();

        // Update the tratamiento
        tratamiento.setFecha(UPDATED_FECHA);
        tratamiento.setConformidad(UPDATED_CONFORMIDAD);
        tratamiento.setCara(UPDATED_CARA);
        tratamiento.setSector(UPDATED_SECTOR);
        tratamiento.setPieza(UPDATED_PIEZA);
        restTratamientoMockMvc.perform(put("/api/tratamientos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tratamiento)))
                .andExpect(status().isOk());

        // Validate the Tratamiento in the database
        List<Tratamiento> tratamientos = tratamientoRepository.findAll();
        assertThat(tratamientos).hasSize(databaseSizeBeforeUpdate);
        Tratamiento testTratamiento = tratamientos.get(tratamientos.size() - 1);
        assertThat(testTratamiento.getFecha().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_FECHA);
        assertThat(testTratamiento.getConformidad()).isEqualTo(UPDATED_CONFORMIDAD);
        assertThat(testTratamiento.getCara()).isEqualTo(UPDATED_CARA);
        assertThat(testTratamiento.getSector()).isEqualTo(UPDATED_SECTOR);
        assertThat(testTratamiento.getPieza()).isEqualTo(UPDATED_PIEZA);
    }

    @Test
    @Transactional
    public void deleteTratamiento() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);

		int databaseSizeBeforeDelete = tratamientoRepository.findAll().size();

        // Get the tratamiento
        restTratamientoMockMvc.perform(delete("/api/tratamientos/{id}", tratamiento.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Tratamiento> tratamientos = tratamientoRepository.findAll();
        assertThat(tratamientos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
