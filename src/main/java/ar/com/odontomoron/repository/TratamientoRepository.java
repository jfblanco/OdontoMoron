package ar.com.odontomoron.repository;

import ar.com.odontomoron.domain.Tratamiento;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tratamiento entity.
 */
public interface TratamientoRepository extends JpaRepository<Tratamiento,Long> {

}
