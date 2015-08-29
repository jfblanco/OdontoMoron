package ar.com.odontomoron.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.com.odontomoron.domain.Prepaga;
import ar.com.odontomoron.repository.PrepagaRepository;
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
 * REST controller for managing Prepaga.
 */
@RestController
@RequestMapping("/api")
public class PrepagaResource {

    private final Logger log = LoggerFactory.getLogger(PrepagaResource.class);

    @Inject
    private PrepagaRepository prepagaRepository;

    /**
     * POST  /prepagas -> Create a new prepaga.
     */
    @RequestMapping(value = "/prepagas",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Prepaga prepaga) throws URISyntaxException {
        log.debug("REST request to save Prepaga : {}", prepaga);
        if (prepaga.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new prepaga cannot already have an ID").build();
        }
        prepagaRepository.save(prepaga);
        return ResponseEntity.created(new URI("/api/prepagas/" + prepaga.getId())).build();
    }

    /**
     * PUT  /prepagas -> Updates an existing prepaga.
     */
    @RequestMapping(value = "/prepagas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Prepaga prepaga) throws URISyntaxException {
        log.debug("REST request to update Prepaga : {}", prepaga);
        if (prepaga.getId() == null) {
            return create(prepaga);
        }
        prepagaRepository.save(prepaga);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /prepagas -> get all the prepagas.
     */
    @RequestMapping(value = "/prepagas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Prepaga> getAll() {
        log.debug("REST request to get all Prepagas");
        return prepagaRepository.findAll();
    }

    /**
     * GET  /prepagas/:id -> get the "id" prepaga.
     */
    @RequestMapping(value = "/prepagas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prepaga> get(@PathVariable Long id) {
        log.debug("REST request to get Prepaga : {}", id);
        return Optional.ofNullable(prepagaRepository.findOne(id))
            .map(prepaga -> new ResponseEntity<>(
                prepaga,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prepagas/:id -> delete the "id" prepaga.
     */
    @RequestMapping(value = "/prepagas/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Prepaga : {}", id);
        prepagaRepository.delete(id);
    }
}
