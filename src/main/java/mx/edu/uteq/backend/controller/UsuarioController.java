package mx.edu.uteq.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.edu.uteq.backend.client.ProfesorRest;
import mx.edu.uteq.backend.model.Entity.Usuario;
import mx.edu.uteq.backend.model.Repositroty.UsuarioRepo;
import mx.edu.uteq.backend.model.dto.Profesor;
// import mx.edu.uteq.backend.model.dto.UsuarioProfesorDTO;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private ProfesorRest profesorRest;

    @GetMapping
    public List<Usuario> getAll() {
        return usuarioRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return usuarioRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{username}")
    public ResponseEntity<?> getByName(@PathVariable String username){
        return usuarioRepo.findByNombre(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody JsonNode body) {
    logger.info("Solicitud POST recibida en /api/usuario: {}", body.toString());
        System.out.println("Solicitud POST recibida en /api/usuario: " + body.toString());
        // Extraer usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(body.get("nombre").asText());
        usuario.setContrasena(body.get("contrasena").asText());
        usuario.setEstatus(body.get("estatus").asBoolean());
        usuario.setRol(body.get("rol").asText());

        Usuario usuarioDB = usuarioRepo.save(usuario);

        // Si viene idProfesor, enviar idProfesor y idUsuario al microservicio profesores
        if (body.has("idProfesor") && !body.get("idProfesor").isNull()) {
            int idProfesor = body.get("idProfesor").asInt();
            int idUsuario = usuarioDB.getId();
            logger.info("Llamando a profesorRest.asignarUsuarioAProfesor con idProfesor={} y idUsuario={}", idProfesor, idUsuario);
            profesorRest.asignarUsuarioAProfesor(idProfesor, idUsuario);
        }

        return ResponseEntity.ok(usuarioDB);
    
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUsuario(@PathVariable int id, @RequestBody Usuario usuario) {
        return usuarioRepo.findById(id)
                .map(existingUsuario -> {
                    existingUsuario.setNombre(usuario.getNombre());
                    existingUsuario.setEstatus(usuario.isEstatus());
                    existingUsuario.setRol(usuario.getRol());
                    existingUsuario.setContrasena(usuario.getContrasena());
                    Usuario updatedUsuario = usuarioRepo.save(existingUsuario);
                    return ResponseEntity.ok(updatedUsuario);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
