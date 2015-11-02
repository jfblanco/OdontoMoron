package ar.com.odontomoron.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.com.odontomoron.domain.Atencion;
import ar.com.odontomoron.repository.AtencionRepository;
import ar.com.odontomoron.repository.TurnoRepository;
import ar.com.odontomoron.repository.UserRepository;
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
import org.joda.time.DateTime;

/**
 * REST controller for managing Atencion.
 */
@RestController
@RequestMapping("/api")
public class AtencionResource {

    private final Logger log = LoggerFactory.getLogger(AtencionResource.class);

    @Inject
    private AtencionRepository atencionRepository;
    
    @Inject
    private TurnoRepository turnoRepository;
    
    @Inject
    private UserRepository userRepository;

    /**
     * POST  /atencions -> Create a new atencion.
     */
    @RequestMapping(value = "/atencions",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Atencion atencion) throws URISyntaxException {
        log.debug("REST request to save Atencion : {}", atencion);
        if (atencion.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new atencion cannot already have an ID").build();
        }
        //atencion.setFecha(DateTime.now());
        atencionRepository.save(atencion);
        atencion.getTurno().setAtencion(atencion);
        turnoRepository.save(atencion.getTurno());
        return ResponseEntity.created(new URI("/api/atencions/" + atencion.getId())).build();
    }

    /**
     * PUT  /atencions -> Updates an existing atencion.
     */
    @RequestMapping(value = "/atencions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Atencion atencion) throws URISyntaxException {
        log.debug("REST request to update Atencion : {}", atencion);
        if (atencion.getId() == null) {
            return create(atencion);
        }
        atencionRepository.save(atencion);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /atencions -> get all the atencions.
     */
    @RequestMapping(value = "/atencions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Atencion> getAll() {
        log.debug("REST request to get all Atencions");
        return atencionRepository.findAll();
    }

    /**
     * GET  /atencions/:id -> get the "id" atencion.
     */
    @RequestMapping(value = "/atencions/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Atencion> get(@PathVariable Long id) {
        log.debug("REST request to get Atencion : {}", id);
        return Optional.ofNullable(atencionRepository.findOne(id))
            .map(atencion -> new ResponseEntity<>(
                atencion,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /atencions/:id -> delete the "id" atencion.
     */
    @RequestMapping(value = "/atencions/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Atencion : {}", id);
        atencionRepository.delete(id);
    }
    
    /**
     * GET  /atencions/:id -> get the "id" atencion.
     */
    @RequestMapping(value = "/atencions/buscaratencionesdelmes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Atencion>> buscarAtencionesDelMes(@PathVariable Long id) {
        log.debug("REST request to get Atencion : {}", id);
        
        return Optional.ofNullable(atencionRepository.findByOdontologo(id))
            .map(atencion -> new ResponseEntity<>(
                atencion,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
