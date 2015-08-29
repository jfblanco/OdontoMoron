package ar.com.odontomoron.repository;

import ar.com.odontomoron.domain.CondicionFacturacion;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CondicionFacturacion entity.
 */
public interface CondicionFacturacionRepository extends JpaRepository<CondicionFacturacion,Long> {

}
