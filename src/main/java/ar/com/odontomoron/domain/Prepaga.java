package ar.com.odontomoron.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Prepaga.
 */
@Entity
@Table(name = "PREPAGA")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Prepaga implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "numero_afiliado")
    private String numeroAfiliado;

    @ManyToOne
    private CondicionFacturacion condicionFacturacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroAfiliado() {
        return numeroAfiliado;
    }

    public void setNumeroAfiliado(String numeroAfiliado) {
        this.numeroAfiliado = numeroAfiliado;
    }

    public CondicionFacturacion getCondicionFacturacion() {
        return condicionFacturacion;
    }

    public void setCondicionFacturacion(CondicionFacturacion condicionFacturacion) {
        this.condicionFacturacion = condicionFacturacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Prepaga prepaga = (Prepaga) o;

        if ( ! Objects.equals(id, prepaga.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Prepaga{" +
                "id=" + id +
                ", numeroAfiliado='" + numeroAfiliado + "'" +
                '}';
    }
}
