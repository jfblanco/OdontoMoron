package ar.com.odontomoron.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.com.odontomoron.domain.Tratamiento;
import ar.com.odontomoron.repository.TratamientoRepository;
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
 * REST controller for managing Tratamiento.
 */
@RestController
@RequestMapping("/api")
public class TratamientoResource {

    private final Logger log = LoggerFactory.getLogger(TratamientoResource.class);

    @Inject
    private TratamientoRepository tratamientoRepository;

    /**
     * POST  /tratamientos -> Create a new tratamiento.
     */
    @RequestMapping(value = "/tratamientos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Tratamiento tratamiento) throws URISyntaxException {
        log.debug("REST request to save Tratamiento : {}", tratamiento);
        if (tratamiento.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new tratamiento cannot already have an ID").build();
        }
        tratamientoRepository.save(tratamiento);
        return ResponseEntity.created(new URI("/api/tratamientos/" + tratamiento.getId())).build();
    }

    /**
     * PUT  /tratamientos -> Updates an existing tratamiento.
     */
    @RequestMapping(value = "/tratamientos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Tratamiento tratamiento) throws URISyntaxException {
        log.debug("REST request to update Tratamiento : {}", tratamiento);
        if (tratamiento.getId() == null) {
            return create(tratamiento);
        }
        tratamientoRepository.save(tratamiento);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /tratamientos -> get all the tratamientos.
     */
    @RequestMapping(value = "/tratamientos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tratamiento> getAll() {
        log.debug("REST request to get all Tratamientos");
        return tratamientoRepository.findAll();
    }

    /**
     * GET  /tratamientos/:id -> get the "id" tratamiento.
     */
    @RequestMapping(value = "/tratamientos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tratamiento> get(@PathVariable Long id) {
        log.debug("REST request to get Tratamiento : {}", id);
        return Optional.ofNullable(tratamientoRepository.findOne(id))
            .map(tratamiento -> new ResponseEntity<>(
                tratamiento,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tratamientos/:id -> delete the "id" tratamiento.
     */
    @RequestMapping(value = "/tratamientos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Tratamiento : {}", id);
        tratamientoRepository.delete(id);
    }
}
