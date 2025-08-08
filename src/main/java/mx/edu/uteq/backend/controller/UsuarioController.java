package mx.edu.uteq.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.edu.uteq.backend.client.ProfesorRest;
import mx.edu.uteq.backend.model.Entity.Usuario;
import mx.edu.uteq.backend.model.Repositroty.UsuarioRepo;
import mx.edu.uteq.backend.model.dto.Profesor;
import mx.edu.uteq.backend.model.dto.UsuarioProfesorDTO;


@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

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
    public ResponseEntity<?> create(@RequestBody UsuarioProfesorDTO dto) {
        Usuario usuarioDB = usuarioRepo.save(dto.getUsuario());

        // Actualiza el profesor con el nuevo id_usuario
        if (dto.getIdProfesor() != null) {
            ResponseEntity<Profesor> response = profesorRest.getIdsByUsuarioId(dto.getIdProfesor());
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Profesor profesor = response.getBody();
                profesor.setIdUsuario(usuarioDB.getId());
                profesorRest.updateProfesor(profesor.getId(), profesor);
            }
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
