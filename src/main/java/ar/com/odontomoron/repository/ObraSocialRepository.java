package ar.com.odontomoron.repository;

import ar.com.odontomoron.domain.ObraSocial;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ObraSocial entity.
 */
public interface ObraSocialRepository extends JpaRepository<ObraSocial,Long> {

}
