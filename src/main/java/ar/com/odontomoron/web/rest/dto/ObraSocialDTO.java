/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.odontomoron.web.rest.dto;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author francisco
 */
public class ObraSocialDTO {
    
    Set<OperatoriaDTO> operatorias = new HashSet<OperatoriaDTO>();
    
    String nombre;

    public Set<OperatoriaDTO> getOperatorias() {
        return operatorias;
    }

    public void setOperatorias(Set<OperatoriaDTO> operatorias) {
        this.operatorias = operatorias;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
