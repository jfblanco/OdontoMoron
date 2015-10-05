package ar.com.odontomoron.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.com.odontomoron.domain.Turno;
import ar.com.odontomoron.domain.User;
import ar.com.odontomoron.repository.PacienteRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

/**
 * REST controller for managing Turno.
 */
@RestController
@RequestMapping("/api")
public class TurnoResource {

    private final Logger log = LoggerFactory.getLogger(TurnoResource.class);

    @Inject
    private TurnoRepository turnoRepository;
    
    @Inject
    private PacienteRepository pacienteRepository;
    
    @Inject
    private UserRepository userRepository;

    /**
     * POST  /turnos -> Create a new turno.
     */
    @RequestMapping(value = "/turnos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Turno turno) throws URISyntaxException {
        log.debug("REST request to save Turno : {}", turno);
        if (turno.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new turno cannot already have an ID").build();
        }
        turno.setPaciente(pacienteRepository.findOne(turno.getPaciente().getId()));
        turnoRepository.save(turno);
        return ResponseEntity.created(new URI("/api/turnos/" + turno.getId())).build();
    }

    /**
     * PUT  /turnos -> Updates an existing turno.
     */
    @RequestMapping(value = "/turnos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Turno turno) throws URISyntaxException {
        log.debug("REST request to update Turno : {}", turno);
        if (turno.getId() == null) {
            return create(turno);
        }
        turnoRepository.save(turno);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /turnos -> get all the turnos.
     */
    @RequestMapping(value = "/turnos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Turno> getAll() {
        log.debug("REST request to get all Turnos");
        return turnoRepository.findAll();
    }

    /**
     * GET  /turnos/:id -> get the "id" turno.
     */
    @RequestMapping(value = "/turnos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Turno> get(@PathVariable Long id) {
        log.debug("REST request to get Turno : {}", id);
        return Optional.ofNullable(turnoRepository.findOne(id))
            .map(turno -> new ResponseEntity<>(
                turno,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * GET  /turnos/:numeroAsociado -> get turnos by odontologo's ID and today.
     */
    @RequestMapping(value = "/turnos/pacienteodontologo/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Turno>> getPorOdontologo(@PathVariable Long id) {
        User odontologo = userRepository.findOne(id);
        log.debug("REST getting Pacientes for the Odontologo : {}", odontologo);
        DateTime dateNow = new DateTime();
        DateTime from = dateNow.minusHours(dateNow.getHourOfDay());
        DateTime to = dateNow.plusHours(24 - dateNow.getHourOfDay());
        PageRequest pageRequest = new PageRequest(0,20,new Sort(Sort.Direction.ASC, "fecha"));
        return Optional.ofNullable(turnoRepository.findTurnosParaOdontologo(from,to,odontologo.getId(), pageRequest))
            .map(turno -> new ResponseEntity<>(
                turno,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /turnos/:id -> delete the "id" turno.
     */
    @RequestMapping(value = "/turnos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Turno : {}", id);
        turnoRepository.delete(id);
    }
}
