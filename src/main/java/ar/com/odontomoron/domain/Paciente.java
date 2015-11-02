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
 * A Paciente.
 */
@Entity
@Table(name = "PACIENTE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Paciente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "numero_asociado")
    private String numeroAsociado;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "nombre")
    private String nombre;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "fecha_nacimiento")
    private DateTime fechaNacimiento;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "domicilio")
    private String domicilio;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "celular")
    private String celular;

    @Column(name = "dni")
    private String dni;
    
    @Column(name = "numero_de_afiliado")
    private String numeroDeAfiliado;
    
    @Column(name = "lugar_de_trabajo")
    private String lugarDeTrabajo;
    
    @Column(name = "parentezco_con_el_asociado")
    private String parentezcoConElAsociado;
    
    @Column(name = "condicion_de_facturacion")
    private String condicionDeFacturacion;
    
    @Column(name = "plan")
    private String plan;
    
    @Column(name = "correo_electronico")
    private String correoElectronico;
    
    @Column(name = "codigo_postal")
    private String codigoPostal;

    @ManyToOne
    private ObraSocial obraSocial;

    @OneToMany(mappedBy = "paciente")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Turno> turnos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroAsociado() {
        return numeroAsociado;
    }

    public void setNumeroAsociado(String numeroAsociado) {
        this.numeroAsociado = numeroAsociado;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public DateTime getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(DateTime fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public ObraSocial getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }    

    public Set<Turno> getTurnos() {
        return turnos;
    }

    public void setTurnos(Set<Turno> turnos) {
        this.turnos = turnos;
    }

    public String getNumeroDeAfiliado() {
        return numeroDeAfiliado;
    }

    public void setNumeroDeAfiliado(String numeroDeAfiliado) {
        this.numeroDeAfiliado = numeroDeAfiliado;
    }

    public String getLugarDeTrabajo() {
        return lugarDeTrabajo;
    }

    public void setLugarDeTrabajo(String lugarDeTrabajo) {
        this.lugarDeTrabajo = lugarDeTrabajo;
    }

    public String getParentezcoConElAsociado() {
        return parentezcoConElAsociado;
    }

    public void setParentezcoConElAsociado(String parentezcoConElAsociado) {
        this.parentezcoConElAsociado = parentezcoConElAsociado;
    }

    public String getCondicionDeFacturacion() {
        return condicionDeFacturacion;
    }

    public void setCondicionDeFacturacion(String condicionDeFacturacion) {
        this.condicionDeFacturacion = condicionDeFacturacion;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Paciente paciente = (Paciente) o;

        if ( ! Objects.equals(id, paciente.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", numeroAsociado='" + numeroAsociado + "'" +
                ", apellido='" + apellido + "'" +
                ", nombre='" + nombre + "'" +
                ", fechaNacimiento='" + fechaNacimiento + "'" +
                ", sexo='" + sexo + "'" +
                ", domicilio='" + domicilio + "'" +
                ", telefono='" + telefono + "'" +
                ", celular='" + celular + "'" +
                ", dni='" + dni + "'" +
                '}';
    }
}
