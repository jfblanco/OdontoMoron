package ar.com.odontomoron.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.com.odontomoron.domain.CondicionFacturacion;
import ar.com.odontomoron.repository.CondicionFacturacionRepository;
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
 * REST controller for managing CondicionFacturacion.
 */
@RestController
@RequestMapping("/api")
public class CondicionFacturacionResource {

    private final Logger log = LoggerFactory.getLogger(CondicionFacturacionResource.class);

    @Inject
    private CondicionFacturacionRepository condicionFacturacionRepository;

    /**
     * POST  /condicionFacturacions -> Create a new condicionFacturacion.
     */
    @RequestMapping(value = "/condicionFacturacions",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody CondicionFacturacion condicionFacturacion) throws URISyntaxException {
        log.debug("REST request to save CondicionFacturacion : {}", condicionFacturacion);
        if (condicionFacturacion.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new condicionFacturacion cannot already have an ID").build();
        }
        condicionFacturacionRepository.save(condicionFacturacion);
        return ResponseEntity.created(new URI("/api/condicionFacturacions/" + condicionFacturacion.getId())).build();
    }

    /**
     * PUT  /condicionFacturacions -> Updates an existing condicionFacturacion.
     */
    @RequestMapping(value = "/condicionFacturacions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody CondicionFacturacion condicionFacturacion) throws URISyntaxException {
        log.debug("REST request to update CondicionFacturacion : {}", condicionFacturacion);
        if (condicionFacturacion.getId() == null) {
            return create(condicionFacturacion);
        }
        condicionFacturacionRepository.save(condicionFacturacion);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /condicionFacturacions -> get all the condicionFacturacions.
     */
    @RequestMapping(value = "/condicionFacturacions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CondicionFacturacion> getAll() {
        log.debug("REST request to get all CondicionFacturacions");
        return condicionFacturacionRepository.findAll();
    }

    /**
     * GET  /condicionFacturacions/:id -> get the "id" condicionFacturacion.
     */
    @RequestMapping(value = "/condicionFacturacions/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CondicionFacturacion> get(@PathVariable Long id) {
        log.debug("REST request to get CondicionFacturacion : {}", id);
        return Optional.ofNullable(condicionFacturacionRepository.findOne(id))
            .map(condicionFacturacion -> new ResponseEntity<>(
                condicionFacturacion,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /condicionFacturacions/:id -> delete the "id" condicionFacturacion.
     */
    @RequestMapping(value = "/condicionFacturacions/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete CondicionFacturacion : {}", id);
        condicionFacturacionRepository.delete(id);
    }
}
