package cl.javadevs.springsecurityjwt.repositories;

import cl.javadevs.springsecurityjwt.models.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IValoracionRepository extends JpaRepository<Valoracion,Long> {
}
