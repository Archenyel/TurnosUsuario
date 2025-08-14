package mx.edu.uteq.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Profesor {
    @JsonProperty("idProfesor") 
    private Integer id;
    private String nombre;
    private String correo;
    private String cubiculo;
    private boolean activo;
    private int idUsuario;
    // Si el microservicio de profesores retorna más campos, agregarlos aquí.
}
