package ar.com.odontomoron.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.com.odontomoron.domain.Operatoria;
import ar.com.odontomoron.domain.Paciente;
import ar.com.odontomoron.repository.OperatoriaRepository;
import ar.com.odontomoron.repository.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Operatoria.
 */
@RestController
@RequestMapping("/api")
public class OperatoriaResource {

    private final Logger log = LoggerFactory.getLogger(OperatoriaResource.class);

    @Inject
    private OperatoriaRepository operatoriaRepository;
    
    @Inject
    private PacienteRepository pacienteRepository;

    /**
     * POST  /operatorias -> Create a new operatoria.
     */
    @RequestMapping(value = "/operatorias",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Operatoria operatoria) throws URISyntaxException {
        log.debug("REST request to save Operatoria : {}", operatoria);
        if (operatoria.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new operatoria cannot already have an ID").build();
        }
        operatoriaRepository.save(operatoria);
        return ResponseEntity.created(new URI("/api/operatorias/" + operatoria.getId())).build();
    }

    /**
     * PUT  /operatorias -> Updates an existing operatoria.
     */
    @RequestMapping(value = "/operatorias",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Operatoria operatoria) throws URISyntaxException {
        log.debug("REST request to update Operatoria : {}", operatoria);
        if (operatoria.getId() == null) {
            return create(operatoria);
        }
        operatoriaRepository.save(operatoria);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /operatorias -> get all the operatorias.
     */
    @RequestMapping(value = "/operatorias",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Operatoria> getAll() {
        log.debug("REST request to get all Operatorias");
        return operatoriaRepository.findAll();
    }

    /**
     * GET  /operatorias/:id -> get the "id" operatoria.
     */
    @RequestMapping(value = "/operatorias/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Operatoria> get(@PathVariable Long id) {
        log.debug("REST request to get Operatoria : {}", id);
        return Optional.ofNullable(operatoriaRepository.findOne(id))
            .map(operatoria -> new ResponseEntity<>(
                operatoria,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * GET  /operatorias/:id -> get the "id" operatoria.
     */
    @RequestMapping(value = "/operatorias/operatoriasporusuario/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Operatoria>> getPorUsuario(@PathVariable Long id) {
        log.debug("REST request to get Operatorias Para el usuario: {}", id);
        Paciente paciente = pacienteRepository.findOne(id);
        return Optional.ofNullable(operatoriaRepository.findByObraSocial(paciente.getObraSocial().getId()))
            .map(operatoria -> new ResponseEntity<>(
                operatoria,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /operatorias/:id -> delete the "id" operatoria.
     */
    @RequestMapping(value = "/operatorias/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Operatoria : {}", id);
        operatoriaRepository.delete(id);
    }
}
