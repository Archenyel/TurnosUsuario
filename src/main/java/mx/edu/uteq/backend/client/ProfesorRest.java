package mx.edu.uteq.backend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import mx.edu.uteq.backend.model.dto.Profesor;

@FeignClient(name = "servicio-profesores", url = "http://localhost:8085") 
public interface ProfesorRest {
    @PutMapping("/api/profesor/{idProfesor}/asignar-usuario/{idUsuario}")
    ResponseEntity<Void> asignarUsuarioAProfesor(@PathVariable("idProfesor") int idProfesor, @PathVariable("idUsuario") int idUsuario);
    
    @GetMapping("/api/profesor/usuario/{idUsuario}")
    ResponseEntity<Profesor> getIdsByUsuarioId(@PathVariable("idUsuario") Integer idUsuario);

    @PutMapping("/api/profesor/{idProfesor}")
    ResponseEntity<Profesor> updateProfesor(@PathVariable("idProfesor") Integer idProfesor, @RequestBody Profesor profesor);
}
