package ar.com.odontomoron.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CondicionFacturacion.
 */
@Entity
@Table(name = "CONDICIONFACTURACION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CondicionFacturacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "descripcion")
    private String descripcion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CondicionFacturacion condicionFacturacion = (CondicionFacturacion) o;

        if ( ! Objects.equals(id, condicionFacturacion.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CondicionFacturacion{" +
                "id=" + id +
                ", descripcion='" + descripcion + "'" +
                '}';
    }
}
