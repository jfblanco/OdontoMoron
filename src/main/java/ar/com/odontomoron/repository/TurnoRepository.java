package ar.com.odontomoron.repository;

import ar.com.odontomoron.domain.Turno;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Turno entity.
 */
public interface TurnoRepository extends JpaRepository<Turno,Long> {

    @Query("from ar.com.odontomoron.domain.Turno t where ((t.fecha between ?1 and ?2) and t.odontologo.id = ?3)")
    public List<Turno> findTurnosParaOdontologo(DateTime fecha_from, DateTime fecha_to, Long odontologoId, Pageable pageable);
}
