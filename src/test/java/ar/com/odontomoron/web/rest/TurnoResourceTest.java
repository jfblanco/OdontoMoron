package ar.com.odontomoron.web.rest;

import ar.com.odontomoron.Application;
import ar.com.odontomoron.domain.Turno;
import ar.com.odontomoron.repository.TurnoRepository;

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
 * Test class for the TurnoResource REST controller.
 *
 * @see TurnoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TurnoResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_FECHA = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_FECHA = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_FECHA_STR = dateTimeFormatter.print(DEFAULT_FECHA);
    private static final String DEFAULT_OBSERVACION = "SAMPLE_TEXT";
    private static final String UPDATED_OBSERVACION = "UPDATED_TEXT";

    private static final Boolean DEFAULT_SOBRETURNO = false;
    private static final Boolean UPDATED_SOBRETURNO = true;

    @Inject
    private TurnoRepository turnoRepository;

    private MockMvc restTurnoMockMvc;

    private Turno turno;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TurnoResource turnoResource = new TurnoResource();
        ReflectionTestUtils.setField(turnoResource, "turnoRepository", turnoRepository);
        this.restTurnoMockMvc = MockMvcBuilders.standaloneSetup(turnoResource).build();
    }

    @Before
    public void initTest() {
        turno = new Turno();
        turno.setFecha(DEFAULT_FECHA);
        turno.setObservacion(DEFAULT_OBSERVACION);
        turno.setSobreturno(DEFAULT_SOBRETURNO);
    }

    @Test
    @Transactional
    public void createTurno() throws Exception {
        int databaseSizeBeforeCreate = turnoRepository.findAll().size();

        // Create the Turno
        restTurnoMockMvc.perform(post("/api/turnos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(turno)))
                .andExpect(status().isCreated());

        // Validate the Turno in the database
        List<Turno> turnos = turnoRepository.findAll();
        assertThat(turnos).hasSize(databaseSizeBeforeCreate + 1);
        Turno testTurno = turnos.get(turnos.size() - 1);
        assertThat(testTurno.getFecha().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_FECHA);
        assertThat(testTurno.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
        assertThat(testTurno.getSobreturno()).isEqualTo(DEFAULT_SOBRETURNO);
    }

    @Test
    @Transactional
    public void getAllTurnos() throws Exception {
        // Initialize the database
        turnoRepository.saveAndFlush(turno);

        // Get all the turnos
        restTurnoMockMvc.perform(get("/api/turnos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(turno.getId().intValue())))
                .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA_STR)))
                .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION.toString())))
                .andExpect(jsonPath("$.[*].sobreturno").value(hasItem(DEFAULT_SOBRETURNO.booleanValue())));
    }

    @Test
    @Transactional
    public void getTurno() throws Exception {
        // Initialize the database
        turnoRepository.saveAndFlush(turno);

        // Get the turno
        restTurnoMockMvc.perform(get("/api/turnos/{id}", turno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(turno.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA_STR))
            .andExpect(jsonPath("$.observacion").value(DEFAULT_OBSERVACION.toString()))
            .andExpect(jsonPath("$.sobreturno").value(DEFAULT_SOBRETURNO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTurno() throws Exception {
        // Get the turno
        restTurnoMockMvc.perform(get("/api/turnos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTurno() throws Exception {
        // Initialize the database
        turnoRepository.saveAndFlush(turno);

		int databaseSizeBeforeUpdate = turnoRepository.findAll().size();

        // Update the turno
        turno.setFecha(UPDATED_FECHA);
        turno.setObservacion(UPDATED_OBSERVACION);
        turno.setSobreturno(UPDATED_SOBRETURNO);
        restTurnoMockMvc.perform(put("/api/turnos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(turno)))
                .andExpect(status().isOk());

        // Validate the Turno in the database
        List<Turno> turnos = turnoRepository.findAll();
        assertThat(turnos).hasSize(databaseSizeBeforeUpdate);
        Turno testTurno = turnos.get(turnos.size() - 1);
        assertThat(testTurno.getFecha().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_FECHA);
        assertThat(testTurno.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
        assertThat(testTurno.getSobreturno()).isEqualTo(UPDATED_SOBRETURNO);
    }

    @Test
    @Transactional
    public void deleteTurno() throws Exception {
        // Initialize the database
        turnoRepository.saveAndFlush(turno);

		int databaseSizeBeforeDelete = turnoRepository.findAll().size();

        // Get the turno
        restTurnoMockMvc.perform(delete("/api/turnos/{id}", turno.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Turno> turnos = turnoRepository.findAll();
        assertThat(turnos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
