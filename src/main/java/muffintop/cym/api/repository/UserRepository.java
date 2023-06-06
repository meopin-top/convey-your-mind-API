package muffintop.cym.api.repository;


import muffintop.cym.api.domain.User;
import muffintop.cym.api.domain.key.UserPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UserPk> {

    @Override
    boolean existsById(UserPk userPk);
}