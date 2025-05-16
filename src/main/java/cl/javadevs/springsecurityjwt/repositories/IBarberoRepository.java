package cl.javadevs.springsecurityjwt.repositories;

import cl.javadevs.springsecurityjwt.models.Barbero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBarberoRepository extends JpaRepository<Barbero, Long> {

}
