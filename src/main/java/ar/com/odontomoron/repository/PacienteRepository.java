package ar.com.odontomoron.repository;

import ar.com.odontomoron.domain.Paciente;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Paciente entity.
 */
public interface PacienteRepository extends JpaRepository<Paciente,Long> {
    
    public Paciente findByNumeroAsociado(String numeroAsociado);
    
    @Query("select count(*) from ar.com.odontomoron.domain.Paciente where numeroAsociado like ?1%")
    public Integer findCountPacientes(String fecha);
}
