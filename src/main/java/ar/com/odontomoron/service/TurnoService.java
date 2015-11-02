package ar.com.odontomoron.service;

import ar.com.odontomoron.domain.Turno;
import ar.com.odontomoron.repository.TurnoRepository;
import java.util.List;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TurnoService {

    private final Logger log = LoggerFactory.getLogger(TurnoService.class);
    
    @Inject
    private TurnoRepository turnoRepository;
    
    @Transactional
    public List<Turno> turnoPorPaciente(DateTime from, DateTime to, Long odontologoID, PageRequest pageRequest)
    {
        List<Turno> lista = turnoRepository.findTurnosParaOdontologo(from,to,odontologoID, pageRequest);
        for(Turno turno : lista)
        {
            turno.setFinalizada((turno.getAtencion() != null)?Boolean.TRUE:Boolean.FALSE);
        }
        return lista;
    }

}
