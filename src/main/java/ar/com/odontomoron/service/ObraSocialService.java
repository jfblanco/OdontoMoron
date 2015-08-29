package ar.com.odontomoron.service;

import ar.com.odontomoron.domain.ObraSocial;
import ar.com.odontomoron.domain.Operatoria;
import ar.com.odontomoron.repository.ObraSocialRepository;
import ar.com.odontomoron.web.rest.dto.ObraSocialDTO;
import ar.com.odontomoron.web.rest.dto.OperatoriaDTO;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ObraSocialService {

    private final Logger log = LoggerFactory.getLogger(ObraSocialService.class);

    @Inject
    private ObraSocialRepository obraSocialRepository;
    
    public ObraSocialDTO cargarOperatorias(Long id)
    {
        ObraSocial obraSocial = obraSocialRepository.findOne(id);
        ObraSocialDTO obraSocialDTO = new ObraSocialDTO();
        obraSocial.setNombre(obraSocial.getNombre());
        for(Operatoria operatoria : obraSocial.getOperatorias())
        {
            OperatoriaDTO operatoriaDTO = new OperatoriaDTO();
            operatoriaDTO.setCodigo(operatoria.getCodigo());
            operatoriaDTO.setDescripcion(operatoria.getDescripcion());
            operatoriaDTO.setPrecio(operatoria.getPrecio());
            obraSocialDTO.getOperatorias().add(operatoriaDTO);
        }
        return obraSocialDTO;
    }
}
