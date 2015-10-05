package ar.com.odontomoron.repository;

import ar.com.odontomoron.domain.Operatoria;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Operatoria entity.
 */
public interface OperatoriaRepository extends JpaRepository<Operatoria,Long> {

}
