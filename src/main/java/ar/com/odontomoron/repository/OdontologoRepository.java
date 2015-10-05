package ar.com.odontomoron.repository;

import ar.com.odontomoron.domain.Odontologo;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Odontologo entity.
 */
public interface OdontologoRepository extends JpaRepository<Odontologo,Long> {

}
