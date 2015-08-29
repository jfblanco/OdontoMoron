package ar.com.odontomoron.repository;

import ar.com.odontomoron.domain.Prepaga;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Prepaga entity.
 */
public interface PrepagaRepository extends JpaRepository<Prepaga,Long> {

}
