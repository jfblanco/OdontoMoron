package ar.com.odontomoron.repository;

import ar.com.odontomoron.domain.Turno;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Turno entity.
 */
public interface TurnoRepository extends JpaRepository<Turno,Long> {

}
