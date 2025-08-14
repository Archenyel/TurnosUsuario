package mx.edu.uteq.backend.model.dto;

import lombok.Data;
import mx.edu.uteq.backend.model.Entity.Usuario;

@Data
public class UsuarioProfesorDTO {
    private Usuario usuario;
    private Profesor profesor; // Cambia idProfesor por Profesor
}
