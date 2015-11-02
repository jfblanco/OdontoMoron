package ar.com.odontomoron.repository;

import ar.com.odontomoron.domain.Operatoria;
import java.util.List;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Operatoria entity.
 */
public interface OperatoriaRepository extends JpaRepository<Operatoria,Long> {

    @Query("from ar.com.odontomoron.domain.Operatoria op where op.obraSocial.id = ?1")
    public List<Operatoria> findByObraSocial(Long obraSocialId);
}
