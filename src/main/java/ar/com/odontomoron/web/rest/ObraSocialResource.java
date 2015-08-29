package ar.com.odontomoron.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.com.odontomoron.domain.ObraSocial;
import ar.com.odontomoron.repository.ObraSocialRepository;
import ar.com.odontomoron.service.ObraSocialService;
import ar.com.odontomoron.web.rest.dto.ObraSocialDTO;
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
 * REST controller for managing ObraSocial.
 */
@RestController
@RequestMapping("/api")
public class ObraSocialResource {

    private final Logger log = LoggerFactory.getLogger(ObraSocialResource.class);

    @Inject
    private ObraSocialRepository obraSocialRepository;
    
    @Inject
    private ObraSocialService obraSocialService;

    /**
     * POST  /obraSocials -> Create a new obraSocial.
     */
    @RequestMapping(value = "/obraSocials",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody ObraSocial obraSocial) throws URISyntaxException {
        log.debug("REST request to save ObraSocial : {}", obraSocial);
        if (obraSocial.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new obraSocial cannot already have an ID").build();
        }
        obraSocialRepository.save(obraSocial);
        return ResponseEntity.created(new URI("/api/obraSocials/" + obraSocial.getId())).build();
    }

    /**
     * PUT  /obraSocials -> Updates an existing obraSocial.
     */
    @RequestMapping(value = "/obraSocials",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody ObraSocial obraSocial) throws URISyntaxException {
        log.debug("REST request to update ObraSocial : {}", obraSocial);
        if (obraSocial.getId() == null) {
            return create(obraSocial);
        }
        obraSocialRepository.save(obraSocial);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /obraSocials -> get all the obraSocials.
     */
    @RequestMapping(value = "/obraSocials",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ObraSocial> getAll() {
        log.debug("REST request to get all ObraSocials");
        return obraSocialRepository.findAll();
    }

    /**
     * GET  /obraSocials/:id -> get the "id" obraSocial.
     */
    @RequestMapping(value = "/obraSocials/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ObraSocialDTO> get(@PathVariable Long id) {
        log.debug("REST request to get ObraSocial : {}", id);
        return Optional.ofNullable(obraSocialService.cargarOperatorias(id))
            .map(obraSocial -> new ResponseEntity<>(
                obraSocial,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /obraSocials/:id -> delete the "id" obraSocial.
     */
    @RequestMapping(value = "/obraSocials/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ObraSocial : {}", id);
        obraSocialRepository.delete(id);
    }
}
