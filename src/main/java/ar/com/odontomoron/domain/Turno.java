package ar.com.odontomoron.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ar.com.odontomoron.domain.util.CustomDateTimeDeserializer;
import ar.com.odontomoron.domain.util.CustomDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Turno.
 */
@Entity
@Table(name = "TURNO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Turno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "fecha")
    private DateTime fecha;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "sobreturno")
    private Boolean sobreturno;

    @OneToOne
    private Tratamiento tratamiento;

    @OneToOne
    @JsonIgnore
    private Atencion atencion;
    
    @ManyToOne
    private Paciente paciente;
    
    @ManyToOne
    private User odontologo;
    
    @Transient
    @JsonSerialize
    private Boolean finalizada;

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

    public User getOdontologo() {
        return odontologo;
    }

    public void setOdontologo(User odontologo) {
        this.odontologo = odontologo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getSobreturno() {
        return sobreturno;
    }

    public void setSobreturno(Boolean sobreturno) {
        this.sobreturno = sobreturno;
    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Atencion getAtencion() {
        return atencion;
    }

    public void setAtencion(Atencion atencion) {
        this.atencion = atencion;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Boolean getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(Boolean finalizada) {
        this.finalizada = finalizada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Turno turno = (Turno) o;

        if ( ! Objects.equals(id, turno.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Turno{" +
                "id=" + id +
                ", fecha='" + fecha + "'" +
                ", observacion='" + observacion + "'" +
                ", sobreturno='" + sobreturno + "'" +
                '}';
    }
}
