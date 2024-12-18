package lada.alex.onlinebookstore.repository.role;

import java.util.Optional;
import lada.alex.onlinebookstore.model.Role;
import lada.alex.onlinebookstore.model.Role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
