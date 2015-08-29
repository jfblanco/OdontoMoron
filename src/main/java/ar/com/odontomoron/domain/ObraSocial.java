package ar.com.odontomoron.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A ObraSocial.
 */
@Entity
@Table(name = "OBRASOCIAL")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ObraSocial implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nombre")
    private String nombre;
    
    @OneToMany(mappedBy = "obraSocial")
    private Set<Operatoria> operatorias;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Operatoria> getOperatorias() {
        return operatorias;
    }

    public void setOperatorias(Set<Operatoria> operatorias) {
        this.operatorias = operatorias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ObraSocial obraSocial = (ObraSocial) o;

        if ( ! Objects.equals(id, obraSocial.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ObraSocial{" +
                "id=" + id +
                ", nombre='" + nombre + "'" +
                '}';
    }
}
