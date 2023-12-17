package muffintop.cym.api.repository;


import java.util.List;
import java.util.Optional;
import muffintop.cym.api.domain.User;
import muffintop.cym.api.domain.UserProjectHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectHistoryRepository extends JpaRepository<UserProjectHistory, Long> {

    boolean existsByProjectIdAndUserAndType(Long ProjectId, User user, char type);

    Optional<UserProjectHistory> findUserProjectHistoryByProjectIdAndUser(Long ProjectId,
        User user);

    List<UserProjectHistory> findAllByProjectId(Long ProjectId);

    List<UserProjectHistory> findTop3ByUserOrderByExpiredDatetimeDesc(User user);

    long countByUserAndType(User user, char type);

    List<UserProjectHistory> findAllByUserAndType(User user, char type, Pageable pageable);
}
