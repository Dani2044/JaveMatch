package gajudama.javematch.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import gajudama.javematch.model.VideollamadaJuego;

@Repository
public interface VideollamadaJuegoRepository extends JpaRepository<VideollamadaJuego, Long> {
}
