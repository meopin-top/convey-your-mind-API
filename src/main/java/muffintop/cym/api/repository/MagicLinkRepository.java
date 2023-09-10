package muffintop.cym.api.repository;


import java.util.Optional;
import muffintop.cym.api.domain.MagicLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MagicLinkRepository extends JpaRepository<MagicLink, Long> {

    boolean existsById(String id);

    Optional<MagicLink> findById(String id);
}
