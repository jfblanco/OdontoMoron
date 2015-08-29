package ar.com.odontomoron.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ar.com.odontomoron.domain.util.CustomDateTimeDeserializer;
import ar.com.odontomoron.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Tratamiento.
 */
@Entity
@Table(name = "TRATAMIENTO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tratamiento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "fecha")
    private DateTime fecha;

    @Column(name = "conformidad")
    private Boolean conformidad;

    @Column(name = "cara")
    private String cara;

    @Column(name = "sector")
    private String sector;

    @Column(name = "pieza")
    private String pieza;

    @ManyToOne
    private Operatoria operatoria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getFecha() {
        return fecha;
    }

    public void setFecha(DateTime fecha) {
        this.fecha = fecha;
    }

    public Boolean getConformidad() {
        return conformidad;
    }

    public void setConformidad(Boolean conformidad) {
        this.conformidad = conformidad;
    }

    public String getCara() {
        return cara;
    }

    public void setCara(String cara) {
        this.cara = cara;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getPieza() {
        return pieza;
    }

    public void setPieza(String pieza) {
        this.pieza = pieza;
    }

    public Operatoria getOperatoria() {
        return operatoria;
    }

    public void setOperatoria(Operatoria operatoria) {
        this.operatoria = operatoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tratamiento tratamiento = (Tratamiento) o;

        if ( ! Objects.equals(id, tratamiento.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tratamiento{" +
                "id=" + id +
                ", fecha='" + fecha + "'" +
                ", conformidad='" + conformidad + "'" +
                ", cara='" + cara + "'" +
                ", sector='" + sector + "'" +
                ", pieza='" + pieza + "'" +
                '}';
    }
}
