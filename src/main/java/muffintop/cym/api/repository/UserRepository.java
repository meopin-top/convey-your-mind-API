package muffintop.cym.api.repository;


import java.util.Optional;
import muffintop.cym.api.domain.User;
import muffintop.cym.api.domain.key.UserPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UserPk> {

    @Override
    boolean existsById(UserPk userPk);

    Optional<User> findUserByIdAndAuthMethod(String userId, char authMethod);

    Optional<User> findUserByEmailAndAuthMethod(String email, char authMethod);

    boolean existsByEmailAndAuthMethod(String email, char authMethod);
}
