package muffintop.cym.api.repository;


import java.util.List;
import java.util.Optional;
import muffintop.cym.api.domain.Project;
import muffintop.cym.api.domain.User;
import muffintop.cym.api.domain.key.UserPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<List<Project>> findAllByUser(User user);

    Optional<Project> findById(Long projectId);

    Optional<Project> findByUserAndId(User user, Long projectId);

    Optional<Project> findByInviteCode(String inviteCode);

    boolean existsByInviteCode(String inviteCode);

}
