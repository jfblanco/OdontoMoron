package ar.com.odontomoron.web.rest;

import ar.com.odontomoron.Application;
import ar.com.odontomoron.domain.Paciente;
import ar.com.odontomoron.repository.PacienteRepository;

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
 * Test class for the PacienteResource REST controller.
 *
 * @see PacienteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PacienteResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_NUMERO_ASOCIADO = "SAMPLE_TEXT";
    private static final String UPDATED_NUMERO_ASOCIADO = "UPDATED_TEXT";
    private static final String DEFAULT_APELLIDO = "SAMPLE_TEXT";
    private static final String UPDATED_APELLIDO = "UPDATED_TEXT";
    private static final String DEFAULT_NOMBRE = "SAMPLE_TEXT";
    private static final String UPDATED_NOMBRE = "UPDATED_TEXT";

    private static final DateTime DEFAULT_FECHA_NACIMIENTO = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_FECHA_NACIMIENTO = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_FECHA_NACIMIENTO_STR = dateTimeFormatter.print(DEFAULT_FECHA_NACIMIENTO);
    private static final String DEFAULT_SEXO = "SAMPLE_TEXT";
    private static final String UPDATED_SEXO = "UPDATED_TEXT";
    private static final String DEFAULT_DOMICILIO = "SAMPLE_TEXT";
    private static final String UPDATED_DOMICILIO = "UPDATED_TEXT";
    private static final String DEFAULT_TELEFONO = "SAMPLE_TEXT";
    private static final String UPDATED_TELEFONO = "UPDATED_TEXT";
    private static final String DEFAULT_CELULAR = "SAMPLE_TEXT";
    private static final String UPDATED_CELULAR = "UPDATED_TEXT";
    private static final String DEFAULT_DNI = "SAMPLE_TEXT";
    private static final String UPDATED_DNI = "UPDATED_TEXT";

    @Inject
    private PacienteRepository pacienteRepository;

    private MockMvc restPacienteMockMvc;

    private Paciente paciente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PacienteResource pacienteResource = new PacienteResource();
        ReflectionTestUtils.setField(pacienteResource, "pacienteRepository", pacienteRepository);
        this.restPacienteMockMvc = MockMvcBuilders.standaloneSetup(pacienteResource).build();
    }

    @Before
    public void initTest() {
        paciente = new Paciente();
        paciente.setNumeroAsociado(DEFAULT_NUMERO_ASOCIADO);
        paciente.setApellido(DEFAULT_APELLIDO);
        paciente.setNombre(DEFAULT_NOMBRE);
        paciente.setFechaNacimiento(DEFAULT_FECHA_NACIMIENTO);
        paciente.setSexo(DEFAULT_SEXO);
        paciente.setDomicilio(DEFAULT_DOMICILIO);
        paciente.setTelefono(DEFAULT_TELEFONO);
        paciente.setCelular(DEFAULT_CELULAR);
        paciente.setDni(DEFAULT_DNI);
    }

    @Test
    @Transactional
    public void createPaciente() throws Exception {
        int databaseSizeBeforeCreate = pacienteRepository.findAll().size();

        // Create the Paciente
        restPacienteMockMvc.perform(post("/api/pacientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paciente)))
                .andExpect(status().isCreated());

        // Validate the Paciente in the database
        List<Paciente> pacientes = pacienteRepository.findAll();
        assertThat(pacientes).hasSize(databaseSizeBeforeCreate + 1);
        Paciente testPaciente = pacientes.get(pacientes.size() - 1);
        assertThat(testPaciente.getNumeroAsociado()).isEqualTo(DEFAULT_NUMERO_ASOCIADO);
        assertThat(testPaciente.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testPaciente.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPaciente.getFechaNacimiento().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testPaciente.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testPaciente.getDomicilio()).isEqualTo(DEFAULT_DOMICILIO);
        assertThat(testPaciente.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testPaciente.getCelular()).isEqualTo(DEFAULT_CELULAR);
        assertThat(testPaciente.getDni()).isEqualTo(DEFAULT_DNI);
    }

    @Test
    @Transactional
    public void getAllPacientes() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacientes
        restPacienteMockMvc.perform(get("/api/pacientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
                .andExpect(jsonPath("$.[*].numeroAsociado").value(hasItem(DEFAULT_NUMERO_ASOCIADO.toString())))
                .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO.toString())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO_STR)))
                .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())))
                .andExpect(jsonPath("$.[*].domicilio").value(hasItem(DEFAULT_DOMICILIO.toString())))
                .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO.toString())))
                .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR.toString())))
                .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI.toString())));
    }

    @Test
    @Transactional
    public void getPaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get the paciente
        restPacienteMockMvc.perform(get("/api/pacientes/{id}", paciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(paciente.getId().intValue()))
            .andExpect(jsonPath("$.numeroAsociado").value(DEFAULT_NUMERO_ASOCIADO.toString()))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO.toString()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO_STR))
            .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO.toString()))
            .andExpect(jsonPath("$.domicilio").value(DEFAULT_DOMICILIO.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO.toString()))
            .andExpect(jsonPath("$.celular").value(DEFAULT_CELULAR.toString()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPaciente() throws Exception {
        // Get the paciente
        restPacienteMockMvc.perform(get("/api/pacientes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

		int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Update the paciente
        paciente.setNumeroAsociado(UPDATED_NUMERO_ASOCIADO);
        paciente.setApellido(UPDATED_APELLIDO);
        paciente.setNombre(UPDATED_NOMBRE);
        paciente.setFechaNacimiento(UPDATED_FECHA_NACIMIENTO);
        paciente.setSexo(UPDATED_SEXO);
        paciente.setDomicilio(UPDATED_DOMICILIO);
        paciente.setTelefono(UPDATED_TELEFONO);
        paciente.setCelular(UPDATED_CELULAR);
        paciente.setDni(UPDATED_DNI);
        restPacienteMockMvc.perform(put("/api/pacientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paciente)))
                .andExpect(status().isOk());

        // Validate the Paciente in the database
        List<Paciente> pacientes = pacienteRepository.findAll();
        assertThat(pacientes).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacientes.get(pacientes.size() - 1);
        assertThat(testPaciente.getNumeroAsociado()).isEqualTo(UPDATED_NUMERO_ASOCIADO);
        assertThat(testPaciente.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testPaciente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPaciente.getFechaNacimiento().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testPaciente.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testPaciente.getDomicilio()).isEqualTo(UPDATED_DOMICILIO);
        assertThat(testPaciente.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testPaciente.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testPaciente.getDni()).isEqualTo(UPDATED_DNI);
    }

    @Test
    @Transactional
    public void deletePaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

		int databaseSizeBeforeDelete = pacienteRepository.findAll().size();

        // Get the paciente
        restPacienteMockMvc.perform(delete("/api/pacientes/{id}", paciente.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Paciente> pacientes = pacienteRepository.findAll();
        assertThat(pacientes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
