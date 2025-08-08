package mx.edu.uteq.backend.model.Repositroty;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.edu.uteq.backend.model.Entity.Usuario;
import java.util.List;
import java.util.Optional;


public interface UsuarioRepo extends JpaRepository<Usuario, Integer> {
   Optional<Usuario> findByNombre(String nombre);
    
}