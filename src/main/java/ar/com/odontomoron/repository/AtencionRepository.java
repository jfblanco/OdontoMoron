package ar.com.odontomoron.repository;

import ar.com.odontomoron.domain.Atencion;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Atencion entity.
 */
public interface AtencionRepository extends JpaRepository<Atencion,Long> {

    @Query("from ar.com.odontomoron.domain.Atencion a where a.odontologo.id = ?1")
    public List<Atencion> findByOdontologo(Long odontologoId);
}
