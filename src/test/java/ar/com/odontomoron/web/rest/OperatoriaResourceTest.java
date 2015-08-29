package ar.com.odontomoron.web.rest;

import ar.com.odontomoron.Application;
import ar.com.odontomoron.domain.Operatoria;
import ar.com.odontomoron.repository.OperatoriaRepository;

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
 * Test class for the OperatoriaResource REST controller.
 *
 * @see OperatoriaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OperatoriaResourceTest {

    private static final String DEFAULT_DESCRIPCION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPCION = "UPDATED_TEXT";
    private static final String DEFAULT_CODIGO = "SAMPLE_TEXT";
    private static final String UPDATED_CODIGO = "UPDATED_TEXT";

    private static final BigDecimal DEFAULT_PRECIO = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO = new BigDecimal(1);

    @Inject
    private OperatoriaRepository operatoriaRepository;

    private MockMvc restOperatoriaMockMvc;

    private Operatoria operatoria;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OperatoriaResource operatoriaResource = new OperatoriaResource();
        ReflectionTestUtils.setField(operatoriaResource, "operatoriaRepository", operatoriaRepository);
        this.restOperatoriaMockMvc = MockMvcBuilders.standaloneSetup(operatoriaResource).build();
    }

    @Before
    public void initTest() {
        operatoria = new Operatoria();
        operatoria.setDescripcion(DEFAULT_DESCRIPCION);
        operatoria.setCodigo(DEFAULT_CODIGO);
        operatoria.setPrecio(DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    public void createOperatoria() throws Exception {
        int databaseSizeBeforeCreate = operatoriaRepository.findAll().size();

        // Create the Operatoria
        restOperatoriaMockMvc.perform(post("/api/operatorias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(operatoria)))
                .andExpect(status().isCreated());

        // Validate the Operatoria in the database
        List<Operatoria> operatorias = operatoriaRepository.findAll();
        assertThat(operatorias).hasSize(databaseSizeBeforeCreate + 1);
        Operatoria testOperatoria = operatorias.get(operatorias.size() - 1);
        assertThat(testOperatoria.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testOperatoria.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testOperatoria.getPrecio()).isEqualTo(DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    public void getAllOperatorias() throws Exception {
        // Initialize the database
        operatoriaRepository.saveAndFlush(operatoria);

        // Get all the operatorias
        restOperatoriaMockMvc.perform(get("/api/operatorias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(operatoria.getId().intValue())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
                .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
                .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.intValue())));
    }

    @Test
    @Transactional
    public void getOperatoria() throws Exception {
        // Initialize the database
        operatoriaRepository.saveAndFlush(operatoria);

        // Get the operatoria
        restOperatoriaMockMvc.perform(get("/api/operatorias/{id}", operatoria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(operatoria.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOperatoria() throws Exception {
        // Get the operatoria
        restOperatoriaMockMvc.perform(get("/api/operatorias/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOperatoria() throws Exception {
        // Initialize the database
        operatoriaRepository.saveAndFlush(operatoria);

		int databaseSizeBeforeUpdate = operatoriaRepository.findAll().size();

        // Update the operatoria
        operatoria.setDescripcion(UPDATED_DESCRIPCION);
        operatoria.setCodigo(UPDATED_CODIGO);
        operatoria.setPrecio(UPDATED_PRECIO);
        restOperatoriaMockMvc.perform(put("/api/operatorias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(operatoria)))
                .andExpect(status().isOk());

        // Validate the Operatoria in the database
        List<Operatoria> operatorias = operatoriaRepository.findAll();
        assertThat(operatorias).hasSize(databaseSizeBeforeUpdate);
        Operatoria testOperatoria = operatorias.get(operatorias.size() - 1);
        assertThat(testOperatoria.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testOperatoria.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testOperatoria.getPrecio()).isEqualTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    public void deleteOperatoria() throws Exception {
        // Initialize the database
        operatoriaRepository.saveAndFlush(operatoria);

		int databaseSizeBeforeDelete = operatoriaRepository.findAll().size();

        // Get the operatoria
        restOperatoriaMockMvc.perform(delete("/api/operatorias/{id}", operatoria.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Operatoria> operatorias = operatoriaRepository.findAll();
        assertThat(operatorias).hasSize(databaseSizeBeforeDelete - 1);
    }
}
