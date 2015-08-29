package ar.com.odontomoron.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.com.odontomoron.domain.Odontologo;
import ar.com.odontomoron.repository.OdontologoRepository;
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
 * REST controller for managing Odontologo.
 */
@RestController
@RequestMapping("/api")
public class OdontologoResource {

    private final Logger log = LoggerFactory.getLogger(OdontologoResource.class);

    @Inject
    private OdontologoRepository odontologoRepository;

    /**
     * POST  /odontologos -> Create a new odontologo.
     */
    @RequestMapping(value = "/odontologos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Odontologo odontologo) throws URISyntaxException {
        log.debug("REST request to save Odontologo : {}", odontologo);
        if (odontologo.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new odontologo cannot already have an ID").build();
        }
        odontologoRepository.save(odontologo);
        return ResponseEntity.created(new URI("/api/odontologos/" + odontologo.getId())).build();
    }

    /**
     * PUT  /odontologos -> Updates an existing odontologo.
     */
    @RequestMapping(value = "/odontologos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Odontologo odontologo) throws URISyntaxException {
        log.debug("REST request to update Odontologo : {}", odontologo);
        if (odontologo.getId() == null) {
            return create(odontologo);
        }
        odontologoRepository.save(odontologo);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /odontologos -> get all the odontologos.
     */
    @RequestMapping(value = "/odontologos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Odontologo> getAll() {
        log.debug("REST request to get all Odontologos");
        return odontologoRepository.findAll();
    }

    /**
     * GET  /odontologos/:id -> get the "id" odontologo.
     */
    @RequestMapping(value = "/odontologos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Odontologo> get(@PathVariable Long id) {
        log.debug("REST request to get Odontologo : {}", id);
        return Optional.ofNullable(odontologoRepository.findOne(id))
            .map(odontologo -> new ResponseEntity<>(
                odontologo,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /odontologos/:id -> delete the "id" odontologo.
     */
    @RequestMapping(value = "/odontologos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Odontologo : {}", id);
        odontologoRepository.delete(id);
    }
}
