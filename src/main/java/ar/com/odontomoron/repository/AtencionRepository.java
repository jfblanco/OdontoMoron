package ar.com.odontomoron.repository;

import ar.com.odontomoron.domain.Atencion;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Atencion entity.
 */
public interface AtencionRepository extends JpaRepository<Atencion,Long> {

}
